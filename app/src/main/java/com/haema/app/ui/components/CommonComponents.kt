package com.haema.app.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.haema.app.data.BloodGroup
import com.haema.app.data.Urgency
import com.haema.app.ui.theme.BloodABNeg
import com.haema.app.ui.theme.BloodABPos
import com.haema.app.ui.theme.BloodANeg
import com.haema.app.ui.theme.BloodAPos
import com.haema.app.ui.theme.BloodBNeg
import com.haema.app.ui.theme.BloodBPos
import com.haema.app.ui.theme.BloodONeg
import com.haema.app.ui.theme.BloodOPos
import com.haema.app.ui.theme.DataMonoFont
import com.haema.app.ui.theme.UrgencyCritical
import com.haema.app.ui.theme.UrgencyLow
import com.haema.app.ui.theme.UrgencyMedium

/** Colour identity for each blood group; rarer groups get more distinctive hues. */
fun bloodGroupColor(bloodGroup: BloodGroup): Color = when (bloodGroup) {
    BloodGroup.O_POS -> BloodOPos
    BloodGroup.A_POS -> BloodAPos
    BloodGroup.B_POS -> BloodBPos
    BloodGroup.AB_POS -> BloodABPos
    BloodGroup.O_NEG -> BloodONeg
    BloodGroup.A_NEG -> BloodANeg
    BloodGroup.B_NEG -> BloodBNeg
    BloodGroup.AB_NEG -> BloodABNeg
}

@Composable
fun BloodGroupChip(bloodGroup: BloodGroup, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bloodGroupColor(bloodGroup))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = bloodGroup.label,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            fontFamily = DataMonoFont,
            fontWeight = FontWeight.Bold
        )
    }
}

fun urgencyColor(urgency: Urgency): Color = when (urgency) {
    Urgency.CRITICAL -> UrgencyCritical
    Urgency.MEDIUM -> UrgencyMedium
    Urgency.LOW -> UrgencyLow
}

@Composable
fun UrgencyChip(urgency: Urgency, modifier: Modifier = Modifier) {
    val color = urgencyColor(urgency)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = urgency.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * A pill-shaped segmented control: one track, [options] segments, the selected
 * one raised on a surface chip in the primary colour.
 */
@Composable
fun SegmentedToggle(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .clickable { onSelect(index) }
                    .padding(vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        if (actionLabel != null) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    accent: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = accent,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DonorAvatarStack(avatarUrls: List<String>, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        avatarUrls.take(4).forEachIndexed { index, url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier
                    .offset(x = (-8 * index).dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}

@Composable
fun CampBanner(
    bannerUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = bannerUrl,
        contentDescription = null,
        modifier = modifier.clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun ProgressRow(
    label: String,
    current: Int,
    goal: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.animateContentSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.labelMedium)
            Text(
                text = "$current / $goal units",
                style = MaterialTheme.typography.labelMedium,
                fontFamily = DataMonoFont,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { (current.toFloat() / goal).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
    }
}

@Composable
fun LocationRow(text: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
