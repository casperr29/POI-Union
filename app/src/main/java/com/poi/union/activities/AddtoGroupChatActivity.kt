package com.poi.union.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.poi.union.MainActivity
import com.poi.union.R
import com.poi.union.adapters.SelectedUsersAdapter
import com.poi.union.adapters.UsuariosGrupoAdapter
import com.poi.union.models.*
import java.time.LocalDateTime

class AddtoGroupChatActivity :AppCompatActivity(), SelectedUsersListener {

    private var listaUsuariosGrupo = mutableListOf<Users>()

    private var listaUsuariosSeleccionados = mutableListOf<Users>()
    //private lateinit var database: FirebaseDatabase
    private lateinit var database: FirebaseDatabase
    private lateinit var usersref: DatabaseReference
    private lateinit var groupRef: DatabaseReference
    private lateinit var recyclerViewAllUsers: RecyclerView
    private lateinit var recyclerViewSelectedUsers: RecyclerView
    //private lateinit var listView:ListView
    private lateinit var adaptadorUsuariosGrupo: UsuariosGrupoAdapter
    private lateinit var adaptadorUsuariosSeleccionados: SelectedUsersAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_users_group_char)

        init()
        getAllUsers()
        setListeners()
    }


    private fun init(){

        recyclerViewAllUsers = findViewById<RecyclerView>(R.id.rvAllUsersAddtoGroup) //se supone que este es el listViewCheckbox del activity_select_users_grpup_char
        recyclerViewSelectedUsers = findViewById<RecyclerView>(R.id.rvContactosSeleccionados) //se supone que este es el listViewCheckbox del activity_select_users_grpup_char

        this.database = FirebaseDatabase.getInstance()

        this.usersref = database.getReference(Constantes.KEY_COLLECTION_USERS)
        this.groupRef = database.getReference(Constantes.KEY_COLLECTION_GROUPS)
        recyclerViewAllUsers.visibility = View.VISIBLE
        recyclerViewSelectedUsers.visibility = View.VISIBLE


        this.adaptadorUsuariosSeleccionados = SelectedUsersAdapter(listaUsuariosSeleccionados)
    }

    private fun getAllUsers(){
        usersref.addValueEventListener(valueEventListener)
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

                listaUsuariosGrupo.add(user)
            }

            if(listaUsuariosGrupo.size > 0){
                /*val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = linearLayoutManager*/

                adaptadorUsuariosGrupo = UsuariosGrupoAdapter(listaUsuariosGrupo, this@AddtoGroupChatActivity)
                //adaptador = UsuarioAdapter(userList, this@AddtoGroupChatActivity)
                //se supone que este es el listViewCheckbox del activity_select_users_grpup_char
                recyclerViewAllUsers.adapter = adaptadorUsuariosGrupo
                //viewContact.visibility = View.VISIBLE

                val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerViewAllUsers.layoutManager = linearLayoutManager

                recyclerViewAllUsers.smoothScrollToPosition(listaUsuariosGrupo.size - 1)
            }


        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(LoginActivity.contextGlobal, "Error al traer Usuarios", Toast.LENGTH_SHORT).show()

        }
    }
     /*@SuppressLint("NotifyDataSetChanged")
    private fun getUsers() {
        val preferenceManager = PreferenceManager(applicationContext)
        //val database = com.google.firebase.firestore.FirebaseFirestore.getInstance()//cambiar esto
       //this.database = FirebaseDatabase.getInstance()
       this.usersref = database.getReference(Constantes.KEY_COLLECTION_USERS)
       database.collection(Constantes.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val currentUserID = preferenceManager.getString(Constantes.KEY_USER) //PREGUNTAR POR SI LAS MOSCAS
                    val documentSnapshot = task.result
                    listaUsuarios = ArrayList()

                    for (doc in documentSnapshot.documents) {
                        if(currentUserID.equals(doc.id)){
                            continue
                        }
                        var user = Users()
                        user.Nombre = doc.getString(Constantes.KEY_NAME).toString()
                        user.Foto = doc.getString(Constantes.KEY_IMAGE).toString()
                        user.id = doc.id

                        listaUsuarios.add(user)

                    }
                    if(listaUsuarios.size > 0){
                        val adaptador = GroupUsersAdapter(listaUsuarios, this)
                        val viewContact = findViewById<ListView>(R.id.listViewCheckbox) //se supone que este es el listViewCheckbox del activity_select_users_grpup_char
                        //val viewContact = findViewById<RecyclerView>(R.id.listViewCheckbox)
                        viewContact.adapter = adaptador
                        //viewContact.visibility = View.VISIBLE
                        viewContact.smoothScrollToPosition(listaUsuarios.size - 1)

                    }else{
                        showErrorMessage()
                    }
                }else{
                    showErrorMessage()
                }

            }
    }*/


    private fun getSelectedUsers(){
        var count=listaUsuariosSeleccionados.size

        findViewById<RecyclerView>(R.id.rvContactosSeleccionados).removeAllViews()

        if(count>0){
            recyclerViewSelectedUsers.adapter= adaptadorUsuariosSeleccionados

            val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            recyclerViewSelectedUsers.layoutManager = linearLayoutManager

            recyclerViewSelectedUsers.smoothScrollToPosition(listaUsuariosSeleccionados.size-1)
        }else showErrorMessage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners(){
        //Boton para crar grupo
        val btnCrearGrupo=findViewById<ImageView>(R.id.btnCreateGroup)

        //Volver
        /*findViewById<ImageView>(R.id.imageViewBack).setOnClickListener {
            startActivity(Intent(LoginActivity.contextGlobal,CreateGroupActivity::class.java))
        }*/

        findViewById<ImageView>(R.id.ivBackAddtoGroup).setOnClickListener{ v: View -> this.onBackPressed() }


        btnCrearGrupo.setOnClickListener {
            if (listaUsuariosSeleccionados.size>0){
                addMembers()
            }else{
                showToast("Selecciona a los miembros del grupo")
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMembers(){
        val preferenceManager=PreferenceManager(LoginActivity.contextGlobal)
        /*var grupo = HashMap<String, Any>()//Creamos un objeto de tipo string
        val grupoFirebase = groupRef.push()//Hacemos referencia a la base de datos

        //Asignamos los valores a guardar para el mensaje
        grupo.put(Constantes.KEY_GROUP_NAME, intent.getStringExtra("nombreGrupo").toString())
        grupo.put(Constantes.KEY_GROUP_IMAGE, intent.getStringExtra("fotoGrupo").toString())
        grupo.put(Constantes.KEY_GROUP_ADMIN_ID, preferenceManager.getString(Constantes.KEY_EMAIL).toString())
        grupo.put(Constantes.KEY_GROUP_ADMIN_NAME, preferenceManager.getString(Constantes.KEY_NAME).toString())
        grupo.put(Constantes.KEY_GROUP_TIMESTAMP, LocalDateTime.now())

        grupoFirebase.setValue(grupo)*/

        val grupoFirebase = intent.getStringExtra("grupoFirebase") as DatabaseReference //Obtenemos al usuario con el que se chatea
        val grupo = intent.getSerializableExtra("grupo") as Grupo


        val memberRef = database.getReference(Constantes.KEY_COLLECTION_GROUPS).child(grupoFirebase.key.toString()).child(Constantes.KEY_COLLECTION_GROUP_MEMBERS)
        var miembroAdmin = HashMap<String, Any>()//Creamos un objeto de tipo string

        miembroAdmin.put(Constantes.KEY_GROUP_MEMBER_ID, preferenceManager.getString(Constantes.KEY_EMAIL).toString())
        miembroAdmin.put(Constantes.KEY_GROUP_MEMBER_NAME, preferenceManager.getString(Constantes.KEY_NAME).toString())
        miembroAdmin.put(Constantes.KEY_GROUP_MEMBER_ROLE, "admin")

        //memberRef.child(preferenceManager.getString(Constantes.KEY_EMAIL).toString()).setValue(miembroAdmin)
        memberRef.child(preferenceManager.getString(Constantes.KEY_NAME).toString()).setValue(miembroAdmin)

        for (i in listaUsuariosSeleccionados.indices){
            var miembro = HashMap<String, Any>()//Creamos un objeto de tipo string

            miembro.put(Constantes.KEY_GROUP_MEMBER_ID, listaUsuariosSeleccionados[i].Email)
            miembro.put(Constantes.KEY_GROUP_MEMBER_NAME, listaUsuariosSeleccionados[i].Nombre)
            miembro.put(Constantes.KEY_GROUP_MEMBER_ROLE, "miembro")

            //memberRef.child(listaUsuariosSeleccionados[i].Email).setValue(miembro)
            memberRef.child(listaUsuariosSeleccionados[i].Nombre).setValue(miembro)
        }
        showToast("¡Grupo creado!")
        val intent = Intent(LoginActivity.contextGlobal, GroupMessagesActivity::class.java)
        intent.putExtra(Constantes.KEY_GROUP, grupo)

        //startActivity(Intent(LoginActivity.contextGlobal, GroupMessagesActivity::class.java))
    }

    private fun showErrorMessage(){
        showToast(String.format("%s", "No users available"))
    }

    private fun showToast(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onUserSelected(user: Users) {
        if(listaUsuariosSeleccionados.contains(user)){
            listaUsuariosSeleccionados.remove(user)
        }else{
            listaUsuariosSeleccionados.add(user)
        }
        getSelectedUsers()
    }

}