package com.poi.union.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.modules.core.PermissionListener
import com.poi.union.R
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetView
import java.net.URL


class JoinCallActivity: AppCompatActivity(), JitsiMeetActivityInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joincall)
        //Volver a la pantalla principal con el boton back
        findViewById<Button>(R.id.jcbackbtn).setOnClickListener{ v: View -> this.onBackPressed() }

        var join= findViewById<Button>(R.id.joinbtn)

        join.setOnClickListener {
            val view= JitsiMeetView(this@JoinCallActivity)
            val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setRoom("https://meet.jit.si/ThoroughDepthsClaimIntensely")
                .setAudioMuted(false)
                .setVideoMuted(false)
                .setAudioOnly(false)
                .setFeatureFlag("call-integration.enabled",false)
                .setConfigOverride("requireDisplayName", true)
                .build()

            view.join(options)
            setContentView(view)
        }

    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        TODO("Not yet implemented")
    }
}