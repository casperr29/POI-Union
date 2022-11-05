package com.poi.union.adapters

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.activities.MessagesActivity
import com.poi.union.activities.SelectUsersGroupCharActivity
import com.poi.union.models.Mensajes
import com.poi.union.models.mensaje
import javax.inject.Singleton

class MensajesAdapter(private val messageList: MutableList<mensaje>, private val senderId:String)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    companion object{
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2

        class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            @RequiresApi(Build.VERSION_CODES.O)
            fun setData(mensaje: mensaje) {

                itemView.findViewById<TextView>(R.id.tvUsuarioSent).text = mensaje.senderName
                itemView.findViewById<TextView>(R.id.tvMensajeSent).text = mensaje.message

                itemView.findViewById<TextView>(R.id.tvFechaSent).text = mensaje.timestamp
            }
        }

        class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            @RequiresApi(Build.VERSION_CODES.O)
            fun setData(mensaje: mensaje) {

                itemView.findViewById<TextView>(R.id.tvUsuarioReceived).text = mensaje.senderName
                itemView.findViewById<TextView>(R.id.tvMensajeReceived).text = mensaje.message

                itemView.findViewById<TextView>(R.id.tvFechaReceived).text = mensaje.timestamp
            }
        }
    }

    /*class mensajesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val nombreView:TextView=itemView.findViewById(R.id.userName)
        val mensajeView:TextView=itemView.findViewById(R.id.ultimoMensaje)
        val horaView:TextView=itemView.findViewById(R.id.hora)

    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT){
            SentMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.mensaje_enviado, parent, false)
            )
        }else{
            ReceivedMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.mensaje_recibido, parent, false)
            )
        }
        /*val view=LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return mensajesViewHolder(view)*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            (holder as SentMessageViewHolder).setData(messageList[position])
        }else{
            (holder as ReceivedMessageViewHolder).setData(messageList[position])
        }

    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        if(messageList[position].senderId == this.senderId) return VIEW_TYPE_SENT else return VIEW_TYPE_RECEIVED
    }


}



