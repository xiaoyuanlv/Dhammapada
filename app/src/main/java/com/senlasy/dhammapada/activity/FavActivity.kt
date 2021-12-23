package com.senlasy.dhammapada.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.TextViewCompat
import androidx.viewpager.widget.ViewPager
import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.adapter.ItemPagerAdapter
import com.senlasy.dhammapada.database.DBHelper
import com.senlasy.dhammapada.fragment.ItemFrag
import com.senlasy.dhammapada.model.Dhamma

class FavActivity : AppCompatActivity(),  ItemFrag.OnFragmentInteractionListener {

    private lateinit var imgbtnLayout : ImageButton
    private lateinit var imgbtnMain : ImageButton
    private lateinit var txtEmpty : TextView
    private lateinit var vpgList : ViewPager

    private var lstItem : List<Dhamma> = ArrayList()
    private var isList = true

    private var adapter : ItemPagerAdapter? = null

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
        rlLangLayout = findViewById(R.id.rlLangLayout)
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



    private fun setUI(){

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


    private fun getData(){
        val dbHelper = DBHelper(this)
        lstItem = dbHelper.getFavList()
        setUI()
    }

    fun backtoMain() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }


    override fun onFavBtnClick(item: Dhamma, view: TextView) {
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


}
