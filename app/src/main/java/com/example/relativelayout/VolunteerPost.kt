package com.example.relativelayout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.logging.Handler

data class Post1(
    val id: String?,
    val content: String,
    val date: String,
    val detailedDescription: String,
    val image: String,
    val name: String?,
    val oid: String?,
    val venue: String
)

class VolunteerPost :AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addost)

        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().reference.child("posts")

        val editTextDescription = findViewById<EditText>(R.id.editTextText1)
        val editTextContent = findViewById<EditText>(R.id.editTextContent)
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val editTextVenue = findViewById<EditText>(R.id.editTextVenue)
        val postButton = findViewById<Button>(R.id.button)


        postButton.setOnClickListener {
            val description = editTextDescription.text.toString().trim()
            val content = editTextContent.text.toString().trim()
            val date = editTextDate.text.toString().trim()
            val venue = editTextVenue.text.toString().trim()
            val image = "drawable/pngtreepreview" // Replace with the actual image path

            if (description.isNotEmpty() && content.isNotEmpty() && date.isNotEmpty() && venue.isNotEmpty()) {
                // Get current organisation's email
                val orgEmail = auth.currentUser?.email
                //val orgEmail="zaidhasanfoundation@gmail.com"
                // Query to find the organisation based on email
                val orgQuery = FirebaseDatabase.getInstance().reference.child("organisations")
                    .orderByChild("email").equalTo(orgEmail)

                orgQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // There should be only one result
                            val orgSnapshot = snapshot.children.first()
                            // Get organisation name
                            val orgName = orgSnapshot.child("name").getValue(String::class.java)
                            // Generate a unique post ID
                            val id = databaseReference.push().key
                            // Create a Post object
                            val post = Post1(id, content, date, description, image, orgName, orgSnapshot.key, venue)

                            // Store the post in the database
                            id?.let {
                                databaseReference.child(it).setValue(post)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(baseContext,"Post Updated successfully!",Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(baseContext,"Post Updation failure!",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle the error
                    }
                })

                val intent=Intent(this,OrganisationView::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(baseContext,"Please Enter the remaining fields as all fields are compulsory",Toast.LENGTH_SHORT).show()
            }
        }
    }
}