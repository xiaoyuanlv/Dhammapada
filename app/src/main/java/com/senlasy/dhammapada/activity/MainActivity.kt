package com.senlasy.dhammapada.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.adapter.CategoryAdapter
import com.senlasy.dhammapada.adapter.GridAutoFitLayoutManager
import com.senlasy.dhammapada.adapter.ItemPagerAdapter
import com.senlasy.dhammapada.database.DBHelper
import com.senlasy.dhammapada.fragment.ItemFrag
import com.senlasy.dhammapada.model.Category
import com.senlasy.dhammapada.model.Dhamma
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var rcyList  : RecyclerView
    lateinit var imgbtnFav  : ImageButton
    var lstCategory : List<Category> = ArrayList()
    var adapter  : CategoryAdapter? = null
    var doubleBackToExitPressedOnce : Boolean = false

    lateinit var btnLangEn : Button
    lateinit var btnLangMM : Button
    lateinit var btnLangPali : Button
    lateinit var btnLangePaliRoman : Button

    lateinit var imgbtnInfo : ImageButton

    var is_en = true
    var is_mm = true
    var is_pali = true
    var is_pali_roman = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rcyList = findViewById(R.id.rcyList)
        rcyList.layoutManager = GridAutoFitLayoutManager(this, 500, RecyclerView.VERTICAL, false)
        imgbtnFav = findViewById(R.id.imgbtnFav)

        imgbtnInfo = findViewById(R.id.imgbtnInfo)

        btnLangEn = findViewById(R.id.btnLangEn)
        btnLangMM = findViewById(R.id.btnLangMM)
        btnLangPali = findViewById(R.id.btnLangPali)
        btnLangePaliRoman = findViewById(R.id.btnLangPaliRoman)

        imgbtnInfo.setOnClickListener {
            try {

                val viewGroup: ViewGroup = findViewById(android.R.id.content)
                val dialogView: View = LayoutInflater.from(this).inflate(R.layout.app_info_dialog, viewGroup, false)

                val btnClose = dialogView.findViewById<Button>(R.id.btnClose)

                val alertDialog = AlertDialog.Builder(this).setView(dialogView).create()

                btnClose.setOnClickListener {
                    alertDialog.dismiss()
                }

                alertDialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
                alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                alertDialog.show()

            }catch (e : Exception){
                Log.e("MainActivity",e.message.toString())
            }

        }

        btnLangEn.setOnClickListener {
            is_en = !is_en
            if(is_en || is_mm || is_pali || is_pali_roman){
                setButtonLang(is_en, is_mm, is_pali, is_pali_roman)
            } else {
                is_en = true
                Toast.makeText(this, "At least one language is enabled!", Toast.LENGTH_SHORT).show()
            }
        }

        btnLangMM.setOnClickListener {
            is_mm = !is_mm
            if(is_en || is_mm || is_pali || is_pali_roman){
                setButtonLang(is_en, is_mm, is_pali, is_pali_roman)
            } else {
                is_mm = true
                Toast.makeText(this, "At least one language is enabled!", Toast.LENGTH_SHORT).show()
            }
        }

        btnLangPali.setOnClickListener {
            is_pali = !is_pali
            if(is_en || is_mm || is_pali || is_pali_roman){
                setButtonLang(is_en, is_mm, is_pali, is_pali_roman)
            } else {
                is_pali = true
                Toast.makeText( this, "At least one language is enabled!", Toast.LENGTH_SHORT).show()
            }
        }

        btnLangePaliRoman.setOnClickListener {
            is_pali_roman = !is_pali_roman
            if(is_en || is_mm || is_pali || is_pali_roman){
                setButtonLang(is_en, is_mm, is_pali, is_pali_roman)
            } else {
                is_pali_roman = true
                Toast.makeText( this, "At least one language is enabled!", Toast.LENGTH_SHORT).show()
            }
        }

        imgbtnFav.setOnClickListener {
            finish()
            startActivity(Intent(this, FavActivity::class.java))
        }

        setButtonLang(is_en, is_mm, is_pali, is_pali_roman)
    }

    fun setButtonLang(en_lang : Boolean, mm_lang : Boolean, pali_lang : Boolean, pali_roman_lang : Boolean){

        if(adapter != null){
            adapter!!.setLangVisibility(en_lang, mm_lang, pali_lang, pali_roman_lang)
            adapter!!.notifyDataSetChanged()
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

        if(pali_roman_lang){

            btnLangePaliRoman.setBackgroundResource(R.drawable.rounded_white_button)
            btnLangePaliRoman.setTextColor(Color.BLACK)
        } else {

            btnLangePaliRoman.setBackgroundResource(R.drawable.rounded_dark_button)
            btnLangePaliRoman.setTextColor(Color.WHITE)
        }


    }

    override fun onResume() {
        super.onResume()
        getData(this)
    }


    fun getData(context: Context){
        var dbHelper = DBHelper(this)
        lstCategory = dbHelper.getAllCategory()
        if (lstCategory.isNotEmpty()){
            adapter = CategoryAdapter(lstCategory.toMutableList(), R.layout.item_category, this, rcyList)
            adapter!!.setOnItemListener(object: CategoryAdapter.OnItemClickListener {
                override fun onItemClick(item: Category, v : View) {
                    Handler().postDelayed(Runnable {

                        v.setBackgroundColor(context.resources.getColor(android.R.color.white,null))
                        finish()
                        val intent = Intent(context, ItemActivity::class.java)
                        intent.putExtra("categoryid", item.id)
                        intent.putExtra("category", item.title)
                        startActivity(intent)

                    }, 1000)



                }
            })
            rcyList.adapter = adapter
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        Handler().postDelayed(Runnable() {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }


}
