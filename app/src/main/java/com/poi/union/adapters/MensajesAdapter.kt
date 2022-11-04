package com.poi.union.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.activities.MessagesActivity
import com.poi.union.activities.SelectUsersGroupCharActivity
import com.poi.union.models.Mensajes
import javax.inject.Singleton

class MensajesAdapter(private val messageList:ArrayList<Mensajes>)
    :RecyclerView.Adapter<MensajesAdapter.mensajesViewHolder>(){


    class mensajesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val nombreView:TextView=itemView.findViewById(R.id.userName)
        val mensajeView:TextView=itemView.findViewById(R.id.ultimoMensaje)
        val horaView:TextView=itemView.findViewById(R.id.hora)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mensajesViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return mensajesViewHolder(view)
    }

    override fun onBindViewHolder(holder: mensajesViewHolder, position: Int) {
        val mensaje=messageList[position]

        holder.nombreView.text=mensaje.name
        holder.mensajeView.text=mensaje.lastMsg
        holder.horaView.text=mensaje.lastMsgTime


        holder.nombreView.setOnClickListener{
            val nombre=mensaje.name
            val intent=Intent(holder.itemView.context,MessagesActivity::class.java)
            intent.putExtra("name",mensaje.name)

            holder.itemView.context.startActivity(intent)
        }

    }



    override fun getItemCount(): Int {
       return messageList.size
    }



}



