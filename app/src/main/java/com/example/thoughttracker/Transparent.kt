package com.example.thoughttracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.thoughttracker.database.Topic
import com.example.thoughttracker.database.TopicViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class Transparent : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transparent)

        val topic1: Topic = intent.getSerializableExtra("topic") as Topic
        val add = intent.getStringExtra("reload")
        val edit = intent.getStringExtra("main")


        if(add!=null){
            finish()
            overridePendingTransition(0, 0)
            val x= Intent(this, ThoughtDetails::class.java)
            x.putExtra("topic",topic1)
            x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(x)
            overridePendingTransition(0, 0)
        }

        if(edit!=null){
            finish()
            overridePendingTransition(0, 0)
            val x= Intent(this, MainActivity::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(x)
            overridePendingTransition(0, 0)
        }


    }

}