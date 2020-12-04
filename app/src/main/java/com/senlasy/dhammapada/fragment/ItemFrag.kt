package com.senlasy.dhammapada.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.model.Dhamma
import info.androidhive.fontawesome.FontTextView

private const val ARG_ITEM = "dhamma"
private const val ARG_EN_LANG = "EN_LANG"
private const val ARG_MM_LANG = "MM_LANG"
private const val ARG_PALI_LANG = "PALI_LANG"
private const val ARG_PALI_ROMAN_LANG = "PALI_ROMAN_LANG"

class ItemFrag : Fragment() {
    private var dhamma: Dhamma? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var rlDhamma: RelativeLayout
    lateinit var txtTitle: TextView
    lateinit var cardDhamma : CardView
    lateinit var imgbtnFav : ImageButton
    lateinit var ftvFav : FontTextView
    lateinit var btnNum : Button

    private var is_mm_visible = true
    private var is_en_visible = true
    private var is_pali_visible = true
    private var is_pali_roman_visible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dhamma = it.getParcelable(ARG_ITEM)
            is_mm_visible = it.getBoolean(ARG_MM_LANG)
            is_en_visible = it.getBoolean(ARG_EN_LANG)
            is_pali_visible = it.getBoolean(ARG_PALI_LANG)
            is_pali_roman_visible = it.getBoolean(ARG_PALI_ROMAN_LANG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v = inflater.inflate(R.layout.fragment_item, container, false)

        cardDhamma = v.findViewById(R.id.cardDhamma)
        rlDhamma = v.findViewById(R.id.rlDhamma)
        txtTitle = v.findViewById(R.id.txtTitle)
        txtTitle.movementMethod = ScrollingMovementMethod()
        imgbtnFav = v.findViewById(R.id.imgbtnFav)
        ftvFav = v.findViewById(R.id.ftvFav)
        btnNum = v.findViewById(R.id.btnNum)

        if(dhamma!!.fav == 1){
            ftvFav.setTextColor(activity!!.resources.getColor(R.color.colorAccent,null))
        } else {
            ftvFav.setTextColor(activity!!.resources.getColor(android.R.color.white,null))
        }

        if(is_en_visible){
            txtTitle.text = dhamma!!.message
        } else if(is_mm_visible){
            txtTitle.text = dhamma!!.mm_message
        } else if(is_pali_visible){
            txtTitle.text = dhamma!!.pali_message
        } else if(is_pali_roman_visible) {
            txtTitle.text = dhamma!!.paliroman
        } else {
            txtTitle.text = dhamma!!.message
        }

        btnNum.text = dhamma!!.id.toString()

        imgbtnFav.setOnClickListener {
            listener?.onFavBtnClick(dhamma!!, ftvFav)
        }

        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onFavBtnClick(item: Dhamma, view : FontTextView)
    }

    companion object {
        @JvmStatic
        fun newInstance(dhamma: Dhamma,  is_en_visible : Boolean, is_mm_visible : Boolean, is_pali_visible : Boolean, is_pali_roman : Boolean) =
            ItemFrag().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ITEM, dhamma)
                    putBoolean(ARG_EN_LANG, is_en_visible)
                    putBoolean(ARG_MM_LANG, is_mm_visible)
                    putBoolean(ARG_PALI_LANG, is_pali_visible)
                    putBoolean(ARG_PALI_ROMAN_LANG , is_pali_roman)
                }
            }
    }
}
