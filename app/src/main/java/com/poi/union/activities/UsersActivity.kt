package com.poi.union.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.adapters.UsuariosGruposAdapterDEPRECATED

class UsersActivity : AppCompatActivity() {

    private lateinit var recyclerViewUsuarios : RecyclerView
    private lateinit var usuariosAdapter: UsuariosGruposAdapterDEPRECATED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

    }




}