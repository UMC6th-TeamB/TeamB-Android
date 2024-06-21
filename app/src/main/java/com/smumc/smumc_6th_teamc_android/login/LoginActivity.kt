package com.smumc.smumc_6th_teamc_android.login

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
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
            if(loginCheck()){ // true: 올바르게 입력 시 로그인 진행 함수 호출 (login)
                login()
            }
            else { // false: 올바르게 입력하지 않을 시 다시 입력하는 함수 호출 (relogin)
                relogin()
            }
        }
    }

    private fun loginCheck(): Boolean { // 로그인 입력 확인 함수

        // 1. 학번 또는 비밀번호를 입력하지 않은 경우 (빈칸)
        if (binding.loginStudentNumberEt.text.toString().isEmpty() ||binding.loginPasswordEt.text.toString().isEmpty()){

            // 학번 (visible or gone)
            binding.loginStudentNumberEt.visibility = View.GONE // 기존 학번 editText는 보이지 않게 설정
            binding.loginStudentNumberEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 학번 editText를 보이게 설정

            // 비밀번호 (visible or gone)
            binding.loginPasswordEt.visibility = View.GONE // 기존 비밀번호 editText는 보이지 않게 설정
            binding.loginPasswordEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 비밀번호 editText를 보이게 설정

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

            Toast.makeText(this, "로그인에 실패했습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show()

            return false
        }

        return true
    }

    private fun reloginCheck(): Boolean { // 로그인 입력 확인 함수 (1번 이상 비정상적으로 입력했을 경우)

        // 학번 또는 비밀번호를 입력하지 않은 경우
        if (binding.loginStudentNumberEtEr.text.toString().isEmpty() ||binding.loginPasswordEtEr.text.toString().isEmpty()){

            // 학번 (visible or gone)
            binding.loginStudentNumberEt.visibility = View.GONE // 기존 학번 editText는 보이지 않게 설정
            binding.loginStudentNumberEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 학번 editText를 보이게 설정

            // 비밀번호 (visible or gone)
            binding.loginPasswordEt.visibility = View.GONE // 기존 비밀번호 editText는 보이지 않게 설정
            binding.loginPasswordEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 비밀번호 editText를 보이게 설정

            Toast.makeText(this, "로그인에 실패했습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show()

            return false
        }

        val studentId = binding.loginStudentNumberEtEr.text.toString()
        val password = binding.loginPasswordEtEr.text.toString()

        // 사용자가 입력한 학번과 비밀번호의 정보가 존재하는지 확인
        val userDB = UserDatabase.getInstance(this)!!
        val user = userDB.userDao().getUser(studentId, password)

        // 사용자가 입력한 학번과 비밀번호의 정보가 DB에 존재하지 않는 경우
        if (user == null){
            // 학번 (visible or gone)
            binding.loginStudentNumberEt.visibility = View.GONE // 기존 학번 editText는 보이지 않게 설정
            binding.loginStudentNumberEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 학번 editText를 보이게 설정

            // 비밀번호 (visible or gone)
            binding.loginPasswordEt.visibility = View.GONE // 기존 비밀번호 editText는 보이지 않게 설정
            binding.loginPasswordEtEr.visibility = View.VISIBLE // 오류 발생 시 나타내는 비밀번호 editText를 보이게 설정

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

    private fun relogin(){ // 로그인 진행 함수 (1번 이상 비정상적으로 입력했을 경우)

        // 다시 학번, 비밀번호를 입력한 후 그 값이 옳은지 확인하는 함수 호출 (reloginCheck)
            // xml에서 editText 입력이 loginStudentNumberEt, loginStudentNumberEtEr 두 가지로 나누어져 있기 때문에
            // 1번 이상 비정상적으로 입력했을 경우 loginStudentNumberEtEr에 입력
        if(reloginCheck()){ // true: 로그인 진행
            // MapActivity.kt로 이동
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "로그인에 성공했습니다!", Toast.LENGTH_SHORT).show()
        }
    }
}