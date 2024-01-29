package com.example.relativelayout

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adminview)
        val container = findViewById<LinearLayout>(R.id.container)

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

            }

            override fun onCancelled(error: DatabaseError) {
                println("Error fetching data from Firebase: $error")
            }
        })
    }

    private fun addPost(container: LinearLayout, title: String, content: String, imageResId: Int, id:String) {
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
        contentTextView.isClickable = true
        contentTextView.setOnClickListener {
            // Handle click event, for example, navigate to another activity
            val intent = Intent(this,AdminEdit::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
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

}