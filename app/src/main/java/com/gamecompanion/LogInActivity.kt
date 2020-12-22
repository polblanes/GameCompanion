package com.gamecompanion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val progressBar = findViewById<ProgressBar>(R.id.login_progressbar)
        progressBar.visibility = View.GONE
        
        val loginToSignup = findViewById<TextView>(R.id.login_to_signup)
        loginToSignup.setOnClickListener { view ->
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}