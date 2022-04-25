package com.senlasy.dhammapada.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.senlasy.dhammapada.R
import com.senlasy.dhammapada.utility.Typewriter
import java.util.*

class SplashActivity : AppCompatActivity(), Typewriter.onFinishListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
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
