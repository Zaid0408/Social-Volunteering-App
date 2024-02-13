package com.example.relativelayout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var Tag:String
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var logEmail=findViewById<EditText>(R.id.LoginEmail)
        var logPassword=findViewById<EditText>(R.id.LoginPassword)
        var logIn=findViewById<Button>(R.id.LogIn)

        auth = Firebase.auth
// Initialize Firebase Auth

        logIn.setOnClickListener {
            val missingFields = mutableListOf<String>()

// Check the username field
            if (logEmail.text.toString().isEmpty()) {
                missingFields.add("Username")
            }

// Check the password field
            if (logPassword.text.toString().isEmpty()) {
                missingFields.add("Password")
            }
            if (missingFields.isNotEmpty()) {
                // Build a message for missing fields
                val errorMessage = "Please fill in the following fields:\n${missingFields.joinToString("\t")}"

                // Display the error message (you can use a Toast or AlertDialog)
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
            else {

                val email:String= logEmail.text.toString()
                val password:String=logPassword.text.toString()
                if(email=="admin@gmail.com" )
                {
                    if(password=="87654321"){
                        Toast.makeText(this, "Welcome Admin", Toast.LENGTH_LONG).show()
                        val intent=Intent(this,AdminView::class.java)
                        startActivity(intent)
                    }
                    else
                    {
                        Toast.makeText(this, "Wrong Password for Admin", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    SignInUser(email,password)
                }
            }
        }
    }

    private fun SignInUser(email: String, password: String) {
        Tag="Main Activity "
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Tag, "signInWithEmail:success")
                    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
                    val user = auth.currentUser
                    //var check:Int
                    // Search in the "users" node
                    val userQuery = databaseReference.child("users").orderByChild("email").equalTo(email)
                    userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email exists in "users" node, it's a user
                                handleUser(dataSnapshot)


                            } else {
                                // Email not found in "users" node, check in "organisations" node
                                val orgQuery = databaseReference.child("organisations").orderByChild("email").equalTo(email)
                                orgQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(orgSnapshot: DataSnapshot) {
                                        if (orgSnapshot.exists()) {
                                            // Email exists in "organisations" node, it's an organization
                                            handleOrganization(orgSnapshot)
//                                            val orgName = orgSnapshot.children.first().child("name").getValue(String::class.java)
//                                            Toast.makeText(baseContext, "Welocome " + orgName, Toast.LENGTH_SHORT).show()
                                        } else {
                                            // Email not found in either "users" or "organisations" node
                                            // Handle accordingly, e.g., show an error message
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Handle database error
                                    }
                                })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle database error
                        }
                    })
//                    if(check==1){
//                        val intent = Intent(this, MainActivity2::class.java)
//                        startActivity(intent)
//                    }
//                    else if(check==0){
//                        val intent = Intent(this, OrganisationView::class.java)
//                        startActivity(intent)
//                    }
//                    Toast.makeText(baseContext, "Welocome " + email, Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity2::class.java)
//                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Tag, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()

                }
            }
    }

    private fun handleUser(dataSnapshot: DataSnapshot) {
        val userName = dataSnapshot.children.first().child("name").getValue(String::class.java)
        Toast.makeText(baseContext, "Welcome $userName", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }

    private fun handleOrganization(orgSnapshot: DataSnapshot) {
        val orgName = orgSnapshot.children.first().child("name").getValue(String::class.java)
        Toast.makeText(baseContext, "Welcome $orgName", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, OrganisationView::class.java)
        intent.putExtra("OrgName",orgName)
        startActivity(intent)
    }

    fun openNewPage(view: View) {
        val intent = Intent(this, MainActivity3::class.java)
        startActivity(intent)
    }
}

