package com.poi.union.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.SearchView
import com.poi.union.R

class SelectUsersGroupCharActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_users_group_char)

        val btn_create = findViewById<ImageView>(R.id.btnCreateGroup)
        val search = findViewById<SearchView>(R.id.busqueda)
        val listView=findViewById<ListView>(R.id.rvAllUsersAddtoGroup)

        btn_create.setOnClickListener(){
            val myIntent =  Intent(this, MessagesActivity::class.java)
            startActivity(myIntent)
            finish()
        }

        val chats= arrayOf("Dhary", "Arely", "Mariana", "Omar")

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            chats
        )

        listView.adapter=adapter

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                if(chats.contains(p0)){
                    adapter.filter.filter(p0)
                }else{
                    Toast.makeText( this@SelectUsersGroupCharActivity,"Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
                return false
            }
            override fun onQueryTextChange(p0: String): Boolean {
                adapter.filter.filter(p0)
                return false
            }
        })


    }
}