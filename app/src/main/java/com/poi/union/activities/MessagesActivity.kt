package com.poi.union.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.poi.union.Fragments.ChatsFragment
import com.poi.union.R
import com.poi.union.models.Constantes
import com.poi.union.models.ChatAdapter
import com.poi.union.models.PreferenceManager
import com.poi.union.models.mensaje
import com.poi.union.models.UsuarioAdapter
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi


class MessagesActivity : AppCompatActivity() {

    private val listaMensajes = mutableListOf<mensaje>()
    private lateinit var adaptador:ChatAdapter
    private lateinit var nombreUsuario: String
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var receiverUser: UsuarioAdapter
    private lateinit var database : FirebaseDatabase
    private lateinit var chatRef : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)


        val backBtn: ImageView = findViewById(R.id.imageViewBack)
        backBtn.setOnClickListener {
            val chatsFragment = ChatsFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentChat, chatsFragment, ChatsFragment::class.java.simpleName)
                .commit()

        }

    }

}