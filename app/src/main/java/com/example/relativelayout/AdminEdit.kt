package com.example.relativelayout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminEdit : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_edit)
        val postsReference = FirebaseDatabase.getInstance().reference.child("posts")
        val intent1=intent
        val postIdToFetch = intent1.getStringExtra("id")
        val DeletePost= findViewById<Button>(R.id.button3)
        val container=findViewById<LinearLayout>(R.id.eventDetails)
        if (postIdToFetch != null) {
            postsReference.child(postIdToFetch).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if the post exists
                    if (dataSnapshot.exists()) {
                        // Fetch values from the dataSnapshot
                        val name = dataSnapshot.child("name").getValue(String::class.java)
                        val content = dataSnapshot.child("content").getValue(String::class.java)
                        val detailedDescription = dataSnapshot.child("detailedDescription").getValue(String::class.java)
                        val venue = dataSnapshot.child("venue").getValue(String::class.java)
                        val date = dataSnapshot.child("date").getValue(String::class.java)

                        //to get volunteers from child node in posts
                        val volunteersList = ArrayList<Map<String, String>>()
                        val volunteersNode = dataSnapshot.child("volunteers")
                        for (volunteerSnapshot in volunteersNode.children) {
                            val volunteerName = volunteerSnapshot.child("name").getValue(String::class.java)
                            val volunteerEmail = volunteerSnapshot.child("email").getValue(String::class.java)

                            if (volunteerName != null && volunteerEmail != null) {
                                val volunteerDetails = mapOf("name" to volunteerName, "email" to volunteerEmail)
                                volunteersList.add(volunteerDetails)
                            }
                            else{
                                addTextView(container, "There are no registered Volunteers for this post", "\n")
                            }
                        }

                        PostDetails(container,name,content,detailedDescription,venue,date)

                        printVolunteerDetails(container, volunteersList,postIdToFetch)

                        //Log.d("FirebaseData", "Name: $name, Content: $content, Detailed Description: $detailedDescription, Venue: $venue, Date: $date")
                    } else {
                        Toast.makeText(baseContext,"postIdToFetch $postIdToFetch does not exist.", Toast.LENGTH_SHORT).show()
                        Log.d("FirebaseData", "Post with ID $postIdToFetch does not exist.")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching post data: $databaseError")
                }
            })
        }
        else{
            Toast.makeText(baseContext,"postIdToFetch is a null value", Toast.LENGTH_SHORT).show()
        }
        DeletePost.setOnClickListener {
            if (postIdToFetch != null) {
                // Get the DatabaseReference for the specific post
                val postReference = postsReference.child(postIdToFetch)
                // Remove the post from the database
                postReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Post deletion successful
                        Toast.makeText(baseContext, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                        val intent=Intent(this,AdminView::class.java)
                        startActivity(intent)
                        // Optionally, navigate back to the previous screen or update UI accordingly
                    } else {
                        // Post deletion failed
                        Toast.makeText(baseContext, "Failed to delete post", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // postIdToFetch is null
                Toast.makeText(baseContext, "Post ID is null", Toast.LENGTH_SHORT).show()
            }
            val intent=Intent(this,AdminView::class.java)
            startActivity(intent)
        }

    }
    private fun PostDetails(container:LinearLayout,name:String?,content:String?,detailedDescription: String?,venue:String?,date:String?) {
        addTextView(container, "Organisation: ", name)
        addTextView(container, "Content: ", content)
        addTextView(container, "Description: ", detailedDescription)
        addTextView(container, "Venue: ", venue)
        addTextView(container, "Date: ", date)
    }
    fun printVolunteerDetails(container: LinearLayout, volunteersList: List<Map<String, String>>,postId:String) {
        // Iterate through volunteersList and add details to the LinearLayout
        var i:Int=1
        for (volunteerDetails in volunteersList) {
            val volunteerName = volunteerDetails["name"]
            val volunteerEmail = volunteerDetails["email"]

            if (volunteerName != null && volunteerEmail != null) {
                // Create a TextView to display volunteer details
                    addTextViewVolunteer(container,"Volunteer name$i: $volunteerName","Email: $volunteerEmail",postId)
                    i=i+1
            }
        }
    }



    private fun addTextView(container: LinearLayout, labelText: String, respectiveContent:String?) {
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
    private fun addTextViewVolunteer(container: LinearLayout, labelText: String, respectiveContent:String,postId: String) {
        val textView = TextView(this)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.textSize = 18f
        (textView.layoutParams as LinearLayout.LayoutParams).topMargin = resources.getDimensionPixelSize(R.dimen.volunteer_form_margin_top)
        (textView.layoutParams as LinearLayout.LayoutParams).bottomMargin = resources.getDimensionPixelSize(R.dimen.volunteer_form_margin_top)
        textView.text = labelText +"\n"+ respectiveContent
        textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        textView.isClickable=true
        val volunteerEmail=extractEmailFromText(respectiveContent)
        textView.setOnClickListener {

            val popupMenu=PopupMenu(this,textView)
            popupMenu.menuInflater.inflate(R.menu.delete_volunteer,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.delete_volunteer ->
                        DeleteVolunteer(postId,volunteerEmail)

            }
                true
            })
            popupMenu.show()

        }
        container.addView(textView)
    }

    private fun DeleteVolunteer(postId: String, volunteerEmail: String?) {
        val postsReference = FirebaseDatabase.getInstance().reference.child("posts")
        val postReference = postsReference.child(postId)
        // Get the DatabaseReference for the volunteers node
        val volunteersReference = postReference.child("volunteers")
        volunteersReference.orderByChild("email").equalTo(volunteerEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val volunteerSnapshot = dataSnapshot.children.firstOrNull()
                    if (volunteerSnapshot != null) {
                        // Remove the volunteer with the specified email
                        volunteerSnapshot.ref.removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Volunteer deletion successful
                                Toast.makeText(baseContext, "Volunteer deleted successfully", Toast.LENGTH_SHORT).show()
                                // Optionally, update UI accordingly
                            } else {
                                // Volunteer deletion failed
                                Toast.makeText(baseContext, "Failed to delete volunteer", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Volunteer with the specified email not found
                        Toast.makeText(baseContext, "Volunteer with email $volunteerEmail not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching volunteer data: $databaseError")
                }
            })
        val intent=Intent(this,AdminView::class.java)
        startActivity(intent)
    }

    fun extractEmailFromText(text: String): String? {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val matchResult = emailRegex.find(text)
        return matchResult?.value
    }

}