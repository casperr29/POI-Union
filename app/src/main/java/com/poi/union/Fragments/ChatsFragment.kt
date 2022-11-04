package com.poi.union.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.poi.union.R
import com.poi.union.activities.LoginActivity
import com.poi.union.activities.MessagesActivity
import com.poi.union.models.*

class ChatsFragment : Fragment(R.layout.fragment_chats), UserListener {
    private var userList = mutableListOf<Users>()
    private var self = this

    private lateinit var database: FirebaseDatabase
    private lateinit var userref: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: UsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.init()
        this.getUsers()
    }

    private fun init(){

        recyclerView = requireActivity().findViewById<RecyclerView>(R.id.list_view)


        this.database = FirebaseDatabase.getInstance()

        this.userref = database.getReference(Constantes.KEY_COLLECTION_USERS)
        recyclerView.visibility = View.VISIBLE

    }

    private fun getUsers(){
        userref.addValueEventListener(valueEventListener)
    }

    private val valueEventListener = object: ValueEventListener {
        //var preferenceManager = PreferenceManager(requireContext())
        var preferenceManager = PreferenceManager(LoginActivity.contextGlobal)

        override fun onDataChange(snapshot: DataSnapshot) {
            val currentUserEmail = preferenceManager.getString(Constantes.KEY_EMAIL)

            for (snap in snapshot.children) {
                val userMap: HashMap<String, Any> = snap.value as HashMap<String, Any>

                if(currentUserEmail.equals(userMap[Constantes.KEY_EMAIL].toString())){
                    continue
                }
                val user = Users()

                user.Nombre = userMap[Constantes.KEY_NAME].toString()
                user.Email = userMap[Constantes.KEY_EMAIL].toString()
                user.Contrasena = userMap[Constantes.KEY_PASSWORD].toString()
                user.Carrera = userMap[Constantes.KEY_CARRERA].toString()
                user.Foto = userMap[Constantes.KEY_ROL].toString()
                user.Rol = userMap[Constantes.KEY_ROL].toString()

                userList.add(user)
            }

            if(userList.size > 0){
                /*val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = linearLayoutManager*/

                adaptador = UsuarioAdapter(userList, self)
                recyclerView.adapter = adaptador

                val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = linearLayoutManager

                recyclerView.smoothScrollToPosition(userList.size - 1)
            }




        }
        override fun onCancelled(error: DatabaseError) {
            /*TODO("Not yet implemented")*/
        }

    }

    override fun onUserClicked(user: Users) {
        val intent = Intent(LoginActivity.contextGlobal, MessagesActivity::class.java)
        intent.putExtra(Constantes.KEY_USER, user)
        startActivity(intent)
    }
}
