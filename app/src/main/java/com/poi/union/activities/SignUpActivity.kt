package com.poi.union.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makeramen.roundedimageview.RoundedImageView
import com.poi.union.MainActivity
import com.poi.union.R
import com.poi.union.models.Users
import com.poi.union.models.Constantes
import com.poi.union.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import kotlin.collections.HashMap

class SignUpActivity : AppCompatActivity() {

    private var encodedImage = ""
    val materias= arrayOf("LMAD", "LCC", "LF", "LM", "LA", "LSTI")
    val rol= arrayOf("Alumno", "Maestro")

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userref: DatabaseReference

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

    private fun init(){
        this.database = FirebaseDatabase.getInstance()
        this.userref = database.getReference(Constantes.KEY_COLLECTION_USERS)
    }

    private fun encodeImage(bitmap: Bitmap): String{
        val previewWidth = 150
        val previewHeight = bitmap.height * previewWidth / bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    //Metodo que permite al usuario seleccionar una imagen de su galeria
    private val pickImage: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val profilePicture = findViewById<RoundedImageView>(R.id.fotoPerfil)
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
                val imageUri = result.data!!.data
                try {
                    val inputStream =
                        contentResolver.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    profilePicture.setImageBitmap(bitmap)
                    encodedImage = encodeImage(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    //Método que desplega una barra de progreso para hacer más estética la transicion del Registro a la pantalla principal
    private fun loading(isLoading:Boolean){
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val buttonSignUp = findViewById<Button>(R.id.botonSignIn)

        if(isLoading) {
            buttonSignUp.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }else{
            buttonSignUp.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }

    }
    private fun Users() {
        val usernombre = findViewById<EditText>(R.id.inputNombreCompleto)
        val userrol = findViewById<Spinner>(R.id.register_spinner_rol)
        val useremail = findViewById<EditText>(R.id.inputEmail)
        val usercarrera = findViewById<Spinner>(R.id.register_spinner_carreras)
        val usercontrasena = findViewById<EditText>(R.id.inputContrasena)

        loading(true)
        var usuario = HashMap<String, Any>()
        usuario.put(Constantes.KEY_NAME, usernombre.text.toString())
        usuario.put(Constantes.KEY_EMAIL, useremail.text.toString())
        usuario.put(Constantes.KEY_PASSWORD, usercontrasena.text.toString())
        usuario.put(Constantes.KEY_ROL, userrol.selectedItem.toString())
        usuario.put(Constantes.KEY_CARRERA, usercarrera.selectedItem.toString())
        usuario.put(Constantes.KEY_IMAGE, encodedImage)

        val userfirebase = userref.push()
        userfirebase.setValue(usuario)
    }


    fun validation():Boolean{
        var isValid=true

        if(binding.inputNombreCompleto.text.isNullOrEmpty()){
            isValid=false
            Toast.makeText( this,"Ingrese su nombre", Toast.LENGTH_SHORT).show()
        }

        if(binding.inputEmail.text.isNullOrEmpty()){
            isValid=false
            Toast.makeText( this,"Ingrese su correo", Toast.LENGTH_SHORT).show()
        }

        if(binding.inputContrasena.text.isNullOrEmpty()){
            isValid=false
            Toast.makeText( this,"Ingrese contraseña", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }

}

