package com.viewflow.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.viewflow.app.R
import com.viewflow.app.data.BloodCamp
import com.viewflow.app.data.MockData
import kotlinx.coroutines.delay

private enum class DonateStep { SELECT_CAMP, HEALTH_CHECK, CONFIRMING, SUCCESS }

private val healthQuestions = listOf(
    "I have not donated blood in the last 3 months",
    "I have not had a major illness or surgery recently",
    "I am not currently taking antibiotics",
    "I have eaten a meal in the last few hours"
)

@Composable
fun DonateScreen(
    preselectedCampId: String? = null,
    onDonationComplete: () -> Unit
) {
    var step by remember { mutableStateOf(DonateStep.SELECT_CAMP) }
    var selectedCamp by remember(preselectedCampId) {
        mutableStateOf(MockData.campById(preselectedCampId ?: MockData.camps.first().id) ?: MockData.camps.first())
    }
    val checkedStates = remember { mutableStateOf(List(healthQuestions.size) { false }) }

    LaunchedEffect(step) {
        if (step == DonateStep.CONFIRMING) {
            delay(1600)
            step = DonateStep.SUCCESS
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(text = "Donate blood", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (step) {
                    DonateStep.SELECT_CAMP -> "Step 1 of 2 · Choose a camp"
                    DonateStep.HEALTH_CHECK -> "Step 2 of 2 · Health checklist"
                    DonateStep.CONFIRMING -> "Confirming your slot…"
                    DonateStep.SUCCESS -> "You're all set!"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnimatedContent(
            targetState = step,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                (fadeIn(tween(280)) togetherWith fadeOut(tween(200)))
            },
            label = "donateStep"
        ) { targetStep ->
            when (targetStep) {
                DonateStep.SELECT_CAMP -> SelectCampStep(
                    selectedCamp = selectedCamp,
                    onSelectCamp = { selectedCamp = it },
                    onNext = { step = DonateStep.HEALTH_CHECK }
                )

                DonateStep.HEALTH_CHECK -> HealthCheckStep(
                    checked = checkedStates.value,
                    onCheckedChange = { index, value ->
                        checkedStates.value = checkedStates.value.toMutableList().also { it[index] = value }
                    },
                    allChecked = checkedStates.value.all { it },
                    onConfirm = { step = DonateStep.CONFIRMING }
                )

                DonateStep.CONFIRMING -> ConfirmingStep()

                DonateStep.SUCCESS -> SuccessStep(
                    camp = selectedCamp,
                    onDone = onDonationComplete
                )
            }
        }
    }
}

@Composable
private fun SelectCampStep(
    selectedCamp: BloodCamp,
    onSelectCamp: (BloodCamp) -> Unit,
    onNext: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(MockData.camps, key = { it.id }) { camp ->
                val isSelected = camp.id == selectedCamp.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = isSelected, onClick = { onSelectCamp(camp) }),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = isSelected, onClick = { onSelectCamp(camp) })
                        Spacer(modifier = Modifier.width(4.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = camp.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "${camp.hospital} · ${camp.date}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun HealthCheckStep(
    checked: List<Boolean>,
    onCheckedChange: (Int, Boolean) -> Unit,
    allChecked: Boolean,
    onConfirm: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            healthQuestions.forEachIndexed { index, question ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked[index],
                        onCheckedChange = { onCheckedChange(index, it) }
                    )
                    Text(text = question, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Button(
            onClick = onConfirm,
            enabled = allChecked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Confirm donation")
        }
    }
}

@Composable
private fun ConfirmingStep() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.heartbeat_pulse)
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Crossfade(targetState = true, label = "confirmingLabel") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reserving your donation slot…", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun SuccessStep(camp: BloodCamp, onDone: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.success_check)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = 1
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Thank you for saving a life",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your donation slot at ${camp.name} on ${camp.date} is confirmed.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
                Text("Back to home")
            }
        }
    }
}
