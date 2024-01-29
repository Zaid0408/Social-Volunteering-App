package com.example.relativelayout

import android.content.Intent
import android.nfc.Tag
import android.view.View

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

data class User(
    val name: String = "",
    val email: String = "",
    val password: String = ""
)

class MainActivity3 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var Tag:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        var signName=findViewById<EditText>(R.id.nameEt)
        var signEmail=findViewById<EditText>(R.id.emailEt)
        var signPassword=findViewById<EditText>(R.id.passwordEt)
        var signConfirmPassword=findViewById<EditText>(R.id.confirmPasswordEt)
        var signIn=findViewById<Button>(R.id.SignIn)
        auth = Firebase.auth
        signIn.setOnClickListener {
            val missingFields = mutableListOf<String>()
//check the name field
            if (signName.text.toString().isEmpty()) {
                missingFields.add("Name")
            }
// Check the email field
            if (signEmail.text.toString().isEmpty()) {
                missingFields.add("Email")
            }

// Check the password field
            if (signPassword.text.toString().isEmpty()) {
                missingFields.add("Password")
            }
            //check the name field
            if (signConfirmPassword.text.toString().isEmpty()) {
                missingFields.add("Confirm Password")
            }

            if (missingFields.isNotEmpty()) {
                // Build a message for missing fields
                val errorMessage = "Please fill in the following fields:\n${missingFields.joinToString(", ")}"

                // Display the error message (you can use a Toast or AlertDialog)
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
            else{
                if(signPassword.text.toString() == signConfirmPassword.text.toString())
                {
                    val email:String= signEmail.text.toString()
                    val password:String=signPassword.text.toString()
                    val name:String=signName.text.toString()
                    createUser(name,email,password)

                }
                else{
                    Toast.makeText(baseContext,"Incorrect Password Enter Again ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createUser(name: String, email: String, password: String) {
        Tag="Main Activity3"
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Tag, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val uid = extractUidFromEmail(email)

                    // Create a user object with the details
                    val newUser = User(name, email, password)

                    // Update the 'users' section in Firebase Database with the new user
                    databaseReference.child(uid).setValue(newUser)

                    Toast.makeText(baseContext,"Welocome "+name, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Tag, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                }
            }
    }

    private fun extractUidFromEmail(email: String): String {
        val atIndex = email.indexOf('@')
        return if (atIndex != -1) {
            email.substring(0, atIndex)
        } else {
            email // Fallback: use the entire email as UID if '@' is not found
        }
    }

    fun openNewPage(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun openOrgSignUp(view: View) {
        val intent = Intent(this, MainActivity6::class.java)
        startActivity(intent)
    }
}
