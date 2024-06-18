package com.smumc.smumc_6th_teamc_android.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smumc.smumc_6th_teamc_android.R
import com.smumc.smumc_6th_teamc_android.databinding.ActivityLoginBinding
import com.smumc.smumc_6th_teamc_android.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private var studentId: String = ""
    private var password: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 버튼 클릭 시
        binding.signUpStartBt.setOnClickListener {

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

            // 회원가입이 실패했으므로 EditText를 비워줌
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

    private fun resignUp(){ // 회원가입 진행 함수 (1번 이상 비정상적으로 입력했을 경우)

        // xml에서 editText 입력란이 signUpStudentNumberEt, signUpStudentNumberEtEr 두 가지로 나누어져 있기 때문에
        // 1번 이상 비정상적으로 입력했을 경우 signUpStudentNumberEtEr에 입력해야 한다.
        if(resignCheckUp()){ //true: 회원가입 진행

            // 사용자가 입력한 정보를 DB에 저장
            val userDB = UserDatabase.getInstance(this)!!
            userDB.userDao().insert(regetUser())


            // 회원가입 진행 완료 후 로그인 화면으로 이동
            val intent = Intent(this, SignUpCheckActivity::class.java)
            intent.putExtra("studentId", studentId) // 학번 전달

            // 슬라이드 효과 적용
            val options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_in_left)
            startActivity(intent, options.toBundle())
        }

    }
}