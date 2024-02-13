package com.example.relativelayout

import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import android.view.View

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.volunteer_form)
        val postsReference = FirebaseDatabase.getInstance().reference.child("posts")
        val intent1=intent
        val postIdToFetch = intent1.getStringExtra("id")
        //val postIdToFetch ="post1"
        val container=findViewById<LinearLayout>(R.id.eventDetails)
        if (postIdToFetch != null) {
            postsReference.child(postIdToFetch).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if the post exists
                    if (dataSnapshot.exists()) {
                        // Fetch values from the dataSnapshot
                        val name = dataSnapshot.child("name").getValue(String::class.java)
                        val content = dataSnapshot.child("content").getValue(String::class.java)
                        val detailedDescription = dataSnapshot.child("detailedDescription").getValue(String::class.java)
                        val venue = dataSnapshot.child("venue").getValue(String::class.java)
                        val date = dataSnapshot.child("date").getValue(String::class.java)
                        createVolunteerForm(container,name,content,detailedDescription,venue,date,postIdToFetch)
                        // Use the fetched values as needed
                        //Log.d("FirebaseData", "Name: $name, Content: $content, Detailed Description: $detailedDescription, Venue: $venue, Date: $date")
                    } else {
                        Toast.makeText(baseContext,"postIdToFetch $postIdToFetch does not exist.",Toast.LENGTH_SHORT).show()

                        Log.d("FirebaseData", "Post with ID $postIdToFetch does not exist.")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching post data: $databaseError")
                }
            })
        }
        else{
            Toast.makeText(baseContext,"postIdToFetch is a null value",Toast.LENGTH_SHORT).show()
        }

    }
    private fun createVolunteerForm(container:LinearLayout,name:String?,content:String?,detailedDescription: String?,venue:String?,date:String?,postId: String){
        addTextView(container, "Organisation: ",name)
        addTextView(container, "Content: ",content)
        addTextView(container, "Description: ",detailedDescription)
        addTextView(container, "Venue: ",venue)
        addTextView(container, "Date: ",date)

        val editTextName = addEditText(container, "Enter Name")
        val editTextEmail = addEditText(container, "Enter Email")
        val phoneNumberUser=addPhoneNumberEditText(container,"Enter Phone No")
        val buttonVolunteer = addButton(container, "Volunteer")
        buttonVolunteer.setOnClickListener {
            val missingFields = mutableListOf<String>()
//check the name field
            if (editTextName.text.toString().isEmpty()) {
                missingFields.add("Name")
            }
// Check the email field
            if (editTextEmail.text.toString().isEmpty()) {
                missingFields.add("Email")
            }

// Check the password field
            if (phoneNumberUser.text.toString().isEmpty()) {
                missingFields.add("Phone No")
            }
            if (missingFields.isNotEmpty()) {
                // Build a message for missing fields
                val errorMessage = "Please fill in the following fields:\n${missingFields.joinToString(", ")}"

                // Display the error message (you can use a Toast or AlertDialog)
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this, "You have successfully registered for the event", Toast.LENGTH_LONG).show()
                val intent = Intent(this,MainActivity2::class.java)
                startActivity(intent)
                val emailRecipient = editTextEmail.text.toString()
                val nameRecipient=editTextName.text.toString()
                addVolunteerToPost(postId,nameRecipient,emailRecipient)
                val subject = "Your email subject"
                val messageBody = "Your custom message goes here"

                val intent1 = Intent(Intent.ACTION_SEND).apply {
                    data = Uri.parse("mailto:") // You can also use "mailto:$emailRecipient" if you want to pre-fill the recipient
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(emailRecipient))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, messageBody)
                }

                if (intent1.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    // Handle the case where no email client is available
                    Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addTextView(container: LinearLayout, labelText: String,respectiveContent:String?) {
        val textView = TextView(this)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.textSize = 18f
        (textView.layoutParams as LinearLayout.LayoutParams).topMargin = resources.getDimensionPixelSize(R.dimen.volunteer_form_margin_top)
        (textView.layoutParams as LinearLayout.LayoutParams).bottomMargin = resources.getDimensionPixelSize(R.dimen.volunteer_form_margin_top)
        textView.text = labelText + respectiveContent
        textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        container.addView(textView)
    }


    private fun addEditText(container: LinearLayout, hint: String): EditText {
        val editText = EditText(this)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.volunteer_form_edittext_height)
        )
        (editText.layoutParams as LinearLayout.LayoutParams).topMargin = resources.getDimensionPixelSize(R.dimen.volunteer_form_margin_top)
        editText.hint = hint
        editText.setTextColor(ContextCompat.getColor(this, R.color.black))
        editText.setHintTextColor(ContextCompat.getColor(this,R.color.hintColor))
        container.addView(editText)
        return editText
    }
    private fun addPhoneNumberEditText(container: LinearLayout, hint: String): EditText {
        val editText = EditText(this)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.volunteer_form_edittext_height)
        )
        (editText.layoutParams as LinearLayout.LayoutParams).topMargin = resources.getDimensionPixelSize(R.dimen.volunteer_form_margin_top)
        editText.hint = hint
        editText.inputType = InputType.TYPE_CLASS_PHONE
        editText.setTextColor(ContextCompat.getColor(this, R.color.black))
        editText.setHintTextColor(ContextCompat.getColor(this,R.color.hintColor))
        container.addView(editText)
        return editText
    }

    private fun addButton(container: LinearLayout, buttonText: String): Button {
        val button = Button(this)
        button.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        (button.layoutParams as LinearLayout.LayoutParams).topMargin = resources.getDimensionPixelSize(R.dimen.volunteer_form_margin_top)
        button.text = buttonText
        container.addView(button)
        return button
    }
    private fun addVolunteerToPost(postId: String, volunteerName: String, volunteerEmail: String) {
        val postsRef:DatabaseReference=FirebaseDatabase.getInstance().reference.child("posts")
        val volunteersRef = postsRef.child(postId).child("volunteers")
        val newVolunteer = mapOf(
            "name" to volunteerName,
            "email" to volunteerEmail
        )

        volunteersRef.push().setValue(newVolunteer)
    }


}