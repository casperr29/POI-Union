package com.poi.union.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.poi.union.R
import com.poi.union.models.Constantes
import com.poi.union.models.UserListener
import com.poi.union.models.Users

class SelectedUsersAdapter (private val ListaUsuariosSeleccionados: MutableList<Users>):
    RecyclerView.Adapter<SelectedUsersAdapter.SelectedUsersViewHolder>() {

    class SelectedUsersViewHolder(itemView: android.view.View): RecyclerView.ViewHolder(itemView){
        @RequiresApi(Build.VERSION_CODES.O)
        private fun decodeImage(encodedImage: String):Bitmap{

            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)

            return BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setUserData(users: Users){
            itemView.findViewById<TextView>(R.id.tvContactoSeleccionado).text = users.Nombre

            if (users.Foto==null||users.Foto==""){
                itemView.findViewById<RoundedImageView>(R.id.imgContacto).setImageResource(R.drawable.space)
            }else{
                val imagenUsuario=users.Foto?.let { Constantes.decodeImage(it) }
                itemView.findViewById<RoundedImageView>(R.id.imgContacto).setImageBitmap(imagenUsuario)
            }

            //itemView.setOnClickListener{ View-> userListener.onUserClicked(users)}

            var params = itemView.findViewById<RelativeLayout>(R.id.selectedUserLayout)

            val newParams=RelativeLayout.LayoutParams(
                params.layoutParams.width,
                params.layoutParams.height,
            )

            params.layoutParams=newParams

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedUsersViewHolder {
        return SelectedUsersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ventana_usuario_seleccionado, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SelectedUsersViewHolder, position: Int) {
        holder.setUserData(ListaUsuariosSeleccionados[position])
    }

    override fun getItemCount(): Int  = ListaUsuariosSeleccionados.size
}