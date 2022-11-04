package com.poi.union

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
import com.makeramen.roundedimageview.RoundedImageView
import com.poi.union.Fragments.ChatsFragment
import com.poi.union.Fragments.GruposFragment
import com.poi.union.Fragments.PageAdapter
import com.poi.union.Fragments.TareasFragment
import com.poi.union.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import com.poi.union.models.Constantes
import com.poi.union.models.Constantes.Companion.decodeImage
import com.poi.union.models.Users
import com.poi.union.models.PreferenceManager
import java.math.RoundingMode


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ChatsFragment())

        val preferenceManager = PreferenceManager(applicationContext)

        val nombreperfil = findViewById<TextView>(R.id.username)
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
                else->{}
            }
            true
        }
    }



    private fun replaceFragment (fragment: Fragment){
        var fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_Layout,fragment)
        fragmentTransaction.commit()
    }


}