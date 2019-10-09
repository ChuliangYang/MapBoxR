package com.demo.cl.mapbox

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.cl.mapbox.db.Pin
import com.demo.cl.mapbox.db.positionIsValid
import com.demo.cl.mapbox.db.toLatLng
import com.demo.cl.mapbox.db.toMarkerOptions
import com.demo.cl.mapbox.ui.adapter.PinAdapter
import com.demo.cl.mapbox.ui.viewmodel.MapViewModel
import com.demo.cl.mapbox.ui.viewmodel.ViewModelFactory
import com.demo.cl.mapbox.utils.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    OnMapReadyCallback, PermissionsListener {

    private val KEY_TYPE = "type"

    private lateinit var mMap: MapboxMap

    private val pinsChannel = Channel<List<Pin>>(1)


    private var mLastType: String? = null

    private var permissionsManager: PermissionsManager? = null

    private var viewModel: MapViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(
            this,
            MAPBOX_KEY
        )
        setContentView(R.layout.activity_maps)
        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(MapViewModel::class.java)

        // Create supportMapFragment
        var mapFragment: SupportMapFragment? =null
        if (savedInstanceState == null) {
            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(
                MapboxMapOptions.createFromAttributes(
                    this,
                    null
                ).apply {
                    camera(
                        CameraPosition.Builder()
                            .target(DEfAULT_LATLNG)
                            .zoom(DEFAULT_CAMERA_DISTANCE.toDouble())
                            .build()
                    )
                })

            submitFragment(R.id.container, mapFragment, MAPBOX_FRAGMENT_TAG)
        } else {
            mLastType = savedInstanceState.getString(KEY_TYPE, null)

            mapFragment =
                supportFragmentManager.findFragmentByTag(MAPBOX_FRAGMENT_TAG) as? SupportMapFragment
        }

        mapFragment?.getMapAsync(this)


        fb_find_my_location.setOnClickListener {
            if (::mMap.isInitialized && mMap.style != null) {
                enableLocationComponent(true)
            }
        }

        rv_pins.apply {
            layoutManager =
                LinearLayoutManager(this@MapsActivity, LinearLayoutManager.VERTICAL, false)
            adapter = PinAdapter().apply {
                onItemClickFun = { view, data ->
                    if (::mMap.isInitialized && data.positionIsValid()) {
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                data.toLatLng()!!, DEFAULT_CAMERA_DISTANCE.toDouble()
                            )
                        )
                        bottom_fly.closePanel()
                    }
                }
            }
        }


        viewModel?.localPins?.observe(this, Observer {
            (rv_pins.adapter as? PinAdapter)?.submitList(it)
            if (!(::mMap.isInitialized)) {
                //In case while map is not initialized, use channel instead to block and wait so we don't lose any data
                launch {
                    pinsChannel.send(it)
                }
            } else {
                updateMarkers(it)
            }
        })


    }

    override fun onStart() {
        super.onStart()
        viewModel?.getPinsFromServer()
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        mMap = mapboxMap
        val styleLoadCallBack = getInitialLoadCallBack()
        if (mLastType != null) {
            mMap.setStyle(Style.Builder().fromJson(mLastType!!), styleLoadCallBack)
        } else {
            mMap.setStyle(Style.MAPBOX_STREETS, styleLoadCallBack)
        }
    }

    private fun getInitialLoadCallBack(): (Style) -> Unit {
        return {
            launch {
                enableLocationComponent(false)
                updateMarkers(pinsChannel.receive())
                pinsChannel.close()
            }
        }
    }

    private fun updateMarkers(
        pins: List<Pin>
    ) {
        if (::mMap.isInitialized) {
            mMap.clear()
            val bound = LatLngBounds.Builder()
            for (pin in pins) {
                if (pin.positionIsValid()) {
                    mMap.addMarker(pin.toMarkerOptions()!!).let {
                        bound.include(it.position)
                    }
                }
            }
            if (pins.isNotEmpty()) {
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        bound.build().center,
                        DEFAULT_CAMERA_DISTANCE.toDouble()
                    )
                )
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.map_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var isConsumed = false
        when (item?.itemId) {
            R.id.menu_normal -> {
                if (::mMap.isInitialized) {
                    mMap.setStyle(Style.MAPBOX_STREETS)
                }
                isConsumed = true
            }

            R.id.menu_satellite -> {
                if (::mMap.isInitialized) {
                    mMap.setStyle(Style.SATELLITE_STREETS)
                }
                isConsumed = true
            }
        }

        return isConsumed || super.onOptionsItemSelected(item)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::mMap.isInitialized) {
            outState.apply {
                mMap.style?.let {
                    putString(KEY_TYPE, it.json)
                }
            }
        }
    }


    private fun enableLocationComponent(moveToLocation: Boolean) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            if (::mMap.isInitialized) {
                // Get an instance of the component
                val locationComponent = mMap.locationComponent

                // Activate with options
                locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, mMap.style!!).build()
                )

                // Enable to make component visible
                locationComponent.isLocationComponentEnabled = true

                if (moveToLocation) {
                    // Set the component's camera mode
                    locationComponent.cameraMode = CameraMode.TRACKING
                }
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager?.requestLocationPermissions(this)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG)
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mMap.getStyle {
                enableLocationComponent(true)
            }
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}