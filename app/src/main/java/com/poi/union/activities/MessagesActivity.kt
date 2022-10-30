package com.poi.union.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.poi.union.Fragments.ChatsFragment
import com.poi.union.R

class MessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        val backBtn:ImageView=findViewById(R.id.imageViewBack)
        backBtn.setOnClickListener{
            val chatsFragment=ChatsFragment()

            supportFragmentManager.beginTransaction().
                    add(R.id.fragmentChat,chatsFragment,ChatsFragment::class.java.simpleName).
                    commit()

        }

    }
}