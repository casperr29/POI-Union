package com.poi.union.adapters

import android.os.Build
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.models.SelectedUsersListener
import com.poi.union.models.Users

class UsuariosGrupoAdapter(private val listaUsuariosGrupo: MutableList<Users>,  private var selectedListener: SelectedUsersListener):
    RecyclerView.Adapter<UsuariosGrupoAdapter.UserViewHolder>() {

    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){


        fun setUserData(user: Users, selectedListener: SelectedUsersListener) {

            itemView.findViewById<TextView>(R.id.tvNombreContacto).text = user.Nombre

            itemView.setOnClickListener{
                val check =  itemView.findViewById<CheckBox>(R.id.chBVentanaContacto)
                check.isChecked = !check.isChecked
                selectedListener.onUserSelected(user)
            }


            var params = itemView.findViewById<ConstraintLayout>(R.id.groupUserContainer)

            val newParams = ConstraintLayout.LayoutParams(
                params.layoutParams.width,
                params.layoutParams.height,
            )
            params.layoutParams = newParams

        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ventana_contactos, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setUserData(listaUsuariosGrupo[position], selectedListener)
    }

    override fun getItemCount(): Int  = listaUsuariosGrupo.size


}