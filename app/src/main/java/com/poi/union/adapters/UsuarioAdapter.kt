package com.poi.union.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.models.UserListener
import com.poi.union.models.Users


class UsuarioAdapter(private val listaUsuarios: MutableList<Users>, private var userListener: UserListener):
    RecyclerView.Adapter<UsuarioAdapter.UserViewHolder>(){

    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){


        fun setUserData(user: Users, userListener: UserListener) {

            itemView.findViewById<TextView>(R.id.userName).text = user.Nombre
            itemView.findViewById<TextView>(R.id.userState).text=user.Activo

            itemView.setOnClickListener{ userListener.onUserClicked(user)}

            var params = itemView.findViewById<LinearLayout>(R.id.itemUserContainer)

            val newParams = LinearLayout.LayoutParams(
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

