package com.poi.union.models

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R

import java.util.*

class ChatAdapter (private val chatMessages: MutableList<mensaje>, private val senderId:String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object{
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2

        class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            @RequiresApi(Build.VERSION_CODES.O)
            fun setData(mensaje: mensaje) {

                itemView.findViewById<TextView>(R.id.tvUsuarioSent).text = mensaje.senderName
                itemView.findViewById<TextView>(R.id.tvUsuarioSent).text = mensaje.message


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SENT){
            return SentMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.mensaje_enviado, parent, false)
            )
        }else{
            return ReceivedMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.mensaje_recibido, parent, false)
            )
        }
        /*return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_Received, parent, false)
        )*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            (holder as SentMessageViewHolder).setData(chatMessages[position])
        }else{
            (holder as ReceivedMessageViewHolder).setData(chatMessages[position])
        }
    }

    override fun getItemCount(): Int = chatMessages.size

    override fun getItemViewType(position: Int): Int {
        if(chatMessages[position].senderId == this.senderId) return VIEW_TYPE_SENT else return VIEW_TYPE_RECEIVED
    }
}