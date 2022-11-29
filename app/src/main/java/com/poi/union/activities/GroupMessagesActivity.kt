package com.poi.union.activities

import android.content.Intent
import android.icu.text.AlphabeticIndex
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.makeramen.roundedimageview.RoundedImageView
import com.poi.union.MainActivity
import com.poi.union.R
import com.poi.union.adapters.MensajesAdapter
import com.poi.union.models.*
import com.poi.union.utils.CifradoTools
import java.security.GeneralSecurityException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GroupMessagesActivity:AppCompatActivity() {
    private val listaMensajes= mutableListOf<mensaje>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador:MensajesAdapter
    //Cambie private lateinit var adaptador:ChatAdapter por private lateinit var adaptador:MensajesAdapter
    private lateinit var nombreUsuario:String
    private var cipherActivated = false
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var groupReceiver: Grupo
    //private val groupReceiver="General"
    //variable que se uso en MessagesActivity: private lateinit var receiverUser: Users
    private lateinit var database:FirebaseDatabase
    private lateinit var chatRef: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //cambiar a otro activity_messages pero para chat grupal
        setContentView(R.layout.activity_groupmessages)

        init()
        setListeners()
        loadReceiverDetails()
        listenMessages()

        //Ubicacion
        val ubication=findViewById<ImageView>(R.id.ubicacion)

        ubication.setOnClickListener(){
            val myIntent =  Intent(this, UbicationGroup::class.java)
            startActivity(myIntent)
            finish()
        }

        val mensaje=findViewById<EditText>(R.id.etMessagePrivateChat)

        val intent = intent
        val str = intent.getStringExtra("message_key")
        mensaje.setText(str)

    }

    private fun init(){
        this.preferenceManager= PreferenceManager(LoginActivity.contextGlobal)
        this.nombreUsuario=preferenceManager.getString(Constantes.KEY_NAME).toString()
        this.cipherActivated = this.preferenceManager.getBoolean(Constantes.IS_CIPHER_ACTIVATED)
        this.adaptador = MensajesAdapter(listaMensajes, preferenceManager.getString(Constantes.KEY_EMAIL).toString())
        recyclerView=  findViewById<RecyclerView>(R.id.rvPrivateChat)
        this.database=FirebaseDatabase.getInstance()
        this.chatRef=database.getReference(Constantes.KEY_COLLECTION_CHAT)
        groupReceiver = intent.getSerializableExtra(Constantes.KEY_GROUP) as Grupo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadReceiverDetails(){

        findViewById<TextView>(R.id.tvUsernamePrivateChat).text = groupReceiver.grupoName

       val groupImage = Constantes.decodeImage(groupReceiver.grupoImagen)

        findViewById<RoundedImageView>(R.id.rivGroupImage).setImageBitmap(groupImage)

        if(this.cipherActivated) {
            findViewById<ImageButton>(R.id.ibActivateGrupalCipher).visibility =  View.INVISIBLE
            findViewById<ImageButton>(R.id.ibDeactivateGrupalCipher).visibility = View.VISIBLE
        }else{
            findViewById<ImageButton>(R.id.ibActivateGrupalCipher).visibility = View.VISIBLE
            findViewById<ImageButton>(R.id.ibDeactivateGrupalCipher).visibility = View.INVISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners(){
        findViewById<ImageView>(R.id.imageViewBack).setOnClickListener {
            val myIntent =  Intent(this, MainActivity::class.java)
            myIntent.putExtra("fragment", "2");
            startActivity(myIntent)
            finish()}
        //findViewById<ImageView>(R.id.imageViewBack).setOnClickListener{ v: View -> this.onBackPressed() }
        findViewById<ImageView>(R.id.btnEnviarPrivateChat).setOnClickListener {
            //val imagenUsuario = preferenceManager.getString(Constantes.KEY_IMAGE).toString()//Asignamos la imagen de perfil del usuario logeado a una variable
            val txtInput = findViewById<EditText>(R.id.etMessagePrivateChat)//Asignamos el texto a enviar a una variable

            if (txtInput.text.isNotEmpty()){
                var message =HashMap<String,Any>()

                message[Constantes.KEY_SENDER_ID]=preferenceManager.getString(Constantes.KEY_EMAIL).toString()
                message[Constantes.KEY_SENDER_NAME]=preferenceManager.getString(Constantes.KEY_NAME).toString()
                message[Constantes.KEY_RECEIVER_ID]=groupReceiver.grupoId
                //message.put(Constants.KEY_RECEIVER_ID, groupReceiver)

                if(this.cipherActivated){
                    message[Constantes.KEY_MESSAGE] = CifradoTools.cifrar(txtInput.text.toString(), Constantes.CIPHER_KEY)
                    message[Constantes.KEY_IS_ENCRYPTED] = true
                }
                else message[Constantes.KEY_MESSAGE] = txtInput.text.toString()

                message[Constantes.KEY_TIMESTAMP]=LocalDateTime.now()
                //message[Constantes.KEY_IMAGE] = imagenUsuario

                this.enviarMensaje(message)
                txtInput.text.clear()
            }
        }

        findViewById<ImageButton>(R.id.ibActivateGrupalCipher).setOnClickListener(){
            this.cipherActivated = true
            this.preferenceManager.putBoolean(Constantes.IS_CIPHER_ACTIVATED, true)
            it.visibility = View.INVISIBLE
            findViewById<ImageButton>(R.id.ibDeactivateGrupalCipher).visibility = View.VISIBLE
        }

        findViewById<ImageButton>(R.id.ibDeactivateGrupalCipher).setOnClickListener(){
            this.cipherActivated = false
            this.preferenceManager.putBoolean(Constantes.IS_CIPHER_ACTIVATED, false)
            it.visibility = View.INVISIBLE
            findViewById<ImageButton>(R.id.ibActivateGrupalCipher).visibility = View.VISIBLE
        }
    }

    private fun enviarMensaje(mensaje: HashMap<String,Any>){
        val mensajeFirebase=chatRef.push()
        mensajeFirebase.setValue(mensaje)
    }

    private fun listenMessages(){
        chatRef.addValueEventListener(valueEventListener)
    }

    private val valueEventListener = object : ValueEventListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onDataChange(snapshot: DataSnapshot) {

            var count=0
            listaMensajes.clear()

            for (snap in snapshot.children){

                val messageMap:HashMap<String,Any> = snap.value as HashMap<String, Any> /* = java.util.HashMap<kotlin.String, kotlin.Any> */
                val date=messageMap[Constantes.KEY_TIMESTAMP] as HashMap<String,Any>

                val dateTimeTemp=LocalDateTime.of(
                    (date[Constantes.KEY_TMSP_YEAR] as Long).toInt(),
                    (date[Constantes.KEY_TMSP_MONTH] as Long).toInt(),
                    (date[Constantes.KEY_TMSP_DAY] as Long).toInt(),
                    (date[Constantes.KEY_TMSP_HOUR] as Long).toInt(),
                    (date[Constantes.KEY_TMSP_MINUTE] as Long).toInt()
                )

                val message=mensaje()
                message.imagen=messageMap[Constantes.KEY_IMAGE].toString()
                message.senderId=messageMap[Constantes.KEY_SENDER_ID].toString()
                message.senderName=messageMap[Constantes.KEY_SENDER_NAME].toString()
                message.receiverId=messageMap[Constantes.KEY_RECEIVER_ID].toString()

                message.isEncrypted = messageMap[Constantes.KEY_IS_ENCRYPTED].toString()== "true"
                if(message.isEncrypted)
                    message.message = CifradoTools.descifrar(messageMap[Constantes.KEY_MESSAGE].toString(), Constantes.CIPHER_KEY)
                else
                    message.message = messageMap[Constantes.KEY_MESSAGE].toString()

                message.timestamp = getReadableLocalDateTime(dateTimeTemp)

                if(message.receiverId == groupReceiver.grupoId)
                    listaMensajes.add(message)
            }
            count = listaMensajes.size

            if(count > 0) {
                //findViewById<RecyclerView>(R.id.rvMensajes).smoothScrollToPosition(listaMensajes.size - 1)
                //adaptador = UsuarioAdapter(userList, self)
                recyclerView.adapter = adaptador

                val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = linearLayoutManager

                recyclerView.smoothScrollToPosition(listaMensajes.size - 1)}
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(this@GroupMessagesActivity, "Error al leer las conversaciones", Toast.LENGTH_SHORT).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getReadableLocalDateTime(date:LocalDateTime):String{
        return date.format(DateTimeFormatter.ofPattern("d/M/y H:m"))
    }

}