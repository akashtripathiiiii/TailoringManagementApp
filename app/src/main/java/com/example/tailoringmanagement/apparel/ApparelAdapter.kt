package com.example.tailoringmanagement.apparel

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tailoringmanagement.R

class ApparelAdapter(private val context: Activity, private val arrayList: ArrayList<Apparel>):
    ArrayAdapter<Apparel> (context, R.layout.apparel_item, arrayList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.apparel_item, null)

        val apparelImage = view.findViewById<ImageView>(R.id.apparelImage)
        val apparelName = view.findViewById<TextView>(R.id.tvApparelName)

        apparelImage.setImageResource(arrayList[position].imageId)
        apparelName.text = arrayList[position].name

        return view
    }

}