package com.poi.union.Fragments

import android.content.res.Resources.NotFoundException
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.poi.union.models.Constantes
import com.poi.union.models.mensaje

class PageAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount()=4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{ChatsFragment()}
            1->{GruposFragment()}
            2->{TareasFragment()}
            3->{SalirFragment()}
            else->{throw NotFoundException("Position not found")}
        }
    }

}