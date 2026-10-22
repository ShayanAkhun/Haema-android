package com.haema.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.haema.app.data.MockData
import com.haema.app.ui.components.BloodDrop
import com.haema.app.ui.components.DonorMap
import com.haema.app.ui.components.RequestCard
import com.haema.app.ui.components.RequestCardWidth
import com.haema.app.ui.components.SectionHeader
import com.haema.app.ui.components.StatCard
import com.haema.app.ui.components.bloodGroupColor
import android.Manifest
import android.os.Build
import kotlinx.coroutines.delay

/** Process-wide so returning to Home doesn't re-prompt within the same launch. */
private var startupPermissionsAsked = false

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    onOpenRequestBlood: () -> Unit,
    onOpenDonateTab: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenRequest: (String) -> Unit
) {
    val donor = MockData.currentDonor
    // Fine + coarse together so Android 12+ shows its precise/approximate choice.
    val locationPermission = rememberMultiplePermissionsState(
        listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    )
    // Approximate-only is still enough to place the user on the map.
    val hasLocation = locationPermission.permissions.any { it.status.isGranted }

    // Location + notifications (Android 13+), asked together when Home first opens.
    // The location banner below and the Profile notifications switch stay as retries.
    val startupPermissions = rememberMultiplePermissionsState(
        buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!startupPermissionsAsked && !startupPermissions.allPermissionsGranted) {
            startupPermissionsAsked = true
            startupPermissions.launchMultiplePermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hi, ${donor.name.substringBefore(" ")} 👋",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = if (donor.nextEligibleInDays == 0) "You're eligible to donate today"
                    else "Eligible again in ${donor.nextEligibleInDays} days",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AsyncImage(
                model = donor.avatarUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onOpenProfile)
            )
        }

        // Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                value = donor.totalDonations.toString(),
                label = "Donations",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = donor.livesSaved.toString(),
                label = "Lives saved",
                modifier = Modifier.weight(1f),
                accent = MaterialTheme.colorScheme.secondary
            )
            StatCard(
                value = donor.bloodGroup.label,
                label = "Blood group",
                modifier = Modifier.weight(1f),
                accent = bloodGroupColor(donor.bloodGroup)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Donate now banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Someone needs you today",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "One donation can save up to 3 lives",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = onOpenDonateTab) {
                        Text("Donate now")
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
                BloodDrop(modifier = Modifier.size(width = 56.dp, height = 70.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Emergency requests
        SectionHeader(
            title = "Emergency requests",
            actionLabel = "Request blood",
            onActionClick = onOpenRequestBlood,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        val requestsListState = rememberLazyListState()
        val requestCount = MockData.requests.size
        val density = LocalDensity.current
        LaunchedEffect(requestCount) {
            if (requestCount > 1) {
                val stridePx = with(density) { (RequestCardWidth + 12.dp).toPx() }
                while (true) {
                    delay(3000)
                    // Skip a tick while the user is actively swiping.
                    if (!requestsListState.isScrollInProgress) {
                        val atLastCard = requestsListState.firstVisibleItemIndex >= requestCount - 1
                        // One card forward, or a slow slide back to the start once we run out of cards.
                        val delta = if (atLastCard) -stridePx * (requestCount - 1) else stridePx
                        requestsListState.animateScrollBy(
                            value = delta,
                            animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
                        )
                    }
                }
            }
        }
        LazyRow(
            state = requestsListState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.requests) { request ->
                RequestCard(request = request, onClick = { onOpenRequest(request.id) })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Donors near you
        SectionHeader(
            title = "Near you",
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = !hasLocation,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Enable location",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Allow location access to show donors near you on the map.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(onClick = { locationPermission.launchMultiplePermissionRequest() }) {
                        Text("Allow access")
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        DonorMap(
            donors = MockData.nearbyDonors,
            centerLatitude = MockData.cityLatitude,
            centerLongitude = MockData.cityLongitude,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}
