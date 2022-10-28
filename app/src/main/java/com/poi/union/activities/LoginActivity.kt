package com.poi.union.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.poi.union.MainActivity
import com.poi.union.R

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_login = findViewById<Button>(R.id.botonLogin)
        val btn_reg = findViewById<TextView>(R.id.textoCrearCuenta)

        btn_reg.setOnClickListener(){
            val myIntent =  Intent(this, SignUpActivity::class.java)
            startActivity(myIntent)
            finish()
        }

        btn_login.setOnClickListener(){
            val myIntent =  Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        }

    }
}