package com.gamecompanion

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSend = view.findViewById<Button>(R.id.message_btn_send)
        btnSend.setOnClickListener {
            //TODO: Send text to db, save as user message, clear text box, hide keyboard
            val db = FirebaseFirestore.getInstance()

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null)
            {
                val userText = view.findViewById<EditText>(R.id.message_input).text.toString()

                val userModel = AccountServiceFunctions.getUserInfoFromSharedPrefferences(activity!!)
                val userMessage = MessageModel(text = userText, createdAt = Date(), userid = userModel.userid, username = userModel.username)

                Log.i("HomeFragment", "Text Message: " + userText)
                db.collection(COLLECTION_MESSAGES).add(userMessage)
                    .addOnSuccessListener {
                        //TODO: Refresh message list
                        loadFirebaseMessages()
                    }
                    .addOnFailureListener {
                        Log.e("HomeFragment", it.message!!)
                        Toast.makeText(activity, "Message not sent, try again later.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(activity, "You can not send messages if you are not logged in.", Toast.LENGTH_SHORT).show()
            }
        }

        layoutManager = LinearLayoutManager(activity)
        adapter = MessageRecyclerViewAdapter(loadFirebaseMessages())
    }

    private fun loadFirebaseMessages() : List<MessageModel> {
        val db = FirebaseFirestore.getInstance()
        val messages = mutableListOf<MessageModel>()
        db.collection(COLLECTION_MESSAGES).get().addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                //Retrieved collection successfully
                task.result?.forEach { documentSnapShot ->
                    val message = documentSnapShot.toObject(MessageModel::class.java)
                    Log.i("HomeFragment", "Got message from Firestore: " + message)
                    messages.add(message)
                }
            }
        }

        return messages.toList()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}