package com.poi.union.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.poi.union.models.UserListener
import com.poi.union.R
import com.poi.union.activities.SelectUsersGroupCharActivity
import com.poi.union.activities.LoginActivity
import com.poi.union.activities.MessagesActivity
import com.poi.union.adapters.MensajesAdapter
import com.poi.union.models.UsuarioAdapter
import com.poi.union.databinding.FragmentChatsBinding
import com.poi.union.models.*
import kotlinx.android.synthetic.main.fragment_chats.view.*

class ChatsFragment : Fragment(R.layout.fragment_chats), UserListener {
    private var userList = mutableListOf<Users>()

    private lateinit var database: FirebaseDatabase
    private lateinit var userref: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: UsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.init()
        userref.addValueEventListener(valueEventListener)
    }

    private fun init(){
        adaptador = UsuarioAdapter(userList, this)
        this.database = FirebaseDatabase.getInstance()
        this.userref = database.getReference(Constantes.KEY_COLLECTION_USERS)
        recyclerView.adapter = adaptador
        recyclerView = requireActivity().findViewById<RecyclerView>(R.id.list_view)
        recyclerView.visibility = View.VISIBLE

    }

    private fun getUsers(){
        userref.addValueEventListener(valueEventListener)
    }

    private val valueEventListener = object: ValueEventListener {
        var preferenceManager = PreferenceManager(requireContext())

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

            }

            if(userList.size > 0){
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
