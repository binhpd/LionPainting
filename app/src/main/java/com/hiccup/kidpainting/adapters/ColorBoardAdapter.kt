package com.hiccup.kidpainting.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.models.ColorModel
import java.util.*

class ColorBoardAdapter(var context: Context, val colorList: ArrayList<ColorModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<ColorBoardAdapter.ColorHolder>() {
    lateinit var listener: ColorBoardListener
    var selectedItem: Int = -1

    init {
        selectedItem = colorList.size-1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_color, null)
        return ColorHolder(view)
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        if (holder == null) {
            return
        }

        holder.ivColor?.setBackgroundColor(colorList[position].color)
        holder.itemView.setOnClickListener { v ->
            listener.onSelectColorItem(v, colorList[position].color)
            if (selectedItem != position) {
                colorList[selectedItem].isSelected = false
                notifyItemChanged(selectedItem)

                selectedItem = position
                colorList[selectedItem].isSelected = true
                notifyItemChanged(selectedItem)
            }
        }

        if (colorList[position].isSelected) {
            holder.ivTick?.visibility = View.VISIBLE
        } else{
            holder.ivTick?.visibility = View.GONE
        }
    }

    fun setSelectedIndex(selectedIndex: Int) {
        selectedItem = selectedIndex
        colorList[selectedIndex].isSelected = true
    }

    class ColorHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var llBackgroundColor: RelativeLayout? = null
        var ivColor: ImageView? = null

        var ivTick: ImageView? = null

        init {
            llBackgroundColor = itemView.findViewById(R.id.llBackgroundColor)
            ivColor = itemView.findViewById(R.id.ivColor)
            ivTick = itemView.findViewById(R.id.ivTick)
        }
    }
}
