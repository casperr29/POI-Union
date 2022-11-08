package com.poi.union.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poi.union.R
import com.poi.union.models.Users
import kotlinx.android.synthetic.main.item_container_user.view.*


class UsuariosGruposAdapterDEPRECATED(val listaUsuariosGrupo: MutableList<Users>) : RecyclerView.Adapter<UsuariosGruposAdapterDEPRECATED.GruposViewHolder>(){

    fun addItem(usuarioModel : Users){
        if (!this.isItemAdded(usuarioModel.Email))
            listaUsuariosGrupo.add(usuarioModel)
        this.notifyDataSetChanged()
    }

    private fun deleteItemByEmail(emailUser : String){
        var itemPosition : Int? = null

        for (index in 0..listaUsuariosGrupo.count() - 1) {
            if (listaUsuariosGrupo.get(index).Email == emailUser) {
                itemPosition = index
                break
            }
        }

        if (itemPosition != null){
            listaUsuariosGrupo.removeAt(itemPosition)
            this.notifyDataSetChanged()
        }

    }

    public fun isItemAdded(emailUser : String) : Boolean{
        var itemAdded = false

        for (index in 0..listaUsuariosGrupo.count() - 1) {
            if (listaUsuariosGrupo.get(index).Email == emailUser) {
                itemAdded = true
                break
            }
        }

        return itemAdded
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GruposViewHolder {
        return GruposViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_container_user,parent,false)
        );
    }

    override fun onBindViewHolder(holder: GruposViewHolder, position: Int) {

        val usuarioGrupo : Users = listaUsuariosGrupo.get(position)

        holder.render(usuarioGrupo, position)

    }

    override fun getItemCount() = listaUsuariosGrupo.size

    class GruposViewHolder(val view: View): RecyclerView.ViewHolder(view){

        fun render(usuarioGrupo: Users, position: Int){

            view.textName.text = if (!usuarioGrupo.Nombre.isEmpty()) usuarioGrupo.Nombre else "Error"

            view.textEmail.text = if (!usuarioGrupo.Email.isEmpty()) usuarioGrupo.Email else "Error"

//            if (usuarioGrupo.Foto.isNotEmpty()) {
//
////                FirebaseStorage.getInstance().getReference("images/Usuarios/${usuarioGrupo.Foto}").downloadUrl
////                    .addOnSuccessListener {
////
////                        Glide.with(view.context)
////                            .load(it.toString())
////                            .into(view.item_add_grupo_foto)
////                    }
//            }
//            else {
//                view.item_add_grupo_foto!!.setImageResource(R.drawable.foto_default_perfil)
//            }
        }
    }

}