package com.senlasy.dhammapada.adapter

import android.content.Context
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.model.Dhamma
import info.androidhive.fontawesome.FontTextView


class ItemAdapter(
    var ItemList: MutableList<Dhamma>,
    private var rowLayout: Int,
    private val context: Context,
    recyclerView: RecyclerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    // for load more
    private val VIEW_TYPE_ITEM = 0

    private var is_mm_visible = true
    private var is_en_visible = true
    private var is_pali_visible = true


    override fun getItemCount(): Int {
        if (ItemList.isEmpty()) return 0 else return ItemList.size
    }

    public interface OnItemClickListener {
        fun onItemClick(item: Dhamma, view : View)
        fun onFavBtnClick(item: Dhamma, view : View)
    }

    fun changeLayout(rowLayout : Int){
        this.rowLayout = rowLayout
    }

    fun addAll(item: List<Dhamma>) {
        ItemList.addAll(item)
        notifyDataSetChanged()
    }

    fun setData(lst: MutableList<Dhamma>){
        ItemList = lst
        notifyDataSetChanged()
    }

    fun remove(item: Dhamma) {
        val position = ItemList.indexOf(item)
        ItemList.removeAt(position)
        notifyItemRemoved(position)
    }

    public fun setLangVisibility(is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean){
        this.is_en_visible = is_en_visible
        this.is_mm_visible = is_mm_visible
        this.is_pali_visible = is_pali_visible
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        var vholder : RecyclerView.ViewHolder? = null
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(rowLayout, parent, false)
            vholder = DhammaViewHolder(view)
        }
        return vholder as RecyclerView.ViewHolder
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DhammaViewHolder) {

            holder.bind(ItemList[position], listener, context, is_en_visible, is_mm_visible, is_pali_visible)

        } else {
            val loadingViewHolder = holder as ViewHolderLoading
            loadingViewHolder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  VIEW_TYPE_ITEM
    }


    fun setOnItemListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    private inner class ViewHolderLoading(view: View) : RecyclerView.ViewHolder(view) {
        var progressBar: ProgressBar

        init {
            progressBar = view.findViewById<View>(R.id.itemProgressbar) as ProgressBar
        }
    }

    class DhammaViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        internal var rlDhamma: RelativeLayout
        internal var txtTitle: TextView
        internal var cardDhamma : CardView
        internal var imgbtnFav : ImageButton
        internal var ftvFav : FontTextView


        init {
            cardDhamma = v.findViewById(R.id.cardDhamma)
            rlDhamma = v.findViewById(R.id.rlDhamma)
            txtTitle = v.findViewById(R.id.txtTitle)
            txtTitle.movementMethod = ScrollingMovementMethod()
            imgbtnFav = v.findViewById(R.id.imgbtnFav)
            ftvFav = v.findViewById(R.id.ftvFav)
        }

        fun bind(item: Dhamma, listener: OnItemClickListener?, context: Context, is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean) {
            setData(item, context, is_en_visible, is_mm_visible, is_pali_visible)

            rlDhamma.setOnClickListener {
                rlDhamma.setBackgroundColor(context.resources.getColor( R.color.colorAccent, null))
                focusedItem = layoutPosition
                listener!!.onItemClick(item, it)
            }

            imgbtnFav.setOnClickListener {
                listener!!.onFavBtnClick(item, ftvFav)
            }
        }

        fun setData(item: Dhamma, context: Context, is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean){

            if(item.fav == 1){
                ftvFav.setTextColor(context.resources.getColor(R.color.colorAccent,null))
            } else {
                ftvFav.setTextColor(context.resources.getColor(android.R.color.white,null))
            }

            if(is_en_visible){
                txtTitle.text = item.message
            } else if(is_mm_visible){
                txtTitle.text = item.mm_message
            } else if(is_pali_visible){
                txtTitle.text = item.pali_message
            } else {
                txtTitle.text = item.message
            }
        }

    }

    companion object {
        private var focusedItem = -1
    }

}
