/*
basis of code based on code from:
https://tutorials.eu/android-sqlite-database-in-depth-guide/#ftoc-heading-22
modified by Jonathon Zempel
 */

package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//allows for individual items to be dynamically placed together in the recyclerview, and be clickable
class ItemAdapter(val context: Context, val items: ArrayList<ItemModelClass>):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    private lateinit var click: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    //creates the clicky thing
    fun setOnItemClickListener(clickListener: onItemClickListener){
        click = clickListener
    }
    //connects the contact_view layout, and the clicky thing into one holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.compact_view, parent, false)
        return ViewHolder(itemView, click)
    }

     //attaches each items info within specified compact_view layout
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.name.text = item.name
        holder.description.text = item.description
        holder.price.text = "$" + item.price
        holder.pic.setImageBitmap(item.pic)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    //the new interactable layout - with all the info for specific tile
    class ViewHolder(view: View,clickListener: onItemClickListener): RecyclerView.ViewHolder(view) {//clicklistener new
        //all the  values that are in compact_view layout within the recyclerview
        val name = view.findViewById<TextView>(R.id.name)
        val description = view.findViewById<TextView>(R.id.description)
        val price = view.findViewById<TextView>(R.id.price)
        var pic = view.findViewById<ImageView>(R.id.image)
        //actual clicky part of this whole thing
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }


}