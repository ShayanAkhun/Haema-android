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

/** A single pin on [MarkersMap] - donor and request markers both reduce to this. */
data class MapMarker(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val snippet: String
)

/**
 * OpenStreetMap (osmdroid) map showing a red pin for every entry in [markers].
 * Free / no API key — tiles come from the public OSM tile servers.
 */
@Composable
fun MarkersMap(
    markers: List<MapMarker>,
    centerLatitude: Double,
    centerLongitude: Double,
    modifier: Modifier = Modifier,
    onMarkerClick: (String) -> Unit = {}
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

    // Loading/inflating the drawable on every recomposition (e.g. every keystroke
    // in a search box above the map) was measurably janky - load it once instead.
    val pin = remember(context) { ContextCompat.getDrawable(context, R.drawable.ic_map_pin) }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { map ->
            map.overlays.clear()
            markers.forEach { marker ->
                map.overlays.add(
                    Marker(map).apply {
                        position = GeoPoint(marker.latitude, marker.longitude)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        icon = pin
                        title = marker.title
                        snippet = marker.snippet
                        setOnMarkerClickListener { clicked, _ ->
                            clicked.showInfoWindow()
                            map.controller.animateTo(clicked.position)
                            onMarkerClick(marker.id)
                            true
                        }
                    }
                )
            }
            map.invalidate()
        }
    )
}

/** Donor-flavoured convenience wrapper over [MarkersMap]. */
@Composable
fun DonorMap(
    donors: List<NearbyDonor>,
    centerLatitude: Double,
    centerLongitude: Double,
    modifier: Modifier = Modifier,
    onDonorClick: (NearbyDonor) -> Unit = {}
) {
    val donorsById = remember(donors) { donors.associateBy { it.id } }
    MarkersMap(
        markers = remember(donors) {
            donors.map {
                MapMarker(
                    id = it.id,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    title = it.name,
                    snippet = "${it.bloodGroup.label} · ${it.distanceKm} km away"
                )
            }
        },
        centerLatitude = centerLatitude,
        centerLongitude = centerLongitude,
        modifier = modifier,
        onMarkerClick = { id -> donorsById[id]?.let(onDonorClick) }
    )
}
