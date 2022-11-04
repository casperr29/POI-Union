package com.poi.union.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.poi.union.MainActivity
import com.poi.union.R
import com.poi.union.databinding.ActivityLoginBinding
import com.poi.union.models.Constantes
import com.poi.union.models.Users
import com.poi.union.models.PreferenceManager

class LoginActivity : AppCompatActivity() {

    companion object {
        lateinit var contextGlobal: Context
    }

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

    fun validation():Boolean {

        val preferenceManager = PreferenceManager(applicationContext)
        contextGlobal = applicationContext
        val email = findViewById<EditText>(R.id.inputEmail).text.toString()
        val password = findViewById<EditText>(R.id.inputContrasena).text.toString()

        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isNullOrEmpty()) {
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        val userTemp = userList.find { it.Email.equals(email) }
        val emailTemp = userTemp?.Email
        val passTemp = userTemp?.Contrasena


        /*al hacer isEmpty() no se si no puede leer o algo falla ya que cierra la app, entonces estas
        validaciones estan en proceso
        if (emailTemp!!.isEmpty()) {
            Toast.makeText(this, "No existe esta cuenta", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passTemp!!.isEmpty()) {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            return false
        }*/
        if (passTemp != password) {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            return false
        }
        preferenceManager.putString(Constantes.KEY_NAME, userTemp.Nombre)
        preferenceManager.putString(Constantes.KEY_IMAGE, userTemp.Foto)
        preferenceManager.putString(Constantes.KEY_EMAIL, userTemp.Email)

        return true
    }
}