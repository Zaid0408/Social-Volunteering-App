package com.example.relativelayout


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

data class Organisation(
    val name: String="",
    val contactNo: String="",
    val description: String="",
    val location: String="",
    val email: String="",
    val password: String=""
)


class MainActivity6 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var Tag:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.organisation_sign_up)
        val signName=findViewById<EditText>(R.id.nameEt)
        val signEmail=findViewById<EditText>(R.id.emailEt)
        var signPassword=findViewById<EditText>(R.id.passwordEt)
        var signConfirmPassword=findViewById<EditText>(R.id.confirmPasswordEt)
        var description = findViewById<EditText>(R.id.Description)
        var orgPhone=findViewById<EditText>(R.id.OrgPhone)
        var orgLoc=findViewById<EditText>(R.id.OrgLocation)
        var signUp=findViewById<Button>(R.id.SignUp)
        auth = Firebase.auth
        //auth = Firebase.auth
        signUp.setOnClickListener {

            val missingFields = mutableListOf<String>()

            if (signName.text.toString().isEmpty()) {
                missingFields.add("Name")
            }
            if (signEmail.text.toString().isEmpty()) {
                missingFields.add("Email")
            }

            if (signPassword.text.toString().isEmpty()) {
                missingFields.add("Password")
            }
            if (signConfirmPassword.text.toString().isEmpty()) {
                missingFields.add("Confirm Password")
            }

            if (description.text.toString().isEmpty()) {
                missingFields.add("Description")
            }

            if (orgPhone.text.toString().isEmpty()) {
                missingFields.add("Phone No.")
            }
            if (orgLoc.text.toString().isEmpty()) {
                missingFields.add("Location")
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
                    val des:String=description.text.toString()
                    val phone:String=orgPhone.text.toString()
                    val location:String=orgLoc.text.toString()
                    createOrg(name,email,password,des,location,phone)

                }
                else{
                    Toast.makeText(baseContext,"Incorrect Password Enter Again ", Toast.LENGTH_SHORT).show()
                }

            }

        }

    }
    private fun createOrg(name: String, email: String, password: String,description: String,location: String,contactNo: String) {
        Tag = "Main Activity3"
        val databaseReference = FirebaseDatabase.getInstance().reference.child("organisations")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Tag, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val uid = extractUidFromName(name)
                    //Toast.makeText(baseContext,"Id is"+uid,Toast.LENGTH_SHORT).show()

                     // Create an organisation object with the details
                    val newOrg = Organisation(name,contactNo,description,location,email,password)

                    // Update the 'organisation' section in Firebase Database with the new user
                    databaseReference.child(uid).setValue(newOrg)

                    Toast.makeText(baseContext,"Welocome "+name, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, OrganisationView::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Tag, "createOrgrWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT,).show()
                }
            }
    }
    private fun extractUidFromName(name: String):String{
        val words = name.split(" ")

        // Get the first word (if available) and convert it to lowercase
        return if (words.isNotEmpty()) {
            words[0].lowercase()

        } else {
            // Fallback: if the name is empty, return an empty string
            ""
        }

    }
}
