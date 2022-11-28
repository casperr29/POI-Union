package com.poi.union.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.poi.union.R

class UbicationGroup : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    companion object{
        const val REQUEST_CODE_LOCATION=0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubication_group)
        createMapFragment()
    }

    private fun createMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableLocation()
    }

    private fun createMarker() {
        val favoritePlace = LatLng(25.726609, -100.311872)
        map.addMarker(MarkerOptions().position(favoritePlace).title("UANL"))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(favoritePlace, 18f),
            4000,
            null
        )
    }

    private fun isLocationPermissionGranted()= ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION
    )== PackageManager.PERMISSION_GRANTED



    private fun enableLocation(){
        if(!::map.isInitialized)return
        if(isLocationPermissionGranted()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled=true
        }else{
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                map.isMyLocationEnabled=true
            }else{
                Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()

            }else->{}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized)return
        if(!isLocationPermissionGranted()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled=false
            Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        val currentLocation = LatLng(p0.latitude, p0.longitude)
        Toast.makeText(this, "Te encuentras en $currentLocation", Toast.LENGTH_SHORT).show()

        val location=currentLocation.toString()

        //pasar el valor de current location al activity

        val myIntent =  Intent(this, GroupMessagesActivity::class.java)
        myIntent.putExtra("message_key",location)
        startActivity(myIntent)
    }


}