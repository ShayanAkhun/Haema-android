package com.haema.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.haema.app.data.BloodRequest
import com.haema.app.ui.theme.DataMonoFont
import kotlinx.coroutines.delay

/** Card width, exposed so callers (the auto-sliding carousel) can size scroll steps to match. */
val RequestCardWidth = 250.dp

private fun formatCountdown(totalSeconds: Int): String {
    if (totalSeconds <= 0) return "EXPIRED"
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}

/**
 * Ticks once a second in its own recomposition scope so the rest of the card
 * (gradient, icons, static text) doesn't redraw every second along with it.
 */
@Composable
private fun CountdownText(requestId: String, expiresInMinutes: Int, color: Color) {
    var remainingSeconds by remember(requestId) { mutableIntStateOf(expiresInMinutes * 60) }
    LaunchedEffect(requestId) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
    }
    Text(
        text = if (remainingSeconds > 0) {
            "${formatCountdown(remainingSeconds)} left"
        } else {
            "EXPIRED"
        },
        style = MaterialTheme.typography.labelMedium,
        color = color,
        fontFamily = DataMonoFont
    )
}

/**
 * A light card washed in a faint tint of its urgency colour. The blood-group chip
 * (coloured by rarity) and the urgency pill carry the colour; everything else stays
 * calm so a row of these reads cleanly.
 */
@Composable
fun RequestCard(request: BloodRequest, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val accent = urgencyColor(request.urgency)
    val surface = MaterialTheme.colorScheme.surface
    val subtle = MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        onClick = onClick,
        modifier = modifier.width(RequestCardWidth),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.07f).compositeOver(surface)),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.22f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BloodGroupChip(request.bloodGroup)
                Spacer(modifier = Modifier.weight(1f))
                UrgencyChip(request.urgency)
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = request.patientName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = request.hospital,
                style = MaterialTheme.typography.bodyMedium,
                color = subtle,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${request.unitsNeeded} unit${if (request.unitsNeeded == 1) "" else "s"} needed",
                    style = MaterialTheme.typography.labelLarge,
                    color = accent,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = subtle,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                CountdownText(
                    requestId = request.id,
                    expiresInMinutes = request.expiresInMinutes,
                    color = subtle
                )
            }
        }
    }
}
