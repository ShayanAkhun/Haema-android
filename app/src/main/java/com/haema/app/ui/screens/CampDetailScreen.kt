package com.haema.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.haema.app.data.BloodCamp
import com.haema.app.ui.components.CampBanner
import com.haema.app.ui.components.DonorAvatarStack
import com.haema.app.ui.components.LocationRow
import com.haema.app.ui.components.ProgressRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampDetailScreen(
    camp: BloodCamp,
    onBack: () -> Unit,
    onRegisterToDonate: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(camp.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            CampBanner(
                bannerUrl = camp.bannerUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = camp.hospital, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            LocationRow(text = camp.address)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Drive date: ${camp.date}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))
            ProgressRow(
                label = "Collection progress",
                current = camp.unitsCollected,
                goal = camp.unitsGoal
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Registered donors", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                DonorAvatarStack(avatarUrls = camp.registeredDonorAvatars)
                Spacer(modifier = Modifier.width(0.dp))
                Text(
                    text = "  ${camp.registeredDonorAvatars.size}+ donors registered",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = onRegisterToDonate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register to donate")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
