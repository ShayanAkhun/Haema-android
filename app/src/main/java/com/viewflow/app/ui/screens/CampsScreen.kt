package com.viewflow.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.viewflow.app.data.MockData
import com.viewflow.app.ui.components.CampCard

@Composable
fun CampsScreen(onOpenCamp: (String) -> Unit) {
    var expandedCampId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(text = "Blood camps", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Tap a camp to see more, or view full details",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        items(MockData.camps, key = { it.id }) { camp ->
            CampCard(
                camp = camp,
                expanded = expandedCampId == camp.id,
                onToggleExpand = {
                    expandedCampId = if (expandedCampId == camp.id) null else camp.id
                },
                onViewDetails = { onOpenCamp(camp.id) }
            )
        }
    }
}
