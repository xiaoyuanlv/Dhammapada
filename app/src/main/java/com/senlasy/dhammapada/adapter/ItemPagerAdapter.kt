package com.senlasy.dhammapada.adapter

import androidx.fragment.app.*
import androidx.viewpager2.adapter.StatefulAdapter
import com.senlasy.dhammapada.fragment.ItemFrag
import com.senlasy.dhammapada.model.Dhamma

class ItemPagerAdapter(var lstDhamma: MutableList<Dhamma>, var context : FragmentActivity) : androidx.viewpager2.adapter.FragmentStateAdapter(context), StatefulAdapter {

    var isEn : Boolean = true
    var isMM : Boolean = false
    var isPali : Boolean = false
    var isPaliRoman  : Boolean = false

    fun setLangVisibility(is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean, is_pali_roman : Boolean){
        this.isEn = is_en_visible
        this.isMM = is_mm_visible
        this.isPali = is_pali_visible
        this.isPaliRoman = is_pali_roman
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return lstDhamma.count()
    }

    override fun createFragment(position: Int): Fragment {
        return ItemFrag.newInstance(lstDhamma[position], isEn, isMM, isPali, isPaliRoman)
    }




}