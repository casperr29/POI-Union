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
import com.poi.union.R
import com.poi.union.databinding.FragmentChatsBinding
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment : Fragment() {

    lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chats, container, false)

        val search = view.findViewById<ListView>(R.id.busqueda)


        val user= arrayOf("Arely","Dhary","Mariana","Alan")
        
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1,
            user
        )

        list_view.adapter=adapter

        busqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search.clearFocus()
                if(user.contains(query)){
                    adapter.filter.filter(query)
                }else{
                    Toast.makeText( requireActivity().baseContext,"Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })


        return view

    }




}