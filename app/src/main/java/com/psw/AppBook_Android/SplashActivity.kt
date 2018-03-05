package com.psw.AppBook_Android

import android.content.Intent
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /* SPLASH_DISPLAY_LENGTH 뒤에 메뉴 액티비티를 실행시키고 종료한다.*/
        Handler().postDelayed({
            /* 메뉴액티비티를 실행하고 로딩화면을 죽인다.*/
            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())


        val cs = findViewById(R.id.ctBack) as ConstraintLayout
        cs.setOnClickListener { finish() }


    }

}
