package com.haema.app.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Hands contact actions off to the user's own apps. None of these need a runtime
 * permission: the dialer / messages / mail app opens pre-filled and the user sends it.
 */

/** Opens the dial pad with [phone] filled in. */
fun openDialer(context: Context, phone: String) =
    launch(context, Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)), "No phone app found")

/** Opens the default messaging app with a new SMS to [phone]. */
fun openSms(context: Context, phone: String) =
    launch(context, Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto", phone, null)), "No messaging app found")

/** Opens the default mail app with a new email to [address]. */
fun openEmail(context: Context, address: String) =
    launch(context, Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null)), "No email app found")

private fun launch(context: Context, intent: Intent, missingAppMessage: String) {
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, missingAppMessage, Toast.LENGTH_SHORT).show()
    }
}
