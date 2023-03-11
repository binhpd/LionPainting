package com.hiccup.kidpainting.adapters

import android.view.View
import android.widget.ImageView
import com.hiccup.kidpainting.R

class PhotoHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    var ivContent: ImageView? = itemView.findViewById(R.id.ivContent)
    var ivLock: ImageView? = itemView.findViewById(R.id.ivLock)
}
