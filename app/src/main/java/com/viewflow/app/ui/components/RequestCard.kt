package com.viewflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewflow.app.data.BloodRequest
import com.viewflow.app.data.Urgency
import com.viewflow.app.ui.theme.DataMonoFont
import com.viewflow.app.ui.theme.RequestCritical
import com.viewflow.app.ui.theme.RequestLow
import com.viewflow.app.ui.theme.RequestMedium
import kotlinx.coroutines.delay

/** Card width, exposed so callers (the auto-sliding carousel) can size scroll steps to match. */
val RequestCardWidth = 250.dp

private fun requestAccent(urgency: Urgency): Color = when (urgency) {
    Urgency.CRITICAL -> RequestCritical
    Urgency.MEDIUM -> RequestMedium
    Urgency.LOW -> RequestLow
}

// Medium's accent is a bright yellow, so white text (fine on the dark red and
// green accents) would fail contrast there — pick text/icon color per card
// from the background's luminance instead of assuming white.
private fun onAccentColor(accent: Color): Color =
    if (accent.luminance() > 0.5f) Color.Black else Color.White

private fun formatCountdown(totalSeconds: Int): String {
    if (totalSeconds <= 0) return "EXPIRED"
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}

@Composable
fun RequestCard(request: BloodRequest, modifier: Modifier = Modifier) {
    val accent = requestAccent(request.urgency)
    val onAccent = onAccentColor(accent)

    var remainingSeconds by remember(request.id) { mutableIntStateOf(request.expiresInMinutes * 60) }
    LaunchedEffect(request.id) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
    }

    Card(
        modifier = modifier.width(RequestCardWidth),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = accent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box {
            // Diagonal light sheen so the flat fill reads with depth.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.18f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.12f)
                            )
                        )
                    )
            )
            // Blood-drop watermark bleeding off the bottom-right corner.
            Icon(
                imageVector = Icons.Filled.Bloodtype,
                contentDescription = null,
                tint = onAccent.copy(alpha = 0.12f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .size(96.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(onAccent.copy(alpha = 0.20f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = request.bloodGroup.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = onAccent,
                            fontFamily = DataMonoFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(onAccent)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = request.urgency.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = onAccent,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = request.patientName,
                    style = MaterialTheme.typography.titleMedium,
                    color = onAccent,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = request.hospital,
                    style = MaterialTheme.typography.bodyMedium,
                    color = onAccent.copy(alpha = 0.85f),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "${request.unitsNeeded} UNITS NEEDED",
                    style = MaterialTheme.typography.labelMedium,
                    color = onAccent.copy(alpha = 0.9f),
                    fontFamily = DataMonoFont,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = onAccent.copy(alpha = 0.85f),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (remainingSeconds > 0) {
                            "expires in ${formatCountdown(remainingSeconds)}"
                        } else {
                            "EXPIRED"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = onAccent.copy(alpha = 0.85f),
                        fontFamily = DataMonoFont
                    )
                }
            }
        }
    }
}
