package com.viewflow.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.viewflow.app.data.MockData
import com.viewflow.app.ui.components.AuthTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySecurityScreen(onBack: () -> Unit) {
    val donor = MockData.currentDonor

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordUpdated by remember { mutableStateOf(false) }

    var twoFactorEnabled by remember { mutableStateOf(donor.twoFactorEnabled) }
    var biometricLoginEnabled by remember { mutableStateOf(false) }
    var loginAlertsEnabled by remember { mutableStateOf(true) }
    var profileVisibleToDonors by remember { mutableStateOf(true) }
    var shareLocationInEmergencies by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy & security") },
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
            Text(text = "Password", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Change your password regularly to keep your account secure",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(14.dp))

            AuthTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = "Current password",
                leadingIcon = Icons.Filled.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible }
            )
            Spacer(Modifier.height(14.dp))
            AuthTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New password",
                leadingIcon = Icons.Filled.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible }
            )
            Spacer(Modifier.height(14.dp))
            AuthTextField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it },
                label = "Confirm new password",
                leadingIcon = Icons.Filled.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                isError = confirmNewPassword.isNotBlank() && confirmNewPassword != newPassword,
                supportingText = if (confirmNewPassword.isNotBlank() && confirmNewPassword != newPassword) {
                    "Passwords don't match"
                } else null
            )

            Spacer(Modifier.height(16.dp))
            AnimatedVisibility(
                visible = passwordUpdated,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                SuccessBanner(text = "Password updated successfully.")
            }
            Button(
                onClick = {
                    passwordUpdated = true
                    currentPassword = ""
                    newPassword = ""
                    confirmNewPassword = ""
                },
                enabled = currentPassword.isNotBlank() &&
                    newPassword.length >= 6 &&
                    newPassword == confirmNewPassword,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Update password")
            }

            Spacer(Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(Modifier.height(24.dp))

            Text(text = "Account security", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column {
                    ToggleRow(
                        icon = Icons.Filled.Security,
                        title = "Two-factor authentication",
                        subtitle = if (twoFactorEnabled) "Enabled · codes sent to ${donor.phone}"
                        else "Add an extra layer of security with SMS codes",
                        checked = twoFactorEnabled,
                        onCheckedChange = {
                            twoFactorEnabled = it
                            MockData.currentDonor = MockData.currentDonor.copy(twoFactorEnabled = it)
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    ToggleRow(
                        icon = Icons.Filled.Fingerprint,
                        title = "Biometric login",
                        subtitle = "Use fingerprint or face unlock to sign in",
                        checked = biometricLoginEnabled,
                        onCheckedChange = { biometricLoginEnabled = it }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    ToggleRow(
                        icon = Icons.Filled.NotificationsActive,
                        title = "Login alerts",
                        subtitle = "Get notified when your account is accessed from a new device",
                        checked = loginAlertsEnabled,
                        onCheckedChange = { loginAlertsEnabled = it }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    ActionRow(
                        icon = Icons.Filled.Devices,
                        title = "Active sessions",
                        subtitle = "Manage devices signed in to your account"
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(text = "Privacy", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column {
                    ToggleRow(
                        icon = Icons.Filled.Visibility,
                        title = "Profile visibility",
                        subtitle = "Show your profile to nearby donors and hospitals",
                        checked = profileVisibleToDonors,
                        onCheckedChange = { profileVisibleToDonors = it }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    ToggleRow(
                        icon = Icons.Filled.LocationOn,
                        title = "Location sharing",
                        subtitle = "Share your live location during active emergency requests",
                        checked = shareLocationInEmergencies,
                        onCheckedChange = { shareLocationInEmergencies = it }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    ActionRow(
                        icon = Icons.Filled.Block,
                        title = "Blocked donors",
                        subtitle = "Manage donors and requesters you've blocked"
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(text = "Your data", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column {
                    ActionRow(
                        icon = Icons.Filled.Download,
                        title = "Download my data",
                        subtitle = "Get a copy of your donation and account history"
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    ActionRow(
                        icon = Icons.Filled.DeleteForever,
                        title = "Delete account",
                        subtitle = "Permanently remove your account and data",
                        destructive = true
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun ActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    destructive: Boolean = false,
    onClick: () -> Unit = {}
) {
    val accent = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = if (destructive) accent else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
