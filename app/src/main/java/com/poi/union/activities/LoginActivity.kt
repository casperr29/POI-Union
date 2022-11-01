package com.poi.union.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.poi.union.MainActivity
import com.poi.union.R
import com.poi.union.databinding.ActivityLoginBinding
import com.poi.union.models.Constantes
import com.poi.union.models.Users

class LoginActivity : AppCompatActivity() {
    private val userList = mutableListOf<Users>()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        getUsers()

        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btn_login = findViewById<Button>(R.id.botonLogin)
        val btn_reg = findViewById<TextView>(R.id.textoCrearCuenta)

        btn_reg.setOnClickListener(){
            val myIntent =  Intent(this, SignUpActivity::class.java)
            startActivity(myIntent)
            finish()
        }

        btn_login.setOnClickListener(){
            if(validation()){


                val myIntent =  Intent(this, MainActivity::class.java)
                startActivity(myIntent)
                finish()
            }

        }

    }

    private fun init(){
        this.database = FirebaseDatabase.getInstance()
        this.userref = database.getReference(Constantes.KEY_COLLECTION_USERS)
    }

    private fun getUsers(){
        userref.addValueEventListener(ValueEventListener)
    }

    private val ValueEventListener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for(snap in snapshot.children){
                val userMap: HashMap<String, Any>  = snap.value as HashMap<String, Any>

                val user = Users()

                user.Nombre = userMap[Constantes.KEY_NAME].toString()
                user.Email = userMap[Constantes.KEY_EMAIL].toString()
                user.Contrasena = userMap[Constantes.KEY_PASSWORD].toString()
                user.Carrera = userMap[Constantes.KEY_CARRERA].toString()
                user.Foto = userMap[Constantes.KEY_ROL].toString()
                user.Rol = userMap[Constantes.KEY_ROL].toString()

                userList.add(user)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText( this@LoginActivity, error.toString(), Toast.LENGTH_SHORT).show()

        }
    }

    fun validation():Boolean{

        val email = binding.inputEmail.text
        val password = binding.inputContrasena.text


        if(email.isNullOrEmpty()){
            Toast.makeText( this,"Ingrese su correo", Toast.LENGTH_SHORT).show()
            return false
        }

        if(password.isNullOrEmpty()){
            Toast.makeText( this,"Ingrese contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        var userTemp = userList.find { it.Email.equals(email)}
        var userTemp1 =  userList.find { it.Contrasena.equals(password)}// && it.Contrasena.equals(password)

        //si entra, pero no hace diferencia entre todos los datos de la base de datos
        if(TextUtils.isEmpty(userTemp.toString()) && TextUtils.isEmpty(userTemp1.toString())){
            Toast.makeText( this,"No existe esta cuenta", Toast.LENGTH_SHORT).show()
            return false
        }/*else if(!userTemp.Contrasena.equals(password)){
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
        }*/

        return true
    }
}