package com.gamecompanion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        progressBar = findViewById<ProgressBar>(R.id.signup_progressbar)
        progressBar.visibility = View.GONE

        val btnSignUp = findViewById<Button>(R.id.signup_btn_submit)
        btnSignUp.setOnClickListener {
            //Set progress bar to visible

            progressBar.visibility = View.VISIBLE

            //TODO: Sign up
            auth = FirebaseAuth.getInstance()

            val inputUsername = findViewById<EditText>(R.id.signup_input_username)
            val inputEmail = findViewById<EditText>(R.id.signup_input_email)
            val inputPassword = findViewById<EditText>(R.id.signup_input_password)

            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val username = inputPassword.text.toString()

            createNewUserWithEmailAndPassword(username, email, password)
        }

        val signupToLogin = findViewById<TextView>(R.id.signup_to_login)
        signupToLogin.setOnClickListener { view ->
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }

    private fun createNewUserWithEmailAndPassword(username: String, email: String, password: String) {
        AccountServiceFunctions.signUpUser(this, username, email, password, progressBar)
    }
}