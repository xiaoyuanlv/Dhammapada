package com.senlasy.dhammapada.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.adapter.ItemAdapter
import com.senlasy.dhammapada.adapter.ItemPagerAdapter
import com.senlasy.dhammapada.database.DBHelper
import com.senlasy.dhammapada.fragment.ItemFrag
import com.senlasy.dhammapada.model.Dhamma
import info.androidhive.fontawesome.FontTextView

class FavActivity : AppCompatActivity(),  ItemFrag.OnFragmentInteractionListener {

    lateinit var imgbtnLayout : ImageButton
    lateinit var ftvLayout : FontTextView
    lateinit var imgbtnMain : ImageButton
    lateinit var txtEmpty : TextView
    lateinit var vpgList : ViewPager

    var lstItem : List<Dhamma> = ArrayList()
    var isList = true

//    lateinit var rcyList : RecyclerView
//    var adapter  : ItemAdapter? = null

    var adapter : ItemPagerAdapter? = null

    lateinit var btnLangEn : Button
    lateinit var btnLangMM : Button
    lateinit var btnLangPali : Button
    lateinit var btnLangPaliRoman : Button

    lateinit var rlLangLayout : RelativeLayout

    var is_en = true
    var is_mm = false
    var is_pali = false
    var is_paliroman = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)

        imgbtnLayout = findViewById(R.id.imgbtnLayout)
        ftvLayout = findViewById(R.id.ftvLayout)
        rlLangLayout = findViewById(R.id.rlLangLayout)
//        rcyList = findViewById(R.id.rcyList)
        imgbtnMain = findViewById(R.id.imgbtnMain)
        txtEmpty = findViewById(R.id.txtEmpty)

        vpgList = findViewById(R.id.vpgList)

        imgbtnMain.setOnClickListener {
            backToMain()
        }

        imgbtnLayout.setOnClickListener {
            isList = !isList
            setUI()
        }

        btnLangEn = findViewById(R.id.btnLangEn)
        btnLangMM = findViewById(R.id.btnLangMM)
        btnLangPali = findViewById(R.id.btnLangPali)
        btnLangPaliRoman = findViewById(R.id.btnLangPaliRoman)

        btnLangEn.setOnClickListener {
            is_en = true
            is_pali = false
            is_mm = false
            is_paliroman = false
            setButtonLang(is_en, is_mm, is_pali, is_paliroman)
        }

        btnLangMM.setOnClickListener {
            is_mm = true
            is_en = false
            is_pali = false
            is_paliroman = false
            setButtonLang(is_en, is_mm, is_pali, is_paliroman)
        }

        btnLangPali.setOnClickListener {
            is_pali = true
            is_en = false
            is_mm = false
            is_paliroman = false
            setButtonLang(is_en, is_mm, is_pali, is_paliroman)
        }

        btnLangPaliRoman.setOnClickListener {
            is_pali = false
            is_en = false
            is_mm = false
            is_paliroman = true
            setButtonLang(is_en, is_mm, is_pali, is_paliroman)
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("is_mm")) {
            is_pali = savedInstanceState.getBoolean("is_pali")
            is_mm = savedInstanceState.getBoolean("is_mm")
            is_en = savedInstanceState.getBoolean("is_en")
            is_paliroman = savedInstanceState.getBoolean("is_paliroman")

            setButtonLang(is_en, is_mm, is_pali, is_paliroman)
        }

        getData()


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("is_mm", is_mm)
        outState.putBoolean("is_en", is_en)
        outState.putBoolean("is_pali", is_pali)
        outState.putBoolean("is_paliroman", is_paliroman)
    }

    fun setButtonLang(en_lang : Boolean, mm_lang : Boolean, pali_lang : Boolean, pali_roman : Boolean){

        if(adapter != null){
            adapter!!.setLangVisibility(en_lang, mm_lang, pali_lang, pali_roman)
            adapter!!.notifyDataSetChanged()
        }

        if(pali_roman){
            btnLangPaliRoman.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangPaliRoman.setTextColor(Color.BLACK)
        } else {
            btnLangPaliRoman.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangPaliRoman.setTextColor(Color.WHITE)
        }

        if(en_lang){
            btnLangEn.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangEn.setTextColor(Color.BLACK)
        } else {
            btnLangEn.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangEn.setTextColor(Color.WHITE)
        }

        if(mm_lang){
            btnLangMM.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangMM.setTextColor(Color.BLACK)
        } else {
            btnLangMM.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangMM.setTextColor(Color.WHITE)
        }

        if(pali_lang){
            btnLangPali.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangPali.setTextColor(Color.BLACK)
        } else {
            btnLangPali.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangPali.setTextColor(Color.WHITE)
        }

    }


    override fun onBackPressed() {
       // super.onBackPressed()
        backToMain()
    }

    fun backToMain() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }



    fun setUI(){

//        if(isList){
//            rcyList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//            ftvLayout.text = getString(R.string.fa_square_full_solid)
//            adapter = ItemAdapter(lstItem.toMutableList(), R.layout.item_list, this, rcyList)
//            adapter!!.setOnItemListener(this)
//            rcyList.adapter = adapter
//        } else {
//            rcyList.layoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)
//            ftvLayout.text = getString(R.string.fa_list_ul_solid)
//            adapter = ItemAdapter(lstItem.toMutableList(), R.layout.item_card, this, rcyList)
//            adapter!!.setOnItemListener(this)
//            rcyList.adapter = adapter
//        }

        adapter = ItemPagerAdapter(lstItem.toMutableList(), supportFragmentManager, this)
        vpgList.adapter = adapter

        if(adapter!!.DhammaList.size == 0){
            txtEmpty.visibility = View.VISIBLE
            rlLangLayout.visibility = View.GONE
        } else {
            txtEmpty.visibility = View.GONE
            rlLangLayout.visibility = View.VISIBLE
        }

        setButtonLang(is_en, is_mm, is_pali, is_paliroman)
        adapter!!.notifyDataSetChanged()

    }


    fun getData(){
        var dbHelper = DBHelper(this)
        lstItem = dbHelper.getFavList()
        setUI()
    }

    fun backtoMain() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onFavBtnClick(item: Dhamma, view: FontTextView) {
        val dbHelper = DBHelper(this)
        if(item.fav == 0){
            item.fav = 1
        } else {
            item.fav = 0
        }
        dbHelper.saveFav(item.id, item.fav)
        adapter!!.DhammaList.remove(item)
        adapter!!.notifyDataSetChanged()

        if(adapter!!.DhammaList.size == 0){
            txtEmpty.visibility = View.VISIBLE
            rlLangLayout.visibility = View.GONE
        } else {
            txtEmpty.visibility = View.GONE
            rlLangLayout.visibility = View.VISIBLE
        }
    }

//
//    override fun onItemClick(item: Dhamma, view: View) {
//        Handler().postDelayed(Runnable {
//            view.setBackgroundColor(resources.getColor(android.R.color.white, null))
//        }, 1000)
//        Toast.makeText(this, item.message, Toast.LENGTH_SHORT).show()
//    }

//    override fun onFavBtnClick(item: Dhamma, view: View) {
//
//        val dbHelper = DBHelper(this)
//        if(item.fav == 0){
//            item.fav = 1
//        } else {
//            item.fav = 0
//        }
//        dbHelper.saveFav(item.id, item.fav)
//        adapter!!.remove(item)
//        adapter!!.notifyDataSetChanged()
//
//        if(adapter!!.ItemList.size == 0){
//            txtEmpty.visibility = View.VISIBLE
//        } else {
//            txtEmpty.visibility = View.GONE
//        }
//    }



}
