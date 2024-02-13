package com.example.relativelayout

import android.content.Intent
import android.view.View

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class MainActivity4 : AppCompatActivity() {
    private val tarAudience= arrayOf("Orphanage","Old Age Home","Animal Shelter","Environmental","Collection Drives",)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        val autoCompleteTextView:AutoCompleteTextView=findViewById(R.id.autoCompleteTextView)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>( this,
            android.R.layout.select_dialog_item,tarAudience )
        autoCompleteTextView.threshold=1
        autoCompleteTextView.setAdapter(arrayAdapter)
//        val backbutton=findViewById<Button>(R.id.Backbutton)
//        backbutton.setOnClickListener {
//            val intent = Intent(this, MainActivity2::class.java)
//            startActivity(intent)
//        }
    }
}