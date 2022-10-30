package com.poi.union.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.poi.union.R

class GruposFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_grupos, container, false)

        val search = view.findViewById<SearchView>(R.id.busqueda)
        val listView=view.findViewById<ListView>(R.id.listView)

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
        })

        return view
    }


}