package com.hiccup.kidpainting.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.models.CollectionItem
import com.hiccup.kidpainting.utilities.AppHelper
import com.hiccup.kidpainting.view.KidTextView


class CollectionAdapter
(var context: Context, var list: ArrayList<CollectionItem>) : androidx.recyclerview.widget.RecyclerView.Adapter<CollectionAdapter.CollectionHolder>() {
    lateinit var listener: OnSelectListener

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CollectionHolder =
            CollectionHolder(LayoutInflater.from(context).inflate(R.layout.item_collection, null))

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        val collectionItem = list[position]
        holder.bindView(context, holder, collectionItem, position, listener)
    }

    class CollectionHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val ivName: ImageView = itemView.findViewById(R.id.ivName)
        private val ivLogo: ImageView
        private val ivLock: ImageView
        private val tvName: KidTextView
        private val ivNew: ImageView

        init {
            ivLogo = itemView.findViewById(R.id.ivLogo)
            ivLock = itemView.findViewById(R.id.ivLock)
            tvName = itemView.findViewById(R.id.tvName)
            ivNew = itemView.findViewById(R.id.ivNew)
        }

        fun bindView(context: Context, holder: CollectionHolder, collectionItem: CollectionItem, position: Int, listener: OnSelectListener) {
            ivName.visibility = View.VISIBLE
            tvName.visibility = View.GONE
            val logoName = if (AppHelper.isVietNamLanguage()) collectionItem.logoName else collectionItem.logoName + "_en"
            Glide.with(context).load(getDrawableResourceFromName(context, logoName))
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(ivName)
            Glide.with(context).load(getDrawableResourceFromName(context, collectionItem.logoIcon))
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(ivLogo)


            holder.ivLock.visibility = if (collectionItem.isEnable) View.GONE else View.VISIBLE

            holder.ivNew.visibility = if (collectionItem.isNew) View.VISIBLE else View.GONE

            holder.itemView.setOnClickListener { listener.onSelectItem(it, position) }
        }

        private fun getDrawableResourceFromName(context: Context, drawableName: String): Int {
            return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
        }
    }


}

