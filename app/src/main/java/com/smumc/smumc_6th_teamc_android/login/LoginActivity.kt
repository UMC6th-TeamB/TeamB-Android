package com.smumc.smumc_6th_teamc_android.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smumc.smumc_6th_teamc_android.databinding.ActivityLoginBinding
import com.smumc.smumc_6th_teamc_android.map.MapActivity

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // "회원가입" 텍스트에 밑줄 추가
        binding.signUpBt.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        // 회원가입 버튼 클릭 시 SingUpActivity(회원가입 화면)으로 이동
        binding.signUpBt.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 클릭 시
        binding.loginStartTv.setOnClickListener{
            // 학번, 비밀번호를 확인하는 함수 호출 (loginCheck)
            if(loginCheck()){
                // 올바르게 입력 시 로그인 진행
                login()
            }
        }

        // 학번 EditText 변경 리스너
        binding.loginStudentNumberEtEr.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.loginStudentNumberEt.visibility = View.VISIBLE
                binding.loginStudentNumberEtEr.visibility = View.GONE
                binding.loginPasswordEt.visibility = View.VISIBLE
                binding.loginPasswordEtEr.visibility = View.GONE
            }
        }

        // 비밀번호 EditText 변경 리스너
        binding.loginPasswordEtEr.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.loginStudentNumberEt.visibility = View.VISIBLE
                binding.loginStudentNumberEtEr.visibility = View.GONE
                binding.loginPasswordEt.visibility = View.VISIBLE
                binding.loginPasswordEtEr.visibility = View.GONE
            }
        }

        // 배경화면 클릭 시 키보드 숨기기
        binding.loginActivity.setOnTouchListener { _, event ->
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

    private fun loginCheck(): Boolean { // 학번, 비밀번호 확인 함수

        // 1. 학번 또는 비밀번호를 입력하지 않은 경우 (빈칸)
        if (binding.loginStudentNumberEt.text.toString().isEmpty() ||binding.loginPasswordEt.text.toString().isEmpty()){

            // 학번 (visible or gone)
            binding.loginStudentNumberEt.visibility = View.GONE // 기존 학번 editText는 보이지 않게 설정
            binding.loginStudentNumberEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 학번 editText를 보이게 설정

            // 비밀번호 (visible or gone)
            binding.loginPasswordEt.visibility = View.GONE // 기존 비밀번호 editText는 보이지 않게 설정
            binding.loginPasswordEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 비밀번호 editText를 보이게 설정

            // 로그인 실패했으므로 EditText를 비워줌
            binding.loginStudentNumberEt.text.clear()
            binding.loginPasswordEt.text.clear()

            Toast.makeText(this, "로그인에 실패했습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show()

            return false
        }

        val studentId = binding.loginStudentNumberEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        // 사용자가 입력한 학번과 비밀번호의 정보가 존재하는지 확인
        val userDB = UserDatabase.getInstance(this)!!
        val user = userDB.userDao().getUser(studentId, password)

        // 2. 사용자가 입력한 학번과 비밀번호의 정보가 DB에 존재하지 않는 경우
        if (user == null){
            // 학번 (visible or gone)
            binding.loginStudentNumberEt.visibility = View.GONE // 기존 학번 editText는 보이지 않게 설정
            binding.loginStudentNumberEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 학번 editText를 보이게 설정

            // 비밀번호 (visible or gone)
            binding.loginPasswordEt.visibility = View.GONE // 기존 비밀번호 editText는 보이지 않게 설정
            binding.loginPasswordEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 비밀번호 editText를 보이게 설정

            // 로그인 실패했으므로 EditText를 비워줌
            binding.loginStudentNumberEt.text.clear()
            binding.loginPasswordEt.text.clear()

            Toast.makeText(this, "로그인에 실패했습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show()

            return false
        }

        return true
    }

    private fun login(){ // 로그인 진행 함수
        // MapActivity.kt로 이동
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "로그인에 성공했습니다!", Toast.LENGTH_SHORT).show()
    }
}