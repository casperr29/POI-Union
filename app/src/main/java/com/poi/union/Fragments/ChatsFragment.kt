package com.poi.union.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.adapters.MensajesAdapter
import com.poi.union.databinding.FragmentChatsBinding
import com.poi.union.models.Mensajes
import kotlinx.android.synthetic.main.fragment_chats.*
import java.util.Objects

class ChatsFragment : Fragment() {

    lateinit var binding: FragmentChatsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var mensajeList:ArrayList<Mensajes>
    private lateinit var mensajesAdapter: MensajesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chats, container, false)

        val search = view.findViewById<SearchView>(R.id.busqueda)
        //val listView=view.findViewById<ListView>(R.id.list_view)

        val recyclerView=view.findViewById<RecyclerView>(R.id.list_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(requireActivity().baseContext)

        mensajeList=ArrayList()

        mensajeList.add(Mensajes(R.drawable.mujer1,"Arely","Hola ¿Cómo haz estado?","7:00pm"))
        mensajeList.add(Mensajes(R.drawable.mujer2,"Dhary","¿Tienes clase?","5:11pm"))
        mensajeList.add(Mensajes(R.drawable.hombre1,"Rafael","Okey","7:25pm"))
        mensajeList.add(Mensajes(R.drawable.mujer3,"Mariana","Listo, ya quedó","2:15pm"))
        mensajeList.add(Mensajes(R.drawable.hombre1,"Alan","Nono","8:03pm"))
        mensajeList.add(Mensajes(R.drawable.mujer1,"Monica","Todo bien","4:46pm"))
        mensajeList.add(Mensajes(R.drawable.mujer2,"Lorena","¿Hay tarea?","5:15pm"))

        mensajesAdapter= MensajesAdapter(mensajeList)
        recyclerView.adapter=mensajesAdapter


        //Con esto obtiene el arreglo

        val names=arrayOf("Hola","Sirve?")
        
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1,
            names
        )

        //listView.adapter=adapter


        //FUNCION PARA EL SEARCH
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                search.clearFocus()
                if(names.contains(p0)){
                    adapter.filter.filter(p0)
                }else{
                    Toast.makeText( requireActivity().baseContext,"Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
                return false
            }
            override fun onQueryTextChange(p0: String): Boolean {
                adapter.filter.filter(p0)
                return false
            }
        })


        return view

    }




}
