package com.example.thoughttracker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thoughttracker.adapters.ThoughtAdapter
import com.example.thoughttracker.database.Converters
import com.example.thoughttracker.database.Thought
import com.example.thoughttracker.database.Topic
import com.example.thoughttracker.database.TopicViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import android.app.Activity
import android.content.DialogInterface
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import kotlinx.serialization.ExperimentalSerializationApi


class ThoughtDetails : AppCompatActivity() {

    lateinit var time:TextView
    private lateinit var thoughts:Array<Thought>
    private lateinit var list:RecyclerView
    private lateinit var back:ImageView
    private lateinit var afterthought:FloatingActionButton
    private lateinit var mTopicViewModel: TopicViewModel
    private lateinit var updatable : Topic
    private lateinit var fa: Activity
    private lateinit var collapsingToolbar:CollapsingToolbarLayout
    private lateinit var menu:ImageView
    private lateinit var hint:LinearLayout



    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thoughts_activity)
        val topic1: Topic = intent.getSerializableExtra("topic") as Topic
        updatable=topic1
        val add = intent.getStringExtra("add")
        val delete = intent.getStringExtra("delete")
        fa=this
        mTopicViewModel= ViewModelProvider(this).get(TopicViewModel::class.java)
        collapsingToolbar = findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbar.title = topic1.topic
        collapsingToolbar.title
        back = findViewById(R.id.imageView)
        hint = findViewById(R.id.addthoughttextid)
        afterthought=findViewById(R.id.addthoughtbutid)
        menu=findViewById(R.id.dropdown_menu)
        thoughts=  Converters().fromString(topic1.thoughts)
        if (thoughts.isEmpty()){
            hint.visibility = View.VISIBLE
        }else{
            hint.visibility=View.GONE
        }
        list = findViewById(R.id.thoughtrecycleid)
        list.layoutManager = LinearLayoutManager(this)
        val adapter = ThoughtAdapter(thoughts,this,updatable,mTopicViewModel,list)
        list.adapter=adapter
        adapter.notifyDataSetChanged()
        if(!TextUtils.isEmpty(add)){
            addThought()
        }
        if(!TextUtils.isEmpty(delete)){
            deleteTopic(topic1)
        }
        back.setOnClickListener {
            val x= Intent(this, MainActivity::class.java)
            x.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(x)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        afterthought.setOnClickListener{
            addThought()
        }
        menu.setOnClickListener{
            val popupMenu = PopupMenu(this,it)
            popupMenu.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.edit_topic->{
                        editTopic()
                        true
                    }
                    R.id.delete_topic->{
                        deleteTopic(topic1)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.topic_options)
            popupMenu.show()
        }
    }

    @SuppressLint("InflateParams")
    private fun editTopic() {
        val dialog2 = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val popup1: View =
            LayoutInflater.from(this).inflate(R.layout.edit_topic_layout, null)
        val updatedTopic: EditText = popup1.findViewById(R.id.topiceditid)
        val finish: ImageView = popup1.findViewById(R.id.submiteditid)
        val temp = updatable
        val model = mTopicViewModel
        updatedTopic.setText(temp.topic)
        finish.setOnClickListener {
            val newTopic = updatedTopic.text.toString()
            if (!TextUtils.isEmpty(newTopic)) {
                if (temp.topic != newTopic) {
                    dialog2.dismiss()
                    temp.topic = newTopic
                    model.updateTopic(temp)
//                    Toast.makeText(this,"Topic updated !",Toast.LENGTH_LONG).show()
                    collapsingToolbar.title = newTopic
                } else {
                    Toast.makeText(this,"No change in Topic !",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this,"Topic is empty !",Toast.LENGTH_LONG).show()
            }
        }
        dialog2.setContentView(popup1)
        dialog2.show()
        dialog2.setCancelable(true)
    }

    override fun onBackPressed() {
        val x= Intent(this, MainActivity::class.java)
        x.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
        startActivity(x)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun deleteTopic(topic3: Topic) {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Yes"){_,_->
            mTopicViewModel.deleteTopic(topic3)
            Toast.makeText(this,"Topic deleted !", Toast.LENGTH_LONG).show()
            val x= Intent(this, MainActivity::class.java)
            x.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
            startActivity(x)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        builder.setNegativeButton("No"){_,_->

        }
        builder.setTitle("Delete ${topic3.topic} for sure?")
        builder.setMessage("saved thoughts of the topic would also be deleted")
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
        val btnOk: Button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val btnCancel: Button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)

        btnOk.setTextColor(ContextCompat.getColor(this, R.color.black))
        btnCancel.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    @SuppressLint("SimpleDateFormat")
    private fun currentTime(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return sdf.format(Date())
    }

    @ExperimentalSerializationApi
    @SuppressLint("NotifyDataSetChanged", "InflateParams")
    fun addThought() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val popup:View=LayoutInflater.from(this).inflate(R.layout.add_thought_layout,null)
        val thought:EditText = popup.findViewById(R.id.thoughttextid)
        val finish:ImageView=popup.findViewById(R.id.submitthoughtid)
        finish.setOnClickListener{
            if(thought.text.isNotEmpty()) {
                dialog.dismiss()
                val t: Thought = Thought(
                    collapsingToolbar.title.toString(),
                    currentTime(),
                    thought.text.toString(),
                    false
                )
                var thoughtArray = Converters().fromString(updatable.thoughts)
                thoughtArray += t
                val thoughtString = Converters().toString(thoughtArray)
                updatable.thoughts = thoughtString
                mTopicViewModel.updateTopic(updatable)
                val adapter = ThoughtAdapter(thoughtArray, this, updatable, mTopicViewModel, list)
                list.adapter = adapter
                adapter.notifyDataSetChanged()
                hint.visibility = View.INVISIBLE
            }else{
                Toast.makeText(this,"Thought is empty !",Toast.LENGTH_SHORT).show()
            }
        }
        dialog.setContentView(popup)
        dialog.show()
        dialog.setCancelable(true)
    }


}


