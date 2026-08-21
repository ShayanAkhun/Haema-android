package com.viewflow.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.viewflow.app.data.MockData
import com.viewflow.app.ui.components.RequestCard
import com.viewflow.app.ui.components.SectionHeader
import com.viewflow.app.ui.components.StatCard
import android.Manifest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    onOpenCamp: (String) -> Unit,
    onOpenRequestBlood: () -> Unit,
    onOpenDonateTab: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val donor = MockData.currentDonor
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

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
                accent = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Donate now banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
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
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "One donation can save up to 3 lives",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onOpenDonateTab,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Donate now")
                    }
                }
                Icon(
                    imageVector = Icons.Filled.Bloodtype,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.35f),
                    modifier = Modifier.size(64.dp)
                )
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
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.requests) { request ->
                RequestCard(request = request)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nearby camps
        SectionHeader(
            title = "Camps near you",
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = !locationPermission.status.isGranted,
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
                        text = "Allow location access to sort blood camps by distance from you.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(onClick = { locationPermission.launchPermissionRequest() }) {
                        Text("Allow access")
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.camps) { camp ->
                Card(
                    modifier = Modifier
                        .width(240.dp)
                        .clickable { onOpenCamp(camp.id) },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        com.viewflow.app.ui.components.CampBanner(
                            bannerUrl = camp.bannerUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = camp.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        com.viewflow.app.ui.components.LocationRow(text = "${camp.distanceKm} km · ${camp.date}")
                    }
                }
            }
        }
    }
}
