package com.senlasy.dhammapada.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.adapter.CategoryAdapter
import com.senlasy.dhammapada.adapter.ItemAdapter
import com.senlasy.dhammapada.adapter.ItemPagerAdapter
import com.senlasy.dhammapada.database.DBHelper
import com.senlasy.dhammapada.fragment.ItemFrag
import com.senlasy.dhammapada.model.Category
import com.senlasy.dhammapada.model.Dhamma
import info.androidhive.fontawesome.FontTextView
import org.w3c.dom.Text

class ItemActivity : AppCompatActivity(), ItemFrag.OnFragmentInteractionListener{


    lateinit var imgbtnLayout : ImageButton
    lateinit var ftvLayout : FontTextView
//    lateinit var rcyList : RecyclerView
    lateinit var vpgList : ViewPager
    lateinit var imgbtnFav  : ImageButton
    lateinit var txtMenuTitle : TextView
    lateinit var txtMark : TextView
    var lstItem : List<Dhamma> = ArrayList()
//    var adapter  : ItemAdapter? = null
    var adapter : ItemPagerAdapter? = null

    var categoryid : Int = 0
    var isList = false
    var category : Category? = null

    lateinit var btnLangEn : Button
    lateinit var btnLangMM : Button
    lateinit var btnLangPali : Button
    lateinit var btnLangPaliRoman : Button

    var is_en = true
    var is_mm = false
    var is_pali = false
    var is_paliroman = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        imgbtnLayout = findViewById(R.id.imgbtnLayout)
        ftvLayout = findViewById(R.id.ftvLayout)
//        rcyList = findViewById(R.id.rcyList)
        vpgList = findViewById(R.id.vpgList)
        imgbtnFav = findViewById(R.id.imgbtnFav)
        txtMenuTitle = findViewById(R.id.txtMenuTitle)

        txtMark = findViewById(R.id.txtMark)

        findViewById<ImageButton>(R.id.imgbtnBack).setOnClickListener {
            backtoMain()
        }

        imgbtnFav.setOnClickListener {
            finish()
            startActivity(Intent(this, FavActivity::class.java))
        }

        imgbtnLayout.setOnClickListener {
            isList = !isList
            setUI()
        }

        getCategoryId()

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
        }


        setButtonLang(is_en, is_mm, is_pali, is_paliroman)
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

        txtMark.text = category?.markNumber.toString()

        if(pali_roman){
            if(category != null){
                txtMenuTitle.text  = category?.paliroman.toString()
            }
            btnLangPaliRoman.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangPaliRoman.setTextColor(Color.BLACK)
        } else {
            btnLangPaliRoman.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangPaliRoman.setTextColor(Color.WHITE)
        }

        if(en_lang){
            if(category != null){
                txtMenuTitle.text  = category?.title.toString()
            }
            btnLangEn.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangEn.setTextColor(Color.BLACK)
        } else {
            btnLangEn.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangEn.setTextColor(Color.WHITE)
        }

        if(mm_lang){
            if(category != null){
                txtMenuTitle.text  = category?.mmtitle.toString()
            }
            btnLangMM.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangMM.setTextColor(Color.BLACK)
        } else {
            btnLangMM.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangMM.setTextColor(Color.WHITE)
        }

        if(pali_lang){
            if(category != null){
                txtMenuTitle.text  = category?.palititle.toString()
            }
            btnLangPali.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangPali.setTextColor(Color.BLACK)
        } else {
            btnLangPali.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangPali.setTextColor(Color.WHITE)
        }

    }

    fun setUI(){

//        if(isList){
//            rcyList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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

//        adapter!!.notifyDataSetChanged()


        adapter = ItemPagerAdapter(lstItem.toMutableList(), supportFragmentManager, this)
        vpgList.adapter = adapter


    }

    fun getCategoryId(){
        if(intent.hasExtra("categoryid") && intent.hasExtra("category")){
            categoryid = intent.getIntExtra("categoryid", 0)
            txtMenuTitle.text  = intent.getStringExtra("category")
        }

        if(categoryid > 0){
            getData()
        } else {
            backtoMain()
        }
    }

    private fun backtoMain(){
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun getData(){
        val dbHelper = DBHelper(this)
        category = dbHelper.getCategory(categoryid)[0]
        lstItem = dbHelper.getAllDhammaByCategory(categoryid)
        if(lstItem.isNotEmpty()){
            setUI()
        } else {
            backtoMain()
        }
    }

//    override fun onItemClick(item: Dhamma, view: View) {
//        Handler().postDelayed(Runnable {
//            view.setBackgroundColor(resources.getColor(android.R.color.white, null))
//        }, 1000)
//        Toast.makeText(this, item.message, Toast.LENGTH_SHORT).show()
//    }


    override fun onBackPressed() {
       // super.onBackPressed()
       backtoMain()
    }

    override fun onFavBtnClick(item: Dhamma, view: FontTextView) {
        try {

            val dbHelper = DBHelper(this)
            if (item.fav == 0) {
                item.fav = 1
            } else {
                item.fav = 0
            }

            if (item.fav == 1) {
                view.setTextColor(this.resources.getColor(R.color.colorAccent, null))
            } else {
                view.setTextColor(this.resources.getColor(android.R.color.white, null))
            }


            dbHelper.saveFav(item.id, item.fav)
            adapter!!.notifyDataSetChanged()

        }catch (ex : Exception){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}

