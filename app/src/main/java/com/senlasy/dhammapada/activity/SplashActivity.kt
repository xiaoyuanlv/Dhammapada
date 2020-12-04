package com.senlasy.dhammapada.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.utility.Typewriter

class SplashActivity : AppCompatActivity(), Typewriter.onFinishListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val twSubTitle = findViewById<Typewriter>(R.id.twSubTitle)
//        twSubTitle.animateText(getString(R.string.app_subtitle))
//        twSubTitle.setAnimationFinishListener(this)

        Handler().postDelayed({
           startMainActivity()
        }, 3000)

    }

    override fun onFinishListener(view: TextView, text: String, type: String?) {
        startMainActivity()
    }

    private fun startMainActivity() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

}
