package com.poi.union.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makeramen.roundedimageview.RoundedImageView
import com.poi.union.Fragments.GruposFragment
import com.poi.union.R
import com.poi.union.models.Constantes
import com.poi.union.models.PreferenceManager
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.time.LocalDateTime

class CreateGroupActivity:AppCompatActivity() {
    private var encodedImage=""//Almacenar la foto de perfil
    private lateinit var database: FirebaseDatabase
    private lateinit var groupRef: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.createchatgroup)
        init()
        setListeners()
    }

    private fun init(){
        this.database = FirebaseDatabase.getInstance()
        this.groupRef = database.getReference(Constantes.KEY_COLLECTION_GROUPS)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners(){

        //Obtenemos el boton de registrarse
        val buttonSiguiente=findViewById<Button>(R.id.Crear_btn)

        //Volver a la pantalla de lista de grupos
        findViewById<Button>(R.id.Volver_btn).setOnClickListener {
            changeFragment(GruposFragment(),"grupos_fragment")
        }

        //Obtenemos la vista de la foto de grupo
        val imagenGrupo=findViewById<RoundedImageView>(R.id.imgCreateGroup)

        imagenGrupo.setOnClickListener {
            //Creamos un intent para acceder a la galeria del dispositivo
            var intento= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intento.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//Definimos los permisios
            pickImage.launch(intento)//Ejecutamos el intent
        }

        //Asignamos un evento al presionar el boton de siguiente
        buttonSiguiente.setOnClickListener {
            //Validacion del formulario de registro
            if (isValidGroupDetails()){
                //Proceso para insertar el grupo
                createGroup()
            }
        }
    }

    //Mostar un mensaje con información adicional
    private fun showToast(message:String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    //Envio de los datos del grupo a crear
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createGroup(){
        val nombreGpoInput=findViewById<EditText>(R.id.set_nombreGrupo)

        showToast("Cargando grupo")

        var preferenceManager = PreferenceManager(LoginActivity.contextGlobal)
        var grupo = HashMap<String, Any>()//Creamos un objeto de tipo string
        val grupoFirebase = groupRef.push()//Hacemos referencia a la base de datos

        //Asignamos los valores a guardar para el mensaje
        grupo.put(Constantes.KEY_GROUP_NAME, intent.getStringExtra("nombreGrupo").toString())
        grupo.put(Constantes.KEY_GROUP_IMAGE, intent.getStringExtra("fotoGrupo").toString())
        grupo.put(Constantes.KEY_GROUP_ADMIN_ID, preferenceManager.getString(Constantes.KEY_EMAIL).toString())
        grupo.put(Constantes.KEY_GROUP_ADMIN_NAME, preferenceManager.getString(Constantes.KEY_NAME).toString())
        grupo.put(Constantes.KEY_GROUP_TIMESTAMP, LocalDateTime.now())

        grupoFirebase.setValue(grupo)

        val intent=Intent(LoginActivity.contextGlobal,AddtoGroupChatActivity::class.java)
       // intent.putExtra("nombreGrupo",nombreGpoInput.text.toString())
        //intent.putExtra("fotoGrupo", encodedImage)
        intent.putExtra("grupo", grupo)
        intent.putExtra("grupoFirebase", grupoFirebase.toString())
        startActivity(intent)
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

    //Permitir al usuario seleccionar una imagen de su galeria
    private val pickImage: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val groupPicture = findViewById<RoundedImageView>(R.id.imgCreateGroup)
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
                val imageUri = result.data!!.data
                try {
                    val inputStream =
                        contentResolver.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    groupPicture.setImageBitmap(bitmap)
                    encodedImage = encodeImage(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    //Validar el registro del grupo
    private fun isValidGroupDetails():Boolean{
        val nombreGpoInput=findViewById<EditText>(R.id.set_nombreGrupo)

        if(encodedImage==""){
            showToast("Selecciona una imagen para tu perfil")
            return false
        }else if (nombreGpoInput.text.isEmpty()){
            showToast("Ingresa un nombre de usuario")
            return false
        }
        return true
    }

    //Funcion que reemplaza el fragment anterior por el que se seleccione
    private fun changeFragment(newFragment: Fragment, tag:String){
        val oldFragment = supportFragmentManager.findFragmentByTag(tag)
        if(oldFragment == null){
            supportFragmentManager.
            beginTransaction().
            replace(R.id.fragmentGrupos, newFragment).
            commit()
        }
    }
}