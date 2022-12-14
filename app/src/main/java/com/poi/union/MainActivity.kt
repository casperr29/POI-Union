package com.poi.union

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makeramen.roundedimageview.RoundedImageView
import com.poi.union.Fragments.*
import com.poi.union.activities.LoginActivity
import com.poi.union.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import com.poi.union.models.Constantes
import com.poi.union.models.Constantes.Companion.decodeImage
import com.poi.union.models.Users
import com.poi.union.models.PreferenceManager
import java.math.RoundingMode


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userref: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = intent.getStringExtra("fragment").toString()

        if(fragment.isNotEmpty()){
            when(fragment){
               "1"->{replaceFragment(ChatsFragment())
               binding.bottomNavigation.selectedItemId = R.id.chats
               }
               "2"-> {
                   replaceFragment(GruposFragment())
                   binding.bottomNavigation.selectedItemId =  R.id.grupos
               }
               "3"-> {
                   replaceFragment(TareasFragment())
                   binding.bottomNavigation.selectedItemId =  R.id.tareas

               }
  
                else->{
                    replaceFragment(ChatsFragment())
                    binding.bottomNavigation.selectedItemId = R.id.chats
                }
            }
        }else{
            replaceFragment(ChatsFragment())

        }


        val preferenceManager = PreferenceManager(applicationContext)

        val nombreperfil = findViewById<TextView>(R.id.username)  //creo que aqui manda al username de la pantalla el nombre del usuario
        nombreperfil.text = preferenceManager.getString(Constantes.KEY_NAME)

        /* La imagen no la obtiene o no funciona encode image, asi que por el momento esta en proceso
        val fotoperfil = findViewById<RoundedImageView>(R.id.fotoPerfil)
        val profilebitmap = preferenceManager.getString(Constantes.KEY_IMAGE)
            ?.let { Constantes.decodeImage(it) }
        fotoperfil.setImageBitmap(profilebitmap)*/

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.chats->replaceFragment(ChatsFragment())
                R.id.grupos->replaceFragment(GruposFragment())
                R.id.tareas->replaceFragment(TareasFragment())

                else->{

                    var intent= Intent(applicationContext, LoginActivity::class.java)
                    preferenceManager.clear()
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                    startActivity(intent)
                }
            }
            true
        }

        //switch del estado usuario
        //Si algo no jala es aqui, checar las rutas del get reference para que se modifique el estado del usuario
        binding.switchActivo.setOnClickListener {
            if(binding.switchActivo.isChecked) {
                binding.switchActivo.text = "Online"

                var usuario = HashMap<String, Any>()
                usuario[Constantes.KEY_ACTIVE]="Online"

                val database = FirebaseDatabase.getInstance()

                val reference = database.getReference(Constantes.KEY_EMAIL)
                reference.setValue(usuario)
            } else {
                binding.switchActivo.text = "Offline"
                var usuario = HashMap<String, Any>()
                usuario[Constantes.KEY_ACTIVE]="Offline"

                val database = FirebaseDatabase.getInstance()

                val reference = database.getReference(Constantes.KEY_EMAIL)
                reference.setValue(usuario)
            }
        }


    }



    private fun replaceFragment (fragment: Fragment){
        var fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_Layout,fragment)
        fragmentTransaction.commit()
    }


}