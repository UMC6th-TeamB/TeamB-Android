package com.smumc.smumc_6th_teamc_android.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ActivityLoginBinding
import com.smumc.smumc_6th_teamc_android.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    private var studentId: String = ""
    private var password: String = ""
    private var termsDatas = ArrayList<Terms>()
    private var personalDatas = ArrayList<Personal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 버튼 클릭 시
        binding.signUpStartBt.setOnClickListener {

            // 체크박스가 체크되어 있는지 확인
            if(!binding.checkBox.isChecked){
                Toast.makeText(this, "이용약관 및 개인정보처리방침에 대해 동의해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 학번, 비밀번호 저장
            studentId = binding.signUpStudentNumberEt.text.toString()
            password = binding.signUpPasswordEt.text.toString()

            if(signCheckUp()){ // 학번, 비밀번호 확인하는 함수 호출 (signCheckUp)
                signUp() // 올바르게 입력 시 회원가입 진행
            }
        }
        
        // 학번 EditText 색상 원상 복구
        binding.signUpStudentNumberEtEr.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.signUpStudentNumberEt.visibility = View.VISIBLE
                binding.signUpStudentNumberEtEr.visibility = View.GONE
                binding.signUpPasswordEt.visibility = View.VISIBLE
                binding.signUpPasswordEtEr.visibility = View.GONE
                binding.signUpError.visibility = View.GONE
        }
    }

        // 비밀번호 EditText 색상 원상 복구
        binding.signUpPasswordEtEr.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.signUpStudentNumberEt.visibility = View.VISIBLE
                binding.signUpStudentNumberEtEr.visibility = View.GONE
                binding.signUpPasswordEt.visibility = View.VISIBLE
                binding.signUpPasswordEtEr.visibility = View.GONE
                binding.signUpError.visibility = View.GONE
            }
        }

        // 배경화면 클릭 시 키보드 숨기기
        binding.signUpActivity.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
            }
            true
        }

        // 이용약관 더미 데이터
        termsDatas.apply {
            add(Terms("제 1조 (목적)","이 약관은 카풀 애플리케이션(이하 \"서비스\")의 이용 조건 및 절차, 회원과 서비스 제공자의 권리와 의무를 규정함을 목적으로 합니다.\n"))
            add(Terms("제 2조 (용어 정의)","1. \"회원\"이라 함은 본 약관을 승인하고 서비스에 가입한 자를 말합니다.\n2. \"탑승자\"는 카풀 서비스를 이용하여 이동하는 회원을 말합니다.\n"))
            add(Terms("제 3조 (회원 가입 및 이용자격)","1. 회원은 서비스 가입 절차를 완료하여 이용 자격을 부여받습니다.\n2. 서비스 가입은 상명대학교 학생에 한합니다.\n"))
            add(Terms("제 4조 (서비스의 제공 및 변경)","1. 본 서비스는 학생들의 편의 증대를 위해 제공됩니다.\n2. 버스 배차간격이 길어지거나 혼잡할 때 택시 카풀 서비스를 지원합니다.\n"))
            add(Terms("제 5조 (이용자의 의무)","1. 회원은 서비스 이용 시 타인의 권리를 침해해서는 안 됩니다.\n2. 회원은 법규를 준수하여 안전하게 서비스를 이용해야 합니다.\n"))
            add(Terms("제 6조 (서비스 중단)", "서비스는 기술적 문제나 기타 사유로 인해 중단될 수 있으며, 이 경우 사전 공지합니다.\n"))
            add(Terms("제 7조 (계정 해지 및 서비스 종료)","회원이 원할 경우 언제든지 계정을 해지할 수 있으며, 서비스 제공자는 사전 통지 후 서비스를 종료할 수 있습니다.\n"))
            add(Terms("제 8조 (면책 조항)","서비스 제공자는 부주의로 인한 사고 등 발생한 문제에 대해 책임을 지지 않습니다.\n"))
            add(Terms("제 9조 (분쟁 해결 및 관할 법원)","서비스 이용과 관련한 분쟁은 상호 협의하여 해결하며, 필요 시 관할 법원은 서비스 제공자의 본사 소재지 법원으로 합니다.\n"))
        }

        // terms 어댑터와 이용약관 더미 데이터 연결
        val termsRVAdapter = TermsRVAdapter(termsDatas)
        binding.useRecyclerview.adapter = termsRVAdapter
        binding.useRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false )

        // 개인정보처리방침 더미 데이터
        personalDatas.apply {
            add(Personal("제 1조 (수집하는 개인정보의 항목)","1. 회원가입: 학번, 이메일 주소\n2. 서비스 이용: 위치정보\n"))
            add(Personal("제 2조 (개인정보의 수집 및 이용목적)","1. 서비스 제공 및 운영\n2. 회원관리 및 본인 확인\n3. 서비스 개선 및 사용자 지원\n"))
            add(Personal("제 3조 (개인정보의 보관 및 이용기간)","이용자의 개인정보를 회원 가입 시부터 회원 탈퇴 시까지 보유 및 이용하며, 법령에 따라 보유해야 하는 경우를 제외하고는 개인정보의 수집 및 이용목적이 달성되면 지체없이 파기합니다.\n"))
            add(Personal("제 4조 (개인정보의 파기절차 및 방법)", "1. 파기절차: 이용목적이 달성된 개인정보는 별도의 DB에 옮겨져 일정 기간 저장된 후 파기됩니다.\n2. 파기방법: 전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제합니다.\n"))
            add(Personal("제 5조 (개인정보의 안전성 확보 조치)", "1. 개인정보 암호화\n2. 해킹 등에 대비한 기술적 대책\n3. 접근 통제 및 접근 권한 제한\n"))
            add(Personal("제 6조 (이용자의 권리와 의무)", "1. 이용자는 언제든지 본인의 개인정보를 조회하거나 수정할 수 있으며, 가입 해지를 요청할 수 있습니다.\n2. 이용자는 개인정보의 정확성을 유지하고 보호받을 권리가 있습니다.\n"))
        }

        // terms 어댑터와 이용약관 더미 데이터 연결
        val personalRVAdapter =  PersonalRVAdapter(personalDatas)
        binding.personalRecyclerview.adapter = personalRVAdapter
        binding.personalRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false )

        // 이용 약관 버튼 클릭 시 (이용약관 레이아웃 보임)
        binding.vectorIv1.setOnClickListener {

            // 이용약관 레이아웃 visible
            binding.termsUseLayout.visibility = View.VISIBLE

            // 이용약관 아이콘 이미지 변경
            binding.vectorIv1.visibility = View.GONE
            binding.vectorIv1Change.visibility = View.VISIBLE
        }

        // 이용약관 변경된 아이콘 클릭 시 (이용약관 레이아웃 X)
        binding.vectorIv1Change.setOnClickListener {

            // 이용약관 레이아웃 gone
            binding.termsUseLayout.visibility = View.GONE

            // 이용약관 아이콘 이미지 변경
            binding.vectorIv1.visibility = View.VISIBLE
            binding.vectorIv1Change.visibility = View.GONE
        }

        // 개인정보처리 버튼 클릭 시 (개인정보처리 레이아웃 보임)
        binding.vectorIv2.setOnClickListener {

            // 이용약관 레이아웃 visible
            binding.personalLayout.visibility = View.VISIBLE

            // 이용약관 아이콘 이미지 변경
            binding.vectorIv2.visibility = View.GONE
            binding.vectorIv2Change.visibility = View.VISIBLE
        }

        // 개인정보처리 변경된 아이콘 클릭 시 (개인정보처리 레이아웃 X)
        binding.vectorIv2Change.setOnClickListener {

            // 이용약관 레이아웃 gone
            binding.personalLayout.visibility = View.GONE

            // 이용약관 아이콘 이미지 변경
            binding.vectorIv2.visibility = View.VISIBLE
            binding.vectorIv2Change.visibility = View.GONE

        }


    }

    private fun signCheckUp(): Boolean { // 학번, 비밀번호 확인하는 함수

        // 학번 또는 비밀번호를 입력하지 않은 경우 (빈칸)
        // 현재로선 빈칸 입력 시 오류 발생하는 것으로 구현했습니다.
        if (binding.signUpStudentNumberEt.text.toString().isEmpty() ||binding.signUpPasswordEt.text.toString().isEmpty()){

            // 학번 (visible or gone)
            binding.signUpStudentNumberEt.visibility = View.GONE // 기존 학번 editText는 보이지 않게 설정
            binding.signUpStudentNumberEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 학번 editText를 보이게 설정

            // 비밀번호 (visible or gone)
            binding.signUpPasswordEt.visibility = View.GONE // 기존 비밀번호 editText는 보이지 않게 설정
            binding.signUpPasswordEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 비밀번호 editText를 보이게 설정

            // 입력 오류 시 멘트 (visible)
            binding.signUpError.visibility = View.VISIBLE // "인증되지 않았습니다." text를 보이게 설정

            // 회원가입이 실패했으므로 EditText를 비워줌 (사용자가 입력한 값이 다 삭제됨)
            binding.signUpStudentNumberEt.text.clear()
            binding.signUpPasswordEt.text.clear()

            return false
        }

        return true
    }

    private fun signUp(){ // 회원가입 진행 함수

        // 사용자가 입력한 정보를 DB에 저장
        val userDB = UserDatabase.getInstance(this)!!
        userDB.userDao().insert(User(studentId, password))

        // DB에 저장이 되었는지 Log를 통해서 확인
        val user = userDB.userDao().getUsers()
        Log.d("SIGNUPACT", user.toString())

        // 회원가입 진행 완료 후 인증메일 화면으로 이동
        val intent = Intent(this, SignUpCheckActivity::class.java)
        intent.putExtra("studentId", studentId) // 학번 전달
        startActivity(intent)

        // 슬라이드 효과 적용
        val options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_in_left)
        startActivity(intent, options.toBundle())
    }
}