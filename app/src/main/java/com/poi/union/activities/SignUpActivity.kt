package com.poi.union.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makeramen.roundedimageview.RoundedImageView
import com.poi.union.R
import com.poi.union.models.Users
import com.poi.union.models.Constantes
import com.poi.union.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import kotlin.collections.HashMap
import com.poi.union.MainActivity

class SignUpActivity : AppCompatActivity() {

    private var encodedImage = ""
    val materias= arrayOf("LMAD", "LCC", "LF", "LM", "LA", "LSTI")
    val rol= arrayOf("Alumno", "Maestro")
    var userrol = ""
    var usercarrera = ""

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userref: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)



        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        val btn_login = findViewById<TextView>(R.id.textoLogIn)

        //Funcion para guardar los datos en firebase aplicando la validación
        val btn_signin = findViewById<Button>(R.id.botonLogin2)

        btn_signin.setOnClickListener{
            if (validation()) {
                val usernombre = findViewById<EditText>(R.id.inputNombreCompleto)
                val useremail = findViewById<EditText>(R.id.inputEmail)
                val usercontrasena = findViewById<EditText>(R.id.inputContrasena)

                loading(true)
                var usuario = HashMap<String, Any>()
                usuario[Constantes.KEY_NAME] = usernombre.text.toString()
                usuario[Constantes.KEY_EMAIL] = useremail.text.toString()
                usuario[Constantes.KEY_PASSWORD] = usercontrasena.text.toString()
                usuario[Constantes.KEY_ROL] = userrol
                usuario[Constantes.KEY_CARRERA] = usercarrera
                usuario[Constantes.KEY_IMAGE] = encodedImage

                val userfirebase = userref.push()
                userfirebase.setValue(usuario)

                val myIntent =  Intent(this, MainActivity::class.java)
                startActivity(myIntent)
                finish()
            }

        }

        //Funcion para abrir la galeria y asignar una foto
        val profilePicture = findViewById<RoundedImageView>(R.id.fotoPerfil)//Obtenemos la vista de la foto de perfil

        profilePicture.setOnClickListener{ //Asgingamos un evento al presionar la vista de la foto de perfil
            var intento = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) //Creamos un intent para acceder a la galeria del dispositivo
            intento.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//Definimos los permisios
            pickImage.launch(intento)//Ejecutamos el intent
        }

        btn_login.setOnClickListener{
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
                usercarrera = materias[position]
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
                userrol = rol[position]

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
        val buttonSignIn = findViewById<Button>(R.id.botonLogin2)

        if(isLoading) {
            buttonSignIn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }else{
            buttonSignIn.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }

    }

    fun validation():Boolean{

        if(binding.inputNombreCompleto.text.isNullOrEmpty()){
            Toast.makeText( this,"Ingrese su nombre", Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.inputEmail.text.isNullOrEmpty()){
            Toast.makeText( this,"Ingrese su correo", Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.inputContrasena.text.isNullOrEmpty()){
            Toast.makeText( this,"Ingrese contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}