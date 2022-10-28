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

        val search = view.findViewById<SearchView>(R.id.busqueda)
        val listView=view.findViewById<ListView>(R.id.list_view)


        val user= arrayOf("Arely","Dhary","Mariana","Alan")
        
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1,
            user
        )

        listView.adapter=adapter

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                search.clearFocus()
                if(user.contains(p0)){
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