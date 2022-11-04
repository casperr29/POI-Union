package com.poi.union.models

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener
import com.poi.union.R


class UsuarioAdapter(private val listaUsuarios: MutableList<Users>, private var userListener: UserListener):
    RecyclerView.Adapter<UsuarioAdapter.UserViewHolder>(){

    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        @RequiresApi(Build.VERSION_CODES.O)

        fun setUserData(user: Users, userListener: UserListener) {

            itemView.findViewById<TextView>(R.id.userName).text = user.Nombre

            itemView.setOnClickListener{ userListener.onUserClicked(user)}

            var params = itemView.findViewById<ConstraintLayout>(R.id.userContainerChatConv)

            val newParams = ConstraintLayout.LayoutParams(
                params.layoutParams.width,
                params.layoutParams.height,
            )
            params.layoutParams = newParams

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setUserData(listaUsuarios[position], userListener)
    }

    override fun getItemCount(): Int  = listaUsuarios.size
}

