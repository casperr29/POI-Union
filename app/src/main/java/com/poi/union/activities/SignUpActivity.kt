package com.poi.union.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.poi.union.MainActivity
import com.poi.union.R
import com.poi.union.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    val materias= arrayOf("LMAD", "LCC", "LF", "LM", "LA", "LSTI")
    val rol= arrayOf("Alumno", "Maestro")

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        val btn_login = findViewById<TextView>(R.id.textoLogIn)

        binding.botonSignIn.setOnClickListener {
            val nombre= binding.inputNombreCompleto.text.toString()
            val rol=binding.registerSpinnerRol.id.toString()
            val email=binding.inputEmail.text.toString()
            val carrera=binding.registerSpinnerCarreras.id.toString()
            val contrasena=binding.inputContrasena.text.toString()
        }




        btn_login.setOnClickListener(){
            val myIntent =  Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        }

        val spinner=findViewById<Spinner>(R.id.register_spinner_carreras)
        val arrayAdapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,materias)
        spinner.adapter=arrayAdapter
        spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(applicationContext,"Seleccionó la carrera " + materias[position],Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        val spinner2=findViewById<Spinner>(R.id.register_spinner_rol)
        val arrayAdapter2=ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,rol)
        spinner2.adapter=arrayAdapter2
        spinner2.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(applicationContext,"Seleccionó el rol de " + rol[position],Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


    }

    fun getUserObj():User{
        
    }


    fun validation():Boolean{
        var isValid=true

        if(binding.inputNombreCompleto.text.isNullOrEmpty()){
            isValid=false
            Toast.makeText( SignUpActivity.this,"Ingrese su nombre", Toast.LENGTH_SHORT).show()
        }

        if(binding.inputEmail.text.isNullOrEmpty()){
            isValid=false
            Toast.makeText( SignUpActivity.this,"Ingrese su correo", Toast.LENGTH_SHORT).show()
        }

        if(binding.inputContrasena.text.isNullOrEmpty()){
            isValid=false
            Toast.makeText( SignUpActivity.this,"Ingrese contraseña", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }

}