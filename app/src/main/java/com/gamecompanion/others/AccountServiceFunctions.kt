package com.gamecompanion

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountServiceFunctions {
    companion object {
        fun loginUser(activity: Activity, email: String, password: String, retrieveAndSaveUserSharedPrefferencesInfo: Boolean = false)
        {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task->
                    if (task.isSuccessful) {
                        //Sign in success
                        Log.d("AccountService", "signIn:success")
                        val user = auth.currentUser
                        if (retrieveAndSaveUserSharedPrefferencesInfo && user != null){
                            val userid = user.uid
                            val db = FirebaseFirestore.getInstance()
                            db.collection(COLLECTION_USERS).document(userid).get().addOnCompleteListener { task ->
                                val result = task.result
                                if (result != null) {
                                    val userData = result.toObject(UserModel::class.java)
                                    if (userData != null) {
                                        val usernameToSave = userData.username
                                        val userIdToSave = userData.userid
                                        if (usernameToSave != null && userIdToSave != null) {
                                            addUserInfoToSharedPreferences(activity, usernameToSave, userIdToSave)
                                        }
                                    }
                                }

                            }

                        }
                    } else {
                        Log.w("AccountService", "signIn:failure", task.exception)
                        Toast.makeText(activity.baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        fun signUpUser(activity: Activity, username: String, email: String, password: String, progressBar: ProgressBar?)
        {
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        //Sign up success
                        //TODO: update UI with the user's information
                        Log.d("AccountService", "createUserWithEmail:success")

                        loginUser(activity, email, password)

                        val user = auth.currentUser

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
                                        Log.e("AccountService", message)
                                }

                            addUserInfoToSharedPreferences(activity, username, user.uid)

                            //Finish sign up activity
                            activity.finish()
                        }

                    } else {
                        //Sign up failure
                        Log.w("AccountService", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                    //Set progress bar to gone
                    progressBar?.visibility = View.GONE
                }
        }

        fun addUserInfoToSharedPreferences(activity: Activity, username: String, userid: String)
        {
            //TODO: Save username
            val editor = activity.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_USERID, userid)
            editor.putString(PREF_USERNAME, username)
            editor.apply()
        }

        fun getUserInfoFromSharedPrefferences(activity: Activity) : UserModel {
            val userPrefferences = activity?.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
            val username = userPrefferences?.getString(PREF_USERNAME, "")
            val userid = userPrefferences?.getString(PREF_USERID, "")

            return UserModel(userid = userid, username = username, email = null)
        }

        fun clearUserSharedPrefferences(activity: Activity)
        {
            activity.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE).edit().clear().apply()
        }

        fun signOutUser(activity: Activity) {
            FirebaseAuth.getInstance().signOut()
            clearUserSharedPrefferences(activity)
        }
    }
}