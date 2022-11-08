package com.poi.union.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.poi.union.R
import com.poi.union.activities.*
import com.poi.union.adapters.GroupsAdapter
import com.poi.union.adapters.UsuarioAdapter
import com.poi.union.models.*

class GruposFragment : Fragment(), GroupListener {

    private var groupList = mutableListOf<Grupo>()

    private lateinit var database: FirebaseDatabase
    private lateinit var groupref: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: GroupsAdapter

// Ocupo una funcion que al dar clic a un grupo te mande a ese grupo (GroupChatActivity)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_grupos, container, false)

        val search = view.findViewById<SearchView>(R.id.busqueda)
        /*val listView=view.findViewById<RecyclerView>(R.id.lvFragmentGrupos)

        val grupos= arrayOf("Programación web de capa intermedia", "Programación orienteada a objetos", "Animación tradicional de humanos y animales", "" +
                "Base de datos multimedia", "Animación de escenarios", "Programación Web 2")

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1,
            grupos
        )

        listView.adapter=adapter

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                search.clearFocus()
                if(grupos.contains(p0)){
                    adapter.filter.filter(p0)
                }else{
                    Toast.makeText( requireActivity().baseContext,"Grupo no encontrado", Toast.LENGTH_SHORT).show()
                }
                return false
            }
            override fun onQueryTextChange(p0: String): Boolean {
                adapter.filter.filter(p0)
                return false
            }
        })*/

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.init()
        this.getGroups()
        this.setListeners()

    }

    private fun init(){

        recyclerView = requireActivity().findViewById<RecyclerView>(R.id.rvFragmentGrupos)

        this.database = FirebaseDatabase.getInstance()

        this.groupref = database.getReference(Constantes.KEY_COLLECTION_GROUPS)
        recyclerView.visibility = View.VISIBLE

    }

    private fun getGroups(){
        groupref.addValueEventListener(valueEventListener)
    }

    private fun setListeners(){
        val btnNuevoGrupo = requireActivity().findViewById<Button>(R.id.NewChat)

        btnNuevoGrupo.setOnClickListener(){
            val myIntent =  Intent(requireContext(), CreateGroupActivity::class.java)
            startActivity(myIntent)
        }
    }

    private val valueEventListener = object: ValueEventListener {
        var preferenceManager = PreferenceManager(LoginActivity.contextGlobal)

        override fun onDataChange(snapshot: DataSnapshot) {

            for (snap in snapshot.children) {
                val groupMap: HashMap<String, Any> = snap.value as HashMap<String, Any>

                val grupo = Grupo()

                grupo.grupoId = groupMap[Constantes.KEY_GROUP_ID].toString()
                grupo.grupoName = groupMap[Constantes.KEY_GROUP_NAME].toString()
                grupo.grupoImagen = groupMap[Constantes.KEY_GROUP_IMAGE].toString()
                grupo.adminId = groupMap[Constantes.KEY_GROUP_ADMIN_ID].toString()
                grupo.members = groupMap[Constantes.KEY_COLLECTION_GROUP_MEMBERS] as List<MiembroGrupo>?
                grupo.timestamp = groupMap[Constantes.KEY_GROUP_TIMESTAMP].toString()

                groupList.add(grupo)
            }

            if(groupList.size > 0){

                adaptador = GroupsAdapter(groupList, this@GruposFragment)
                recyclerView.adapter = adaptador

                val linearLayoutManager = LinearLayoutManager(LoginActivity.contextGlobal)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = linearLayoutManager

                recyclerView.smoothScrollToPosition(groupList.size - 1)
            }




        }
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(requireContext(), "Error al obtener los grupos", Toast.LENGTH_SHORT).show()

        }

    }


    override fun onGroupClicked(grupo: Grupo) {
        val intent = Intent(LoginActivity.contextGlobal, GroupMessagesActivity::class.java)
        intent.putExtra(Constantes.KEY_GROUP, grupo)
        startActivity(intent)
    }


}

