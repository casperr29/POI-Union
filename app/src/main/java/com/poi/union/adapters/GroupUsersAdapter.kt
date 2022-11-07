package com.poi.union.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.poi.union.R
import com.poi.union.models.UserListener
import com.poi.union.models.Users

class GroupUsersAdapter(private val listaUsuariosGrupo: MutableList<Users>, private var userListener: UserListener) :
    RecyclerView.Adapter<GroupUsersAdapter.GroupUsersViewHolder>(){

    class GroupUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.O)
        private fun decodeImage(encodedImage:String): Bitmap {

            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)

            return BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setUserData(user: Users, userListener: UserListener) {

            itemView.findViewById<TextView>(R.id.tvNombreContacto).text = user.Nombre

            if(user.Foto == null || user.Foto == ""){
                itemView.findViewById<RoundedImageView>(R.id.imgContacto).setImageResource(R.drawable.space)
            }else{
                val imagenUsuario = user.Foto?.let { decodeImage(it) }
                itemView.findViewById<RoundedImageView>(R.id.imgContacto).setImageBitmap(imagenUsuario)
            }

            itemView.setOnClickListener{ v: View -> userListener.onUserClicked(user)
                v.findViewById<CheckBox>(R.id.chBVentanaContacto).toggle()
            }

            var params = itemView.findViewById<ConstraintLayout>(R.id.groupUserContainer)

            val newParams = ConstraintLayout.LayoutParams(
                params.layoutParams.width,
                params.layoutParams.height,
            )
            params.layoutParams = newParams

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupUsersViewHolder {
        return GroupUsersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ventana_contactos, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: GroupUsersViewHolder, position: Int) {
        holder.setUserData(listaUsuariosGrupo[position], userListener)
    }

    override fun getItemCount(): Int  = listaUsuariosGrupo.size


}