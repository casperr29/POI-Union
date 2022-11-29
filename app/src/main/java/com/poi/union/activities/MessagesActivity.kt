package com.poi.union.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.poi.union.R
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.adapters.MensajesAdapter
import com.poi.union.models.*
import com.poi.union.utils.CifradoTools
import java.time.format.DateTimeFormatter


class MessagesActivity : AppCompatActivity() {

    private val listaMensajes = mutableListOf<mensaje>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador:MensajesAdapter
    private lateinit var nombreUsuario: String
    private var cipherActivated = false
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var receiverUser: Users
    private lateinit var database : FirebaseDatabase
    private lateinit var chatRef : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        setListeners()//Asignamos los listeners
        init()//Cargamos los detalles del usuario con el que chateamos//Inicializamos los atributos de la clase
        loadReceiverDetails()
        getMessages() //Recibimos los mensajes


        /*val backBtn: ImageView = findViewById(R.id.imageViewBack)
        backBtn.setOnClickListener {
            val chatsFragment = ChatsFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentChat, chatsFragment, ChatsFragment::class.java.simpleName)
                .commit()

        }*/


        //Ubicacion
        val ubication=findViewById<ImageView>(R.id.ubicacion)

        ubication.setOnClickListener(){
            val myIntent =  Intent(this, Ubication::class.java)
            startActivity(myIntent)
            finish()
        }

        val mensaje=findViewById<EditText>(R.id.etMessagePrivateChat)

        val intent = intent
        val str = intent.getStringExtra("message_key")
        mensaje.setText(str)

    }

    private fun init(){
        this.preferenceManager = PreferenceManager(LoginActivity.contextGlobal)
        this.nombreUsuario = preferenceManager.getString(Constantes.KEY_NAME).toString()
        this.cipherActivated = this.preferenceManager.getBoolean(Constantes.IS_CIPHER_ACTIVATED)
        this.adaptador = MensajesAdapter(listaMensajes, preferenceManager.getString(Constantes.KEY_EMAIL).toString())
        recyclerView=  findViewById<RecyclerView>(R.id.rvPrivateChat)
        this.database = FirebaseDatabase.getInstance()
        this.chatRef = database.getReference(Constantes.KEY_COLLECTION_CHAT)

    }

    //Cargamos los datos del usuario que recibirá los mensajes
    private fun loadReceiverDetails(){
        receiverUser = intent.getSerializableExtra(Constantes.KEY_USER) as Users //Obtenemos al usuario con el que se chatea
        findViewById<TextView>(R.id.tvUsernamePrivateChat).text = receiverUser.Nombre //Mostramos su nombre en la toolbar
        if(this.cipherActivated) {
            findViewById<ImageButton>(R.id.ibActivatePrivateCipher).visibility =  View.INVISIBLE
            findViewById<ImageButton>(R.id.ibDeactivatePrivateCipher).visibility = View.VISIBLE
        }else{
            findViewById<ImageButton>(R.id.ibActivatePrivateCipher).visibility = View.VISIBLE
            findViewById<ImageButton>(R.id.ibDeactivatePrivateCipher).visibility = View.INVISIBLE
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners(){
        //Volver a la pantalla principal con el boton back
        findViewById<ImageView>(R.id.imageViewBack).setOnClickListener{ v: View -> this.onBackPressed() }

        //Evento activado al presionar el botón de enviar mensaje
        findViewById<ImageView>(R.id.btnEnviarPrivateChat).setOnClickListener {

            //val imagenUsuario = preferenceManager.getString(Constantes.KEY_IMAGE).toString()//Asignamos la imagen de perfil del usuario logeado a una variable
            val txtInput = findViewById<EditText>(R.id.etMessagePrivateChat)//Asignamos el texto a enviar a una variable

            if (txtInput.text.isNotEmpty()) {//Verificamos que el input del mensaje no este vacio

                var message = HashMap<String, Any>()//Creamos un objeto de tipo string

                //Asignamos los valores a guardar para el mensaje
                message[Constantes.KEY_SENDER_ID] = preferenceManager.getString(Constantes.KEY_EMAIL).toString()
                message[Constantes.KEY_SENDER_NAME] = preferenceManager.getString(Constantes.KEY_NAME).toString()
                message[Constantes.KEY_RECEIVER_ID] = receiverUser.Email

                if(this.cipherActivated){
                    message[Constantes.KEY_MESSAGE] = CifradoTools.cifrar(txtInput.text.toString(), Constantes.CIPHER_KEY)
                    message[Constantes.KEY_IS_ENCRYPTED] = true
                }
                else message[Constantes.KEY_MESSAGE] = txtInput.text.toString()

                message[Constantes.KEY_TIMESTAMP] = LocalDateTime.now()
                //message[Constantes.KEY_IMAGE] = imagenUsuario

                this.enviarMensaje(message)//Mandamos el mensaje como parametro al método enviarMensaje()
                txtInput.text.clear() //Limpiamos el input

            }
        }

        findViewById<ImageButton>(R.id.ibActivatePrivateCipher).setOnClickListener(){
            this.cipherActivated = true
            this.preferenceManager.putBoolean(Constantes.IS_CIPHER_ACTIVATED, true)
            it.visibility = View.INVISIBLE
            findViewById<ImageButton>(R.id.ibDeactivatePrivateCipher).visibility = View.VISIBLE
        }

        findViewById<ImageButton>(R.id.ibDeactivatePrivateCipher).setOnClickListener(){
            this.cipherActivated = false
            this.preferenceManager.putBoolean(Constantes.IS_CIPHER_ACTIVATED, false)
            it.visibility = View.INVISIBLE
            findViewById<ImageButton>(R.id.ibActivatePrivateCipher).visibility = View.VISIBLE
        }
    }

    //Método que envia la información del mensaje a la base de datos
    private fun enviarMensaje(mensaje: HashMap<String, Any>) {
        val mensajeFirebase = chatRef.push()//Hacemos referencia a la base de datos
        mensajeFirebase.setValue(mensaje)//Añadimos el recién creado mensaje a la base de datos
    }

    //Se añade el valueEventListener a la referencia de la base de datos
    private fun getMessages(){
        chatRef.addValueEventListener(valueEventListener)
    }

    private val valueEventListener = object : ValueEventListener {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onDataChange(snapshot: DataSnapshot) {

            var count = 0
            listaMensajes.clear()

            for (snap in snapshot.children) {

                val messageMap: HashMap<String,Any> = snap.value as HashMap<String,Any>
                val date = messageMap[Constantes.KEY_TIMESTAMP] as HashMap<String,Any>

                val dateTimeTemp = LocalDateTime.of(
                    (date[Constantes.KEY_TMSP_YEAR] as Long).toInt(),
                    (date[Constantes.KEY_TMSP_MONTH] as Long).toInt(),
                    (date[Constantes.KEY_TMSP_DAY] as Long).toInt(),
                    (date[Constantes.KEY_TMSP_HOUR] as Long).toInt(),
                    (date[Constantes.KEY_TMSP_MINUTE] as Long).toInt()
                )

                val message = mensaje()
                message.imagen = messageMap[Constantes.KEY_IMAGE].toString()
                message.senderId = messageMap[Constantes.KEY_SENDER_ID].toString()
                message.senderName = messageMap[Constantes.KEY_SENDER_NAME].toString()
                message.receiverId = messageMap[Constantes.KEY_RECEIVER_ID].toString()

                message.isEncrypted = messageMap[Constantes.KEY_IS_ENCRYPTED].toString()== "true"
                if(message.isEncrypted)
                    message.message = CifradoTools.descifrar(messageMap[Constantes.KEY_MESSAGE].toString(), Constantes.CIPHER_KEY)
                else
                    message.message = messageMap[Constantes.KEY_MESSAGE].toString()

                message.timestamp = getReadableLocalDateTime(dateTimeTemp)

                if(message.senderId == preferenceManager.getString(Constantes.KEY_EMAIL) && message.receiverId == receiverUser.Email)
                    listaMensajes.add(message)
                else if (message.senderId == receiverUser.Email && message.receiverId == preferenceManager.getString(Constantes.KEY_EMAIL))
                    listaMensajes.add(message)

            }

            count = listaMensajes.size

            if(count > 0) {
                //adaptador = UsuarioAdapter(userList, self)
                recyclerView.adapter = adaptador

                val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = linearLayoutManager

                recyclerView.smoothScrollToPosition(listaMensajes.size - 1)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(this@MessagesActivity, "Error al leer las conversaciones", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getReadableLocalDateTime(date:LocalDateTime):String{
        return date.format(DateTimeFormatter.ofPattern("d/M/y H:m"))
    }
}