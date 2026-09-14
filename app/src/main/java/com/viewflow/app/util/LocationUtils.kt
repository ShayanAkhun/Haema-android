package com.viewflow.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale
import kotlin.coroutines.resume

private const val LOCATION_TIMEOUT_MS = 15_000L

/** A last-known fix newer than this is used as-is instead of waiting for a fresh one. */
private const val MAX_LAST_KNOWN_AGE_MS = 5 * 60_000L

/**
 * Resolves the device's current city name via the platform LocationManager + Geocoder.
 * Returns null if location can't be determined (no provider, timeout, or geocoding failure).
 * Caller must have already been granted ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION.
 */
suspend fun fetchCurrentCityName(context: Context): String? {
    val location = withTimeoutOrNull(LOCATION_TIMEOUT_MS) { currentLocation(context) } ?: return null
    return withContext(Dispatchers.IO) { reverseGeocodeCity(context, location) }
}

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
private suspend fun currentLocation(context: Context): Location? {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        ?: return null

    val provider = when {
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
        else -> null
    } ?: return null

    locationManager.getLastKnownLocation(provider)?.let { lastKnown ->
        if (System.currentTimeMillis() - lastKnown.time < MAX_LAST_KNOWN_AGE_MS) return lastKnown
    }

    return suspendCancellableCoroutine { continuation ->
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationManager.removeUpdates(this)
                if (continuation.isActive) continuation.resume(location)
            }
        }
        continuation.invokeOnCancellation { locationManager.removeUpdates(listener) }
        locationManager.requestSingleUpdate(provider, listener, Looper.getMainLooper())
    }
}

@Suppress("DEPRECATION")
private fun reverseGeocodeCity(context: Context, location: Location): String? = runCatching {
    val geocoder = Geocoder(context, Locale.getDefault())
    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
    address?.locality ?: address?.subAdminArea ?: address?.adminArea
}.getOrNull()
