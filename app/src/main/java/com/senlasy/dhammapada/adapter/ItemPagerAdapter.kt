package com.senlasy.dhammapada.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.senlasy.dhammapada.fragment.ItemFrag
import com.senlasy.dhammapada.model.Dhamma

class ItemPagerAdapter(var DhammaList: MutableList<Dhamma>,fragmentManager : FragmentManager,var context : Context) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var is_en_visible : Boolean = true
    var is_mm_visible : Boolean = false
    var is_pali_visible : Boolean = false
    var is_pali_roman  : Boolean = false


    override fun getItem(position: Int): Fragment {
        return ItemFrag.newInstance(DhammaList[position], is_en_visible, is_mm_visible, is_pali_visible, is_pali_roman)
    }

    fun setLangVisibility(is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean, is_pali_roman : Boolean){
        this.is_en_visible = is_en_visible
        this.is_mm_visible = is_mm_visible
        this.is_pali_visible = is_pali_visible
        this.is_pali_roman = is_pali_roman
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return DhammaList.count()
    }


}