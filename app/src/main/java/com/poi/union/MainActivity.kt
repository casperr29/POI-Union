package com.poi.union

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.poi.union.Fragments.ChatsFragment
import com.poi.union.Fragments.GruposFragment
import com.poi.union.Fragments.PageAdapter
import com.poi.union.Fragments.TareasFragment
import com.poi.union.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(GruposFragment())

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
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_Layout,fragment)
        fragmentTransaction.commit()
    }


}