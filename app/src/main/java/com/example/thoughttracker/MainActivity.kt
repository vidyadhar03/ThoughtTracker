package com.example.thoughttracker

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thoughttracker.adapters.TopicAdapter
import com.example.thoughttracker.database.Converters
import com.example.thoughttracker.database.Thought
import com.example.thoughttracker.database.Topic
import com.example.thoughttracker.database.TopicViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.serialization.ExperimentalSerializationApi
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var topics: Array<Topic>
    private lateinit var thoughts:Array<Thought>
    private lateinit var mTopicViewModel: TopicViewModel
    private lateinit var viewLifecycleOwner: LifecycleOwner
    private lateinit var adapter: TopicAdapter
    private lateinit var hid:LinearLayout
    private lateinit var info:ImageView
    private lateinit var topic:FloatingActionButton



    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mTopicViewModel=ViewModelProvider(this).get(TopicViewModel::class.java)
        recyclerView=findViewById(R.id.recycleid)
        hid=findViewById(R.id.hiddentopicid)
        info=findViewById(R.id.infoid)
        topic=findViewById(R.id.addtopicbutid)
        topic.setOnClickListener{
            val dialog:Dialog = BottomSheetDialog(this,R.style.BottomSheetDialog)
            val popup:View=layoutInflater.inflate(R.layout.add_topic_layout,null)
            val submit:ImageView=popup.findViewById(R.id.submitid)
            val topic:EditText=popup.findViewById(R.id.topictextid)
            submit.setOnClickListener {
                if(topic.text.toString().isNotEmpty()) {
                    dialog.dismiss()
                    val converter : String = Converters().toString(thoughts)
                    val new1 =
                        Topic(0,topic = topic.text.toString(), currentTime(), converter)
                    insertDataDataBase(new1)
                }else{
                    Toast.makeText(this,"Topic is empty !",Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setContentView(popup)
            dialog.show()
            dialog.setCancelable(true)
        }
        info.setOnClickListener{
            val dialog = Dialog(this)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.info_bottom)
            dialog.setCancelable(true)
            dialog.show()
        }
        val staggeredGridLayoutManager = GridLayoutManager(this,2)
        recyclerView.layoutManager=staggeredGridLayoutManager
        topics= arrayOf()
        thoughts= arrayOf()
        viewLifecycleOwner  = this
        updateData()

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun updateData() {
        mTopicViewModel.readAllData.observe(viewLifecycleOwner, { topics->
            if(topics.isEmpty()){
                hid.visibility=View.VISIBLE
            }else{
                hid.visibility=View.INVISIBLE
            }
            adapter= TopicAdapter(topics, mTopicViewModel,this)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }


    private fun insertDataDataBase(topic: Topic) {
        mTopicViewModel.addTopic(topic)
    }


    @SuppressLint("SimpleDateFormat")
    private fun currentTime(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return sdf.format(Date())
    }


}