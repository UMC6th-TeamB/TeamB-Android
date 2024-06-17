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
    private var studentId: String? = "" // SignUpCheck 액티비티로 전달해야 하는 변수 선언..

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 버튼 클릭 시
        binding.signUpStartBt.setOnClickListener {

            // 학번, 비밀번호를 확인하는 함수 호출 (singCheckUp)
            if(signCheckUp()){ // true: 올바르게 입력 시 회원가입 진행 함수 호출 (singUp)
                studentId = binding.signUpStudentNumberEt.text.toString()
                singUp()
            }
            else { // false: 올바르게 입력하지 않을 시 다시 입력하는 함수 호출 (resignUp)
                studentId = binding.signUpStudentNumberEtEr.text.toString()
                resignUp()
            }
        }
    }

    private fun getUser() : User { // 사용자가 EditText에 입력한 값을 저장하는 함수 // 정상적으로 입력 (signUpStudentNumberEt 저장)
        val studentId: String = binding.signUpStudentNumberEt.text.toString()
        val password : String = binding.signUpPasswordEt.text.toString()

        return User(studentId, password)
    }

    private fun regetUser(): User { // 사용자가 EditText에 입력한 값을 저장하는 함수 // 다시 입력 시도 (signUpStudentNumberEtEr 저장)
        val studentId: String = binding.signUpStudentNumberEtEr.text.toString()
        val password: String = binding.signUpPasswordEtEr.text.toString()

        return User(studentId, password)
    }

    private fun signCheckUp(): Boolean { // 회원가입 입력 확인 함수

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

    private fun resignCheckUp(): Boolean { // 회원가입 입력 확인 함수 (1번 이상 비정상적으로 입력했을 경우)

        // 학번 또는 비밀번호를 입력하지 않은 경우 (signCheckUp 함수와 달리 signUpStudentNumberEtEr 텍스트로 확인)
        if (binding.signUpStudentNumberEtEr.text.toString().isEmpty() ||binding.signUpPasswordEtEr.text.toString().isEmpty()){

            // 학번 (visible or gone)
            binding.signUpStudentNumberEt.visibility = View.GONE
            binding.signUpStudentNumberEtEr.visibility = View.VISIBLE

            // 비밀번호 (visible or gone)
            binding.signUpPasswordEt.visibility = View.GONE
            binding.signUpPasswordEtEr.visibility = View.VISIBLE

            // 입력 오류 시 멘트 (visible)
            binding.signUpError.visibility = View.VISIBLE

            Log.d("SignUpActivity", "signCheckUp failed")

            // 회원가입이 실패했으므로 EditText를 비워줌
            binding.signUpStudentNumberEtEr.text.clear()
            binding.signUpPasswordEtEr.text.clear()

            return false
        }

        return true
    }

    private fun singUp(){ // 회원가입 진행 함수 (처음부터 정상적으로 입력했을 경우)

        // 사용자가 입력한 정보를 DB에 저장
        val userDB = UserDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        // DB에 저장이 되었는지 Log를 통해서 확인
        val user = userDB.userDao().getUsers()
        Log.d("SIGNUPACT", user.toString())


        // 회원가입 진행 완료 후 로그인 화면으로 이동
        val intent = Intent(this, SignUpCheckActivity::class.java)
        intent.putExtra("studentId", studentId) // 학번 전달
        startActivity(intent)
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
            startActivity(intent)
        }

    }

}