package com.viewflow.app.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.viewflow.app.data.BloodGroup
import com.viewflow.app.data.MockData
import com.viewflow.app.ui.components.AuthTextField
import com.viewflow.app.util.fetchCurrentCityName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(onBack: () -> Unit) {
    val donor = MockData.currentDonor
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var avatarUrl by remember { mutableStateOf(donor.avatarUrl) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) avatarUrl = uri.toString() }

    var fullName by remember { mutableStateOf(donor.name) }
    var email by remember { mutableStateOf(donor.email) }
    var phone by remember { mutableStateOf(donor.phone) }
    var city by remember { mutableStateOf(donor.city) }
    var bloodGroup by remember { mutableStateOf(donor.bloodGroup) }
    var profileSaved by remember { mutableStateOf(false) }

    var isFetchingLocation by remember { mutableStateOf(false) }
    var awaitingLocationPermission by remember { mutableStateOf(false) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    suspend fun resolveCityFromLocation() {
        isFetchingLocation = true
        val resolved = fetchCurrentCityName(context)
        isFetchingLocation = false
        if (resolved != null) {
            city = resolved
        } else {
            Toast.makeText(context, "Couldn't determine your location", Toast.LENGTH_SHORT).show()
        }
    }

    fun onLocationIconClick() {
        if (locationPermissionState.status.isGranted) {
            scope.launch { resolveCityFromLocation() }
        } else {
            awaitingLocationPermission = true
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationPermissionState.status) {
        if (awaitingLocationPermission && locationPermissionState.status.isGranted) {
            awaitingLocationPermission = false
            resolveCityFromLocation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Profile picture
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(108.dp)
                            .clip(CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-2).dp, y = (-2).dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Change photo",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))
            Text(text = "Personal info", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))

            AuthTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full name",
                leadingIcon = Icons.Filled.Person
            )
            Spacer(Modifier.height(14.dp))
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.height(14.dp))
            AuthTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone number",
                leadingIcon = Icons.Filled.Phone,
                keyboardType = KeyboardType.Phone
            )
            Spacer(Modifier.height(14.dp))
            AuthTextField(
                value = city,
                onValueChange = { city = it },
                label = "City",
                leadingIcon = Icons.Filled.LocationOn,
                leadingIconLoading = isFetchingLocation,
                leadingIconContentDescription = "Use current location",
                onLeadingIconClick = ::onLocationIconClick
            )

            Spacer(Modifier.height(20.dp))
            Text(text = "Blood group", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BloodGroup.entries.forEach { group ->
                    FilterChip(
                        selected = bloodGroup == group,
                        onClick = { bloodGroup = group },
                        label = { Text(group.label) }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            AnimatedVisibility(
                visible = profileSaved,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                SuccessBanner(text = "Profile updated successfully.")
            }
            Button(
                onClick = {
                    MockData.currentDonor = donor.copy(
                        name = fullName,
                        email = email,
                        phone = phone,
                        city = city,
                        bloodGroup = bloodGroup,
                        avatarUrl = avatarUrl
                    )
                    profileSaved = true
                },
                enabled = fullName.isNotBlank() && email.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Save changes")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SuccessBanner(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.width(10.dp))
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
