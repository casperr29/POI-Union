package com.poi.union.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.poi.union.MainActivity
import com.poi.union.R
import com.poi.union.adapters.SelectedUsersAdapter
import com.poi.union.adapters.UsuariosGrupoAdapter
import com.poi.union.models.*
import java.time.LocalDateTime

class AddtoGroupChatActivity :AppCompatActivity() {

    private var listaUsuariosGrupo = mutableListOf<Users>()

    private var usersSelected = mutableListOf<Users>()
    private var adaptadorSelect= SelectedUsersAdapter(usersSelected)
    //private lateinit var database: FirebaseDatabase
    private val database = FirebaseDatabase.getInstance()
    private val groupRef = database.getReference(Constantes.KEY_COLLECTION_GROUPS)
    private lateinit var usersref: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var listView:ListView
    private lateinit var adaptador: UsuariosGrupoAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_users_group_char)
        usersSelected=ArrayList()

        val rvSelectedUsers=findViewById<RecyclerView>(R.id.rvContactosSeleccionados)
        //val lvCheckBox=findViewById<ListView>(R.id.listViewCheckbox)
        rvSelectedUsers.adapter=adaptadorSelect
        //rvSelectedUsers.visibility = View.VISIBLE
        rvSelectedUsers.smoothScrollToPosition(usersSelected.size-1)

        getUsers()
        setListeners()
    }


    private fun getUsers(){
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

                adaptador = UsuariosGrupoAdapter(listaUsuariosGrupo)
                //adaptador = UsuarioAdapter(userList, this@AddtoGroupChatActivity)
                //se supone que este es el listViewCheckbox del activity_select_users_grpup_char
                val viewContact = findViewById<RecyclerView>(R.id.listViewCheckbox) //se supone que este es el listViewCheckbox del activity_select_users_grpup_char
                viewContact.adapter = adaptador
                //viewContact.visibility = View.VISIBLE

                //val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
                //linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                //recyclerView.layoutManager = linearLayoutManager

                viewContact.smoothScrollToPosition(listaUsuariosGrupo.size - 1)
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
        var count=usersSelected.size

        findViewById<RecyclerView>(R.id.rvContactosSeleccionados).removeAllViews()

        if(count>0){
            val selectedView=findViewById<RecyclerView>(R.id.rvContactosSeleccionados)
            selectedView.adapter=adaptadorSelect
            selectedView.smoothScrollToPosition(usersSelected.size-1)
        }else showErrorMessage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners(){
        //Boton para crar grupo
        val btnCrearGrupo=findViewById<ImageView>(R.id.done)

        //Volver
        findViewById<ImageView>(R.id.imageViewBack).setOnClickListener {
            startActivity(Intent(LoginActivity.contextGlobal,CreateGroupActivity::class.java))
        }
        btnCrearGrupo.setOnClickListener {
            if (usersSelected.size>0){
                crearGrupo()
            }else{
                showToast("Selecciona a los miembros del grupo")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearGrupo(){
        val preferenceManager=PreferenceManager(LoginActivity.contextGlobal)
        var grupo = HashMap<String, Any>()//Creamos un objeto de tipo string
        val grupoFirebase = groupRef.push()//Hacemos referencia a la base de datos

        //Asignamos los valores a guardar para el mensaje
        grupo.put(Constantes.KEY_GROUP_NAME, intent.getStringExtra("nombreGrupo").toString())
        grupo.put(Constantes.KEY_GROUP_IMAGE, intent.getStringExtra("fotoGrupo").toString())
        grupo.put(Constantes.KEY_GROUP_ADMIN_ID, preferenceManager.getString(Constantes.KEY_EMAIL).toString())
        grupo.put(Constantes.KEY_GROUP_ADMIN_NAME, preferenceManager.getString(Constantes.KEY_NAME).toString())
        grupo.put(Constantes.KEY_GROUP_TIMESTAMP, LocalDateTime.now())

        grupoFirebase.setValue(grupo)

        val memberRef = database.getReference(Constantes.KEY_COLLECTION_GROUPS).child(grupoFirebase.key.toString()).child(Constantes.KEY_COLLECTION_GROUP_MEMBERS)
        var miembroAdmin = HashMap<String, Any>()//Creamos un objeto de tipo string

        miembroAdmin.put(Constantes.KEY_GROUP_MEMBER_ID, preferenceManager.getString(Constantes.KEY_EMAIL).toString())
        miembroAdmin.put(Constantes.KEY_GROUP_MEMBER_NAME, preferenceManager.getString(Constantes.KEY_NAME).toString())
        miembroAdmin.put(Constantes.KEY_GROUP_MEMBER_ROLE, "admin")

        memberRef.child(preferenceManager.getString(Constantes.KEY_EMAIL).toString()).setValue(miembroAdmin)

        for (i in usersSelected.indices){
            var miembro = HashMap<String, Any>()//Creamos un objeto de tipo string

            miembro.put(Constantes.KEY_GROUP_MEMBER_ID, usersSelected[i].Email)
            miembro.put(Constantes.KEY_GROUP_MEMBER_NAME, usersSelected[i].Nombre)
            miembro.put(Constantes.KEY_GROUP_MEMBER_ROLE, "miembro")

            memberRef.child(usersSelected[i].Email).setValue(miembro)
        }
        showToast("¡Grupo creado!")
        startActivity(Intent(LoginActivity.contextGlobal, MainActivity::class.java))
    }

    private fun showErrorMessage(){
        showToast(String.format("%s", "No users available"))
    }

    private fun showToast(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}