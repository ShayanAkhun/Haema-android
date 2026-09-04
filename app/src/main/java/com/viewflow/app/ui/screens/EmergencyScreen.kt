package com.viewflow.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.viewflow.app.data.BloodRequest
import com.viewflow.app.data.MockData
import com.viewflow.app.data.NearbyDonor
import com.viewflow.app.data.Urgency
import com.viewflow.app.ui.components.BloodGroupChip
import com.viewflow.app.ui.components.DonorMap
import com.viewflow.app.ui.components.SectionHeader
import com.viewflow.app.ui.components.SegmentedToggle
import com.viewflow.app.ui.components.UrgencyChip
import com.viewflow.app.ui.theme.AccentTeal

private enum class EmergencyTab { REQUESTS, DONORS }

/** Sort rank so Critical requests float to the top and Low sinks. */
private fun Urgency.rank(): Int = when (this) {
    Urgency.CRITICAL -> 0
    Urgency.MEDIUM -> 1
    Urgency.LOW -> 2
}

private fun expiresLabel(minutes: Int): String {
    if (minutes <= 0) return "Expired"
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "Expires in ${h}h ${m}m" else "Expires in ${m}m"
}

@Composable
fun EmergencyScreen(onNewRequest: () -> Unit) {
    var tab by remember { mutableStateOf(EmergencyTab.REQUESTS) }
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 12.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Emergency",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onNewRequest) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.size(4.dp))
                Text("New request")
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            placeholder = {
                Text(
                    if (tab == EmergencyTab.DONORS) "Search by A+, O-, B+, ETC." else "Search requests"
                )
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(12.dp))

        SegmentedToggle(
            options = listOf("Requests", "Donors"),
            selectedIndex = if (tab == EmergencyTab.REQUESTS) 0 else 1,
            onSelect = { tab = if (it == 0) EmergencyTab.REQUESTS else EmergencyTab.DONORS },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(8.dp))

        AnimatedContent(
            targetState = tab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "emergencyTab"
        ) { current ->
            when (current) {
                EmergencyTab.REQUESTS -> RequestsList(query = query)
                EmergencyTab.DONORS -> DonorsList(query = query)
            }
        }
    }
}

@Composable
private fun RequestsList(query: String) {
    val q = query.trim()
    val requests = remember(q) {
        MockData.requests
            .filter {
                q.isBlank() ||
                    it.patientName.contains(q, ignoreCase = true) ||
                    it.hospital.contains(q, ignoreCase = true) ||
                    it.bloodGroup.label.contains(q, ignoreCase = true)
            }
            .sortedWith(compareBy({ it.urgency.rank() }, { it.expiresInMinutes }))
    }

    if (requests.isEmpty()) {
        EmptyState("No matching requests")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(requests, key = { it.id }) { request ->
            EmergencyRequestCard(request)
        }
    }
}

@Composable
private fun EmergencyRequestCard(request: BloodRequest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildString {
                        append(request.requestedBy.ifBlank { request.patientName })
                        append("  ·  ")
                        append("${request.postedMinutesAgo}m ago")
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                UrgencyChip(request.urgency)
            }

            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                BloodGroupChip(request.bloodGroup)
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "${request.unitsNeeded} unit${if (request.unitsNeeded == 1) "" else "s"} needed",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(6.dp))
            Text(
                text = "${request.patientName} · ${request.hospital}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (request.note.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = request.note,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = expiresLabel(request.expiresInMinutes),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (request.respondents > 0) {
                    Spacer(Modifier.size(10.dp))
                    Icon(
                        imageVector = Icons.Filled.Groups,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = "${request.respondents} responding",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
                ) {
                    Text("I can help")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonorsList(query: String) {
    var listView by remember { mutableIntStateOf(1) } // 0 = Map, 1 = List
    val context = LocalContext.current
    val q = query.trim()
    val donors = remember(q) {
        MockData.nearbyDonors
            .filter { q.isBlank() || it.bloodGroup.label.contains(q, ignoreCase = true) || it.name.contains(q, ignoreCase = true) }
            .sortedBy { it.distanceKm }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SegmentedToggle(
                options = listOf("Map View", "List View"),
                selectedIndex = listView,
                onSelect = { listView = it },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (listView == 0) {
            item {
                DonorMap(
                    donors = donors,
                    centerLatitude = MockData.cityLatitude,
                    centerLongitude = MockData.cityLongitude,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(18.dp))
                )
            }
        }

        item {
            SectionHeader(
                title = "Nearby Donors",
                actionLabel = "See All",
                onActionClick = {}
            )
        }

        if (donors.isEmpty()) {
            item { EmptyState("No matching donors") }
        }
        items(donors, key = { it.id }) { donor ->
            NearbyDonorCard(
                donor = donor,
                onMessage = {
                    runCatching {
                        context.startActivity(
                            Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${donor.phone}"))
                        )
                    }
                },
                onCall = {
                    runCatching {
                        context.startActivity(
                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:${donor.phone}"))
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun NearbyDonorCard(
    donor: NearbyDonor,
    onMessage: () -> Unit,
    onCall: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = donor.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.size(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = donor.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    if (donor.verified) {
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Verified,
                                contentDescription = null,
                                tint = AccentTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.size(4.dp))
                            Text(
                                text = "Verified Donor",
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentTeal
                            )
                        }
                    }
                }
                BloodGroupChip(donor.bloodGroup)
            }

            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = "Donated ${donor.lastDonatedAgo}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = if (donor.eligible) AccentTeal else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = if (donor.eligible) "Eligible to Donate" else "Not eligible yet",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (donor.eligible) AccentTeal else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onMessage,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(8.dp))
                    Text("Message")
                }
                Button(
                    onClick = onCall,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(8.dp))
                    Text("Call")
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
