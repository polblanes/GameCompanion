package com.gamecompanion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val progressBar = findViewById<ProgressBar>(R.id.signup_progressbar)
        progressBar.visibility = View.GONE

        val btnSignUp = findViewById<Button>(R.id.signup_btn_submit)
        btnSignUp.setOnClickListener {
            //Set progress bar to visible

            progressBar.visibility = View.VISIBLE

            //TODO: Sign up
            val auth = FirebaseAuth.getInstance()

            val inputUsername = findViewById<EditText>(R.id.signup_input_username)
            val inputEmail = findViewById<EditText>(R.id.signup_input_email)
            val inputPassword = findViewById<EditText>(R.id.signup_input_username)

            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val username = inputPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Sign up success
                    //TODO: update UI with the user's information
                    Log.d("SignUpActivity", "createUserWithEmail:success")
                    val user = auth.currentUser
                    //TODO: Save username
                    if (user != null){
                        val userModel = UserModel(user.uid, username, user.email)

                        val db = FirebaseFirestore.getInstance()
                        db.collection(COLLECTION_USERS)
                            .document(user.uid)
                            .set(userModel)
                            .addOnSuccessListener {
                                //Sign Up Completed
                                //TODO: Tell user everything was fine and finish
                            }.addOnFailureListener {
                                //TODO: Handle failure
                                val message = it.message
                                if (message != null)
                                    Log.e("SignUpActivity", message)
                            }

                        //Finish sign up activity
                        finish()
                    }

                } else {
                    //Sign up failure
                    Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this@SignUpActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }

                //Set progress bar to gone
                progressBar.visibility = View.GONE
            }
        }

        val signupToLogin = findViewById<TextView>(R.id.signup_to_login)
        signupToLogin.setOnClickListener { view ->
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }
}