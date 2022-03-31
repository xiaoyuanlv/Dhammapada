package com.senlasy.dhammapada.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.model.Category

class CategoryAdapter(
    var CategoryList: MutableList<Category>,
    private val rowLayout: Int,
    private val context: Context,
    recyclerView: RecyclerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    // for load more
    private val VIEW_TYPE_ITEM = 0

    private var is_mm_visible = true
    private var is_en_visible = true
    private var is_pali_visible = true
    private var is_pali_roman_visible = true

    override fun getItemCount(): Int {
        if (CategoryList.isEmpty()) return 0 else return CategoryList.size
    }

    public interface OnItemClickListener {
        fun onItemClick(item: Category, view : View)
    }

    public fun setLangVisibility(is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean, is_pali_roman_visible: Boolean){
        this.is_en_visible = is_en_visible
        this.is_mm_visible = is_mm_visible
        this.is_pali_visible = is_pali_visible
        this.is_pali_roman_visible = is_pali_roman_visible
    }

    fun addAll(item: List<Category>) {
        CategoryList.addAll(item)
    }

    fun setData(lst: MutableList<Category>){
        CategoryList = lst
    }

    fun remove(item: Category) {
        val position = CategoryList.indexOf(item)
        CategoryList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        var vholder : RecyclerView.ViewHolder? = null
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(rowLayout, parent, false)
            vholder = CategoryViewHolder(view)
        }
        return vholder as RecyclerView.ViewHolder
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {

            holder.bind(CategoryList[position], listener, context, is_en_visible, is_mm_visible, is_pali_visible, is_pali_roman_visible)

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

    class CategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        internal var rlCategory: LinearLayout
        internal var title: TextView
        internal var txtMMTitle: TextView
        internal var txtPaliTitle: TextView
        internal var txtPaliRomanTitle : TextView
        internal var cardCategory : CardView
        internal var btnNum : Button
        internal var txtMark : TextView


        init {
            cardCategory = v.findViewById(R.id.cardCategory)
            rlCategory = v.findViewById(R.id.rlCategory)
            title = v.findViewById(R.id.txtTitle)
            txtMMTitle = v.findViewById(R.id.txtMMTitle)
            txtPaliTitle = v.findViewById(R.id.txtPaliTitle)
            txtPaliRomanTitle = v.findViewById(R.id.txtPaliRomanTitle)
            btnNum = v.findViewById(R.id.btnNum)
            txtMark = v.findViewById(R.id.txtMark)
        }

        fun bind(item: Category, listener: OnItemClickListener?, context: Context, is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean, is_pali_roman_visible : Boolean) {
            setData(item, is_en_visible, is_mm_visible, is_pali_visible, is_pali_roman_visible)

            rlCategory.setOnClickListener {
                rlCategory.setBackgroundColor(Color.parseColor("#FFDE03"));
//                rlCategory.setBackgroundColor(context.resources.getColor(R.color.colorAccent, ))
                focusedItem = layoutPosition
                listener!!.onItemClick(item, it)
            }
        }

        fun setData(item: Category, is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean, is_pali_roman_visible : Boolean) {
            btnNum.text = item.id.toString()

            if (is_en_visible) {
                title.visibility = View.VISIBLE
            } else {
                title.visibility = View.GONE
            }

            if (is_mm_visible) {
                txtMMTitle.visibility = View.VISIBLE
            } else {
                txtMMTitle.visibility = View.GONE
            }

            if (is_pali_visible) {
                txtPaliTitle.visibility = View.VISIBLE
            } else {
                txtPaliTitle.visibility = View.GONE
            }

            if (is_pali_roman_visible) {
                txtPaliRomanTitle.visibility = View.VISIBLE
            } else {
                txtPaliRomanTitle.visibility = View.GONE
            }

            txtMark.text = item.markNumber

            title.text = item.title
            txtMMTitle.text = item.mmtitle
            txtPaliTitle.text = item.palititle
            txtPaliRomanTitle.text = item.paliroman
        }

    }

    companion object {
        private var focusedItem = -1
    }


}
