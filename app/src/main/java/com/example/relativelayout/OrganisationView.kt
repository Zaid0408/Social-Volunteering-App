package com.example.relativelayout

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class OrganisationView :AppCompatActivity(){
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.organisation_view)
        val container = findViewById<LinearLayout>(R.id.container)
        val addPostButton= findViewById<FloatingActionButton>(R.id.button2)
        addPostButton.visibility=View.VISIBLE
        val postsReference = FirebaseDatabase.getInstance().reference.child("posts")

        // Attach a ValueEventListener to fetch data from Firebase
        postsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Iterate through each post in the 'posts' node
                for (postSnapshot in snapshot.children) {
                    // Retrieve relevant details (name, content, image) for each post
                    val name = postSnapshot.child("name").getValue(String::class.java) ?: ""
                    val content = postSnapshot.child("content").getValue(String::class.java) ?: ""
                    val image = postSnapshot.child("image").getValue(String::class.java) ?: ""
                    val id=postSnapshot.child("id").getValue(String::class.java)?: ""
                    val resourceId = resources.getIdentifier(image, "drawable", packageName)

                    addPost(container, name, content,resourceId,id)

                }
                // Add post function works well when initialized here
                //addPost(container, "Volunteering Event 4", "Join us for a community cleanup this weekend! Let's make a positive impact together.",R.drawable.groupofhappypeople2)

            }

            override fun onCancelled(error: DatabaseError) {
                println("Error fetching data from Firebase: $error")
            }
        })
        // Adding posts dynamically
        //addPost(container, "Volunteering Event 4", "Join us for a community cleanup this weekend! Let's make a positive impact together.",R.drawable.groupofhappypeople2)
        //addPost(container, "Volunteering Event 5", "Join us for a community cleanup this weekend! Let's make a positive impact together.",R.drawable.sad1)
        //addPost(container, "Volunteering Event 6", "Join us for a community cleanup this weekend! Let's make a positive impact together.",R.drawable.pngtreepreview)
        addPostButton.setOnClickListener {

            val intent=Intent(this,VolunteerPost::class.java)
            startActivity(intent)
        }

    }

    private fun addPost(container: LinearLayout, title: String, content: String, imageResId: Int,id:String) {
        val postContainer = LinearLayout(this)
        postContainer.orientation = LinearLayout.VERTICAL
        val containerId = ViewCompat.generateViewId() // Generate a unique ID
        postContainer.id = containerId
        postContainer.setBackgroundResource(android.R.color.holo_blue_light)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.post_container_height)
        )
        val marginInDp = 16
        val marginInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            marginInDp.toFloat(),
            resources.displayMetrics
        ).toInt()
        layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)
        postContainer.layoutParams = layoutParams
        val paddingInDp = 16
        val paddingInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            paddingInDp.toFloat(),
            resources.displayMetrics
        ).toInt()
        postContainer.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)


        val titleTextView = TextView(this)
        titleTextView.text = title
        titleTextView.textSize = 17f
        titleTextView.setTypeface(null, Typeface.BOLD)
        titleTextView.setTextColor(Color.RED)
        postContainer.addView(titleTextView)


        val contentTextView = TextView(this)
        contentTextView.text = content
        contentTextView.textSize = 13f
        val contentLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val contentMarginInDp = 5
        val contentMarginInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            contentMarginInDp.toFloat(),
            resources.displayMetrics
        ).toInt()
        contentLayoutParams.setMargins(
            contentMarginInPixels,
            contentMarginInPixels,
            contentMarginInPixels,
            contentMarginInPixels
        )
        contentTextView.layoutParams = contentLayoutParams
        postContainer.addView(contentTextView)


        val imageView = ImageView(this)
        imageView.setImageResource(imageResId)
// Set layout parameters with margin
        val imageLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.post_image_height)
        )
        val marginInDpImage = 20
        val marginInPixelsImage = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            marginInDpImage.toFloat(),
            resources.displayMetrics
        ).toInt()
        imageLayoutParams.setMargins(
            marginInPixelsImage,
            marginInPixelsImage,
            marginInPixelsImage,
            marginInPixelsImage
        ) // Set margin in pixels
        imageView.layoutParams = imageLayoutParams
// Set padding
        val paddingInDpImage = 10
        val paddingInPixelsImage = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            paddingInDpImage.toFloat(),
            resources.displayMetrics
        ).toInt()
        imageView.setPadding(
            paddingInPixelsImage,
            paddingInPixelsImage,
            paddingInPixelsImage,
            paddingInPixelsImage
        ) // Set padding in pixels
        postContainer.addView(imageView)


        container.addView(postContainer)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun openLoginPage(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Firebase.auth.signOut()
        Toast.makeText(baseContext, "Signed Out Successfully.", Toast.LENGTH_SHORT,).show()
    }

}


