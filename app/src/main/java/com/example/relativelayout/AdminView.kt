package com.example.relativelayout

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
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
                    createCardView(container,name,content,resourceId,id)

                    //addPost(container, name, content,resourceId,id)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                println("Error fetching data from Firebase: $error")
            }
        })
    }
    private fun createCardView(container: LinearLayout, title: String, content: String, imageResId: Int, id: String) {
        val cardView = CardView(this)
        val cardLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        cardView.layoutParams = cardLayoutParams
        cardView.cardElevation = resources.getDimensionPixelSize(R.dimen.card_elevation).toFloat()
        cardView.radius = resources.getDimensionPixelSize(R.dimen.card_corner_radius).toFloat()
        cardView.useCompatPadding = true
        //cardView.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))
        cardView.setBackgroundResource(R.drawable.gradient_background_2)


        val postContainer = LinearLayout(this)
        postContainer.orientation = LinearLayout.VERTICAL
        val containerId = ViewCompat.generateViewId()
        postContainer.id = containerId
        //postContainer.setBackgroundResource(android.R.color.holo_blue_light)
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
        titleTextView.setTextColor(Color.WHITE)
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
            val intent = Intent(this, AdminEdit::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
        postContainer.addView(contentTextView)

        val imageView = ImageView(this)
        imageView.setImageResource(imageResId)
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
        )
        imageView.layoutParams = imageLayoutParams
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
        )
        postContainer.addView(imageView)

        cardView.addView(postContainer)
        container.addView(cardView)

    }



}