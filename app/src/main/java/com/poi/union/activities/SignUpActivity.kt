package com.poi.union.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.poi.union.MainActivity
import com.poi.union.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val btn_login = findViewById<TextView>(R.id.textoLogIn)
        val btn_create = findViewById<Button>(R.id.botonSignIn)

        btn_create.setOnClickListener(){
            val myIntent =  Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        }

        btn_login.setOnClickListener(){
            val myIntent =  Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        }

    }
}