package com.viewflow.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewflow.app.R
import com.viewflow.app.ui.components.AuthHeroBackground
import com.viewflow.app.ui.components.AuthTextField
import com.viewflow.app.ui.components.HaemaWordmark

@Composable
fun SignupScreen(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    var showEmailForm by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var showEmailSentDialog by remember { mutableStateOf(false) }

    // Gesture / system back collapses the email sub-form before leaving the screen.
    BackHandler(enabled = showEmailForm) { showEmailForm = false }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Illustration takes whatever vertical space is left after the form, so the
        // whole screen stays fixed (no scrolling) on any device.
        AuthHeroBackground(
            rawResId = R.raw.signup,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HaemaWordmark(fontSize = 28.sp)
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Join Haema",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Create an account to start donating",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))

            AnimatedContent(targetState = showEmailForm, label = "signup-method") { emailFormVisible ->
                if (!emailFormVisible) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedButton(
                            onClick = onSignupClick,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFFDADCE0)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF1F1F1F)
                            ),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            GoogleGlyph()
                            Spacer(Modifier.width(12.dp))
                            Text("Continue with Google", fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = onSignupClick,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            AppleGlyph()
                            Spacer(Modifier.width(12.dp))
                            Text("Continue with Apple", fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { showEmailForm = true },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            Icon(Icons.Filled.Email, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(12.dp))
                            Text("Continue with Email", fontWeight = FontWeight.SemiBold)
                        }
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "We'll email you a link to finish creating your account.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(16.dp))
                        AuthTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            leadingIcon = Icons.Filled.Email
                        )

                        Spacer(Modifier.height(20.dp))
                        Button(
                            onClick = { showEmailSentDialog = true },
                            enabled = email.isNotBlank() && email.contains("@"),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Continue")
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onLoginClick) {
                    Text("Log in", fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showEmailSentDialog) {
        AlertDialog(
            onDismissRequest = { showEmailSentDialog = false },
            confirmButton = {
                TextButton(onClick = { showEmailSentDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Check your email") },
            text = { Text("Please follow the link sent to your email for account creation.") }
        )
    }
}

@Composable
private fun GoogleGlyph() {
    Box(
        modifier = Modifier.size(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "G",
            color = Color(0xFF4285F4),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

/**
 * A simplified Apple logo: two overlapping circles unioned into a rounded
 * apple body, a circular bite subtracted from the top right, plus a stem
 * and leaf. Built from boolean path ops rather than freehand curves so the
 * silhouette stays proportionate and recognizable at small sizes.
 */
@Composable
private fun AppleGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height

        val leftLobe = Path().apply {
            addOval(Rect(offset = Offset(w * 0.04f, h * 0.32f), size = Size(w * 0.58f, h * 0.60f)))
        }
        val rightLobe = Path().apply {
            addOval(Rect(offset = Offset(w * 0.40f, h * 0.30f), size = Size(w * 0.56f, h * 0.60f)))
        }
        val body = Path().apply { op(leftLobe, rightLobe, PathOperation.Union) }

        val bite = Path().apply {
            addOval(Rect(offset = Offset(w * 0.66f, h * 0.36f), size = Size(w * 0.26f, h * 0.26f)))
        }
        val bodyWithBite = Path().apply { op(body, bite, PathOperation.Difference) }

        drawPath(bodyWithBite, color = Color.White)

        drawLine(
            color = Color.White,
            start = Offset(w * 0.50f, h * 0.32f),
            end = Offset(w * 0.50f, h * 0.14f),
            strokeWidth = w * 0.07f,
            cap = StrokeCap.Round
        )

        val leaf = Path().apply {
            moveTo(w * 0.50f, h * 0.20f)
            quadraticTo(w * 0.80f, h * 0.02f, w * 0.82f, h * 0.22f)
            quadraticTo(w * 0.64f, h * 0.28f, w * 0.50f, h * 0.20f)
            close()
        }
        drawPath(leaf, color = Color.White)
    }
}
