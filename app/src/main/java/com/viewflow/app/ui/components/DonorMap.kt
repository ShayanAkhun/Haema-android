package com.viewflow.app.ui.components

import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.viewflow.app.R
import com.viewflow.app.data.NearbyDonor
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * OpenStreetMap (osmdroid) map showing a red pin for every [donors] entry.
 * Free / no API key — tiles come from the public OSM tile servers.
 */
@Composable
fun DonorMap(
    donors: List<NearbyDonor>,
    centerLatitude: Double,
    centerLongitude: Double,
    modifier: Modifier = Modifier,
    onDonorClick: (NearbyDonor) -> Unit = {}
) {
    val context = LocalContext.current

    val mapView = remember {
        // osmdroid needs a user agent set before it fetches any tiles.
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(centerLatitude, centerLongitude))
            // Keep drag gestures on the map instead of the surrounding scroll container.
            setOnTouchListener { v, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN ->
                        v.parent?.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                        v.parent?.requestDisallowInterceptTouchEvent(false)
                }
                false
            }
        }
    }

    DisposableEffect(Unit) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDetach()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { map ->
            val pin = ContextCompat.getDrawable(context, R.drawable.ic_map_pin)
            map.overlays.clear()
            donors.forEach { donor ->
                map.overlays.add(
                    Marker(map).apply {
                        position = GeoPoint(donor.latitude, donor.longitude)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        icon = pin
                        title = donor.name
                        snippet = "${donor.bloodGroup.label} · ${donor.distanceKm} km away"
                        setOnMarkerClickListener { clicked, _ ->
                            clicked.showInfoWindow()
                            map.controller.animateTo(clicked.position)
                            onDonorClick(donor)
                            true
                        }
                    }
                )
            }
            map.invalidate()
        }
    )
}
