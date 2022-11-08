package com.poi.union.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.models.GroupListener
import com.poi.union.models.Grupo

class GroupsAdapter (private val listaGrupos: MutableList<Grupo>, private var groupListener: GroupListener):
    RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder>() {

    class GroupsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

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
        fun setGroupData(grupo: Grupo, groupListener: GroupListener) {

            itemView.findViewById<TextView>(R.id.nombreGrupo).text = grupo.grupoName

           /* if(grupo.grupoImagen == null || grupo.grupoImagen == ""){
                itemView.findViewById<ImageView>(R.id.imgGrupoLista).setImageResource(R.drawable.space)
            }else{
                val imagenGrupo = grupo.grupoImagen?.let { decodeImage(it) }
                itemView.findViewById<ImageView>(R.id.imgGrupoLista).setImageBitmap(imagenGrupo)
            }*/

            itemView.setOnClickListener{ v: View -> groupListener.onGroupClicked(grupo)}

            var params = itemView.findViewById<LinearLayout>(R.id.lilaGroupItemContainer)

            val newParams = LinearLayout.LayoutParams(
                params.layoutParams.width,
                params.layoutParams.height,
            )
            params.layoutParams = newParams

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupsAdapter.GroupsViewHolder {
        return GroupsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_grupo, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: GroupsAdapter.GroupsViewHolder, position: Int) {
        holder.setGroupData(listaGrupos[position], groupListener)
    }

    override fun getItemCount(): Int  = listaGrupos.size

}