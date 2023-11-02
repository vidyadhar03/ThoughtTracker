package com.example.thoughttracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.thoughttracker.R
import com.example.thoughttracker.ThoughtDetails
import com.example.thoughttracker.Transparent
import com.example.thoughttracker.database.Topic
import com.example.thoughttracker.database.TopicViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.shapes.RectShape

import android.graphics.drawable.ShapeDrawable
import android.widget.*
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt


class TopicAdapter(private val topics:Array<Topic>, mTopicViewModel: TopicViewModel, context: Context): Adapter<TopicAdapter.RecylerViewHolder>() {

    private val context=context
    private val model:TopicViewModel = mTopicViewModel
    private val colors: MutableList<String> = mutableListOf()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecylerViewHolder {
//        colors.add("#a8b6d6")
//        colors.add("#bec8e0")
//        colors.add("#f2f8fb")
//        colors.add("#add8e6")
//        colors.add("#bbdeea")
        colors.add("#c9e5ee")
        colors.add("#d7ebf2")
        colors.add("#e4f2f7")
        colors.add("#c0ceef")
        colors.add("#d0daf3")
        colors.add("#e0e6f7")
        colors.add("#eff3fb")
        var cardLayout= LayoutInflater.from(parent.context).inflate(R.layout.topic_item,parent,false)
        return RecylerViewHolder(cardLayout)
    }



    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: RecylerViewHolder, position: Int) {
        val temp = topics[position]
        holder.topic.text = temp.topic



        val r = Random
        var i1 = r.nextInt( 7- 0) + 0
        val colors1 = IntArray(2)
        colors1[0] = Color.parseColor(colors[i1])
        i1 = r.nextInt(7 - 0) + 0
        colors1[1] = Color.parseColor(colors[i1])
        val gd = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, colors1
        )
        gd.gradientType = GradientDrawable.LINEAR_GRADIENT
        gd.gradientRadius = 300f
        gd.cornerRadius = 0f
        holder.item.background=gd

//        val h =holder.item.layoutParams.height
//        val w= holder.item.layoutParams.width
//
//        if(h<w) holder.item.layoutParams.height=w


        holder.item.setOnClickListener {
            val a = context as Activity
            val x = Intent(context, ThoughtDetails::class.java)
            x.putExtra("topic", temp)
            a.startActivity(x)
            a.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


        holder.item.isLongClickable = true
        holder.item.setOnLongClickListener(OnLongClickListener {
            val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            val popup: View =
                LayoutInflater.from(context).inflate(R.layout.topic_options_layout, null)
            val add: TextView = popup.findViewById(R.id.addtopicid)
            val edit: TextView = popup.findViewById(R.id.edittopicid)
            val delete: TextView = popup.findViewById(R.id.deletetopicid)
            dialog.setContentView(popup)
            dialog.show()
            dialog.setCancelable(true)
            add.setOnClickListener {
                dialog.dismiss()
                val a = context as Activity
                val x = Intent(context, ThoughtDetails::class.java)
                x.putExtra("topic", temp)
                x.putExtra("add", "add thought")
                a.startActivity(x)
                a.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            edit.setOnClickListener {
                dialog.dismiss()

                val dialog2 = BottomSheetDialog(context, R.style.BottomSheetDialog)
                val popup1: View =
                    LayoutInflater.from(context).inflate(R.layout.edit_topic_layout, null)
                val updatedTopic: EditText = popup1.findViewById(R.id.topiceditid)
                val finish: ImageView = popup1.findViewById(R.id.submiteditid)
                updatedTopic.setText(temp.topic)
                finish.setOnClickListener {
                    val newTopic = updatedTopic.text.toString()
                    if (!TextUtils.isEmpty(newTopic)) {
                        if (temp.topic != newTopic) {
                            dialog2.dismiss()
                            temp.topic = newTopic
                            model.updateTopic(temp)
//                            Toast.makeText(context,"Topic updated !",Toast.LENGTH_LONG).show()
//                            val x1 = Intent(context, Transparent::class.java)
//                            x1.putExtra("topic", temp)
//                            x1.putExtra("main", "add thought")
//                            context.startActivity(x1)

                        } else {
                            Toast.makeText(context,"No change in Topic !",Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(context,"Topic is empty !",Toast.LENGTH_LONG).show()
                    }
                }
                dialog2.setContentView(popup1)
                dialog2.show()
                dialog2.setCancelable(true)
            }

            delete.setOnClickListener {
                dialog.dismiss()
                val builder = AlertDialog.Builder(context)
                builder.setPositiveButton("Yes"){_,_->
                    model.deleteTopic(temp)
//                    val x = Intent(context, Transparent::class.java)
//                    x.putExtra("topic", temp)
//                    x.putExtra("main", "add thought")
//                    context.startActivity(x)
//                    Toast.makeText(context,"Topic deleted !",Toast.LENGTH_LONG).show()
                }
                builder.setNegativeButton("No"){_,_->

                }
                builder.setTitle("Delete ${temp.topic} for sure?")
                builder.setMessage("saved thoughts of the topic would also be deleted")
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
                val btnOk: Button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                val btnCancel: Button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)

                btnOk.setTextColor(ContextCompat.getColor(context, R.color.black))
                btnCancel.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            true
        })

    }


    override fun getItemCount()=topics.size

    class RecylerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topic:TextView=itemView.findViewById(R.id.topicitemid)
        var item:LinearLayout=itemView.findViewById(R.id.cardItemId)

    }
}