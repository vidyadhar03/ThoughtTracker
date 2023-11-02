package com.example.thoughttracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.thoughttracker.R
import com.example.thoughttracker.Transparent
import com.example.thoughttracker.database.Converters
import com.example.thoughttracker.database.Thought
import com.example.thoughttracker.database.Topic
import com.example.thoughttracker.database.TopicViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.serialization.ExperimentalSerializationApi


class ThoughtAdapter (private val thoughts:Array<Thought>, context: Context, topic: Topic,
                      mTopicViewModel: TopicViewModel,list:RecyclerView) : RecyclerView.Adapter<ThoughtAdapter.RecylerViewHolder>(){

    private val context=context
    private val topic:Topic = topic
    private val model:TopicViewModel = mTopicViewModel
    private val list = list


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecylerViewHolder {
        val gridlayout= LayoutInflater.from(parent.context).inflate(R.layout.thought_item,parent,false)
        return RecylerViewHolder(gridlayout)
    }

    override fun getItemCount(): Int {
        return thoughts.size
    }

    class RecylerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thought: TextView =itemView.findViewById(R.id.thoughttextid)
        val time: TextView =itemView.findViewById(R.id.textView)
        val item: CardView =itemView.findViewById(R.id.thoughtcardid)
        val dots:ImageView=itemView.findViewById(R.id.optionimageid)
        val hidden:TextView=itemView.findViewById(R.id.hiddenid)
    }

    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecylerViewHolder, position: Int) {
        val temp=thoughts[position]
        holder.thought.text = temp.thought
        holder.time.text = temp.time
        holder.dots.setOnClickListener {

            val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            val popup:View=LayoutInflater.from(context).inflate(R.layout.thought_options_layout,null)
            val edit : TextView = popup.findViewById(R.id.editthoughtid)
            val hide : TextView = popup.findViewById(R.id.hideid)
            val delete : TextView = popup.findViewById(R.id.deletetopicid)

            edit.setOnClickListener{
                dialog.dismiss()
                val dialog1 = BottomSheetDialog(context, R.style.BottomSheetDialog)
                val popup1:View=LayoutInflater.from(context).inflate(R.layout.edit_thought_layout,null)
                val finish:ImageView = popup1.findViewById(R.id.finishid)
                val edited:EditText = popup1.findViewById(R.id.thoughteditid)
                edited.setText(temp.thought)
                finish.setOnClickListener{
                    dialog1.dismiss()
                    val thought_array = Converters().fromString(topic.thoughts)
                    val editedThought = edited.text.toString()
                    thought_array[position].thought = editedThought
                    topic.thoughts = Converters().toString(thought_array)
                    model.updateTopic(topic)
                    val x= Intent(context, Transparent::class.java)
                    x.putExtra("topic",topic)
                    x.putExtra("reload","add thought")
                    context.startActivity(x)
                }
                dialog1.setContentView(popup1)
                dialog1.show()
                dialog1.setCancelable(true)
            }

            delete.setOnClickListener{
                dialog.dismiss()
                val x = position
                val builder = AlertDialog.Builder(context)
                builder.setPositiveButton("Yes"){_,_->
                    val thought_array = Converters().fromString(topic.thoughts)
                    val result = Array<Thought>(thought_array.size - 1){ _ -> thought_array[0] }
                    for (i in 0 until x) {
                        result[i] = thought_array[i]
                    }
                    for (i in x until thought_array.size - 1) {
                        result[i] = thought_array[i + 1]
                    }
                    topic.thoughts=Converters().toString(result)
                    model.updateTopic(topic)
                    val x1= Intent(context, Transparent::class.java)
                    x1.putExtra("topic",topic)
                    x1.putExtra("reload","add thought")
                    context.startActivity(x1)
                }
                builder.setNegativeButton("No"){_,_->
                }
                builder.setTitle("Confirm Delete Thought?")
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
                val btnOk: Button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                val btnCancel: Button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)

//                btnOk.setTextColor(btnOk.context.resources.getColor(R.color.black))
                btnOk.setTextColor(ContextCompat.getColor(context, R.color.black))

                btnCancel.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            hide.setOnClickListener{
                dialog.dismiss()
                if(holder.thought.visibility==View.VISIBLE) {
                    holder.thought.visibility=View.GONE
                    holder.hidden.visibility=View.VISIBLE
                }else{
                    holder.thought.visibility=View.VISIBLE
                    holder.hidden.visibility=View.GONE
                }
            }
            dialog.setContentView(popup)
            dialog.show()
            dialog.setCancelable(true)
        }
    }




}
