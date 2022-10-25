package com.poi.union.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.adapters.UsuariosGruposAdapter
import com.poi.union.models.Users

class UsersActivity : AppCompatActivity() {

    private lateinit var recyclerViewUsuarios : RecyclerView
    private lateinit var usuariosAdapter: UsuariosGruposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        this.recyclerViewUsuarios = findViewById<RecyclerView>(R.id.usersRecyclerView)

        var listaUsuarios: MutableList<Users> = mutableListOf()


        var usuario =  Users()
        usuario.Nombre="Dhary"
        usuario.ApPaterno="Garcia"
        usuario.ApMaterno="Arroyo"
        usuario.Email="dharia@gmmail.com"

        listaUsuarios.add(usuario)

        var usuario2 =  Users()
        usuario2.Nombre="Luisito"
        usuario2.ApPaterno="Daniel"
        usuario2.ApMaterno="Sanchez"
        usuario2.Email="luisito@gmmail.com"

        listaUsuarios.add(usuario2)

        var usuario3 =  Users()
        usuario3.Nombre="Alan"
        usuario3.ApPaterno="Lightwood"
        usuario3.ApMaterno="Gonzales"
        usuario3.Email="sensei@gmmail.com"

        listaUsuarios.add(usuario3)

        var usuario4 =  Users()
        usuario4.Nombre="Rafael"
        usuario4.ApPaterno="Velasco"
        usuario4.ApMaterno="Huerta"
        usuario4.Email="rafita@gmmail.com"

        listaUsuarios.add(usuario4)



        usuariosAdapter = UsuariosGruposAdapter(listaUsuarios)
        recyclerViewUsuarios.apply {
            adapter = usuariosAdapter
            layoutManager = LinearLayoutManager(context)
        }



    }


    private fun setupMiembrosRecyclerView(){


        var usuario =  Users()
        usuario.Nombre="Dhary"
        usuario.ApPaterno="Garcia"
        usuario.ApMaterno="Arroyo"
        usuario.Email="dharia@gmmail.com"

        usuariosAdapter.addItem(usuario)

        var usuario2 =  Users()
        usuario.Nombre="Luisito"
        usuario.ApPaterno="Daniel"
        usuario.ApMaterno="Sanchez"
        usuario.Email="luisito@gmmail.com"

        usuariosAdapter.addItem(usuario2)

        var usuario3 =  Users()
        usuario.Nombre="Alan"
        usuario.ApPaterno="Lightwood"
        usuario.ApMaterno="Gonzales"
        usuario.Email="sensei@gmmail.com"

        usuariosAdapter.addItem(usuario3)

        var usuario4 =  Users()
        usuario.Nombre="Rafael"
        usuario.ApPaterno="Velasco"
        usuario.ApMaterno="Huerta"
        usuario.Email="rafita@gmmail.com"

        usuariosAdapter.addItem(usuario4)


    }

}