package com.viewflow.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.viewflow.app.R
import com.viewflow.app.ui.theme.BloodRed
import com.viewflow.app.ui.theme.ViewFlowTheme

/** Copy + artwork for one onboarding page. */
data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

/** The three onboarding pages, in order. The last one is the call-to-action page. */
val onboardingPages = listOf(
    OnboardingPage(
        imageRes = R.drawable.first,
        title = "Why Donate",
        description = "Donating blood is a simple act that can save multiple lives every day. " +
            "It's safe, quick, and gives hope to patients in urgent need. Be a hero"
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding_second,
        title = "Why Blood Needs?",
        description = "Blood is essential for life, and many people need it every day. " +
            "Accidents, surgeries, childbirth complications, and medical conditions like " +
            "anemia or cancer often require blood transfusions."
    ),
    OnboardingPage(
        imageRes = R.drawable.third,
        title = "Blood Donation Process",
        description = "The blood donation process is simple, safe, and quick. First, donors " +
            "register and undergo a brief health screening to ensure they are eligible to give blood."
    )
)

/**
 * Stateful onboarding flow: steps through [onboardingPages] with Next/Skip.
 * The final page swaps the Next/Skip controls for the call-to-action: [onGetStarted]
 * (Start Saving Lives) and [onLogin] (Log In). Skipping earlier pages also goes to [onLogin].
 */
@Composable
fun OnboardingFlow(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = remember { onboardingPages }
    var pageIndex by rememberSaveable { mutableIntStateOf(0) }
    val page = pages[pageIndex]

    if (pageIndex == pages.lastIndex) {
        OnboardingFinalScreen(
            page = page,
            pageCount = pages.size,
            currentPage = pageIndex,
            onGetStarted = onGetStarted,
            onLogin = onLogin,
            modifier = modifier
        )
    } else {
        OnboardingScreen(
            imageRes = page.imageRes,
            title = page.title,
            description = page.description,
            pageCount = pages.size,
            currentPage = pageIndex,
            onSkip = onLogin,
            onNext = { pageIndex++ },
            modifier = modifier
        )
    }
}

/**
 * A single page of the onboarding flow. Reused for every page except the last —
 * only the image/copy and page position change; [onSkip]/[onNext] wire up navigation.
 */
@Composable
fun OnboardingScreen(
    imageRes: Int,
    title: String,
    description: String,
    pageCount: Int,
    currentPage: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingContent(
            imageRes = imageRes,
            title = title,
            description = description,
            pageCount = pageCount,
            currentPage = currentPage,
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onSkip) {
                Text(
                    text = "Skip",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(onClick = onNext) {
                Text(
                    text = "Next",
                    color = BloodRed,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * The last onboarding page: same artwork/copy layout, but the bottom controls
 * become the primary call-to-action. [onGetStarted] leads to sign-up, [onLogin]
 * to the login screen.
 */
@Composable
fun OnboardingFinalScreen(
    page: OnboardingPage,
    pageCount: Int,
    currentPage: Int,
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingContent(
            imageRes = page.imageRes,
            title = page.title,
            description = page.description,
            pageCount = pageCount,
            currentPage = currentPage,
            modifier = Modifier.weight(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onGetStarted,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Start Saving Lives", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onLogin) {
                    Text("Log In", color = BloodRed, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun OnboardingContent(
    imageRes: Int,
    title: String,
    description: String,
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Scales every illustration up to the same footprint, so small and large
        // source images read at a consistent size across pages.
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        PageIndicator(pageCount = pageCount, currentPage = currentPage)
    }
}

@Composable
private fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            Box(
                modifier = Modifier
                    .size(if (isActive) 8.dp else 6.dp)
                    .clip(CircleShape)
                    .then(
                        Modifier.background(
                            if (isActive) BloodRed else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    ViewFlowTheme {
        Surface {
            OnboardingScreen(
                imageRes = R.drawable.first,
                title = "Why Donate",
                description = "Donating blood is a simple act that can save multiple lives every day. It's safe, quick, and gives hope to patients in urgent need. Be a hero",
                pageCount = 3,
                currentPage = 0,
                onSkip = {},
                onNext = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingFinalScreenPreview() {
    ViewFlowTheme {
        Surface {
            OnboardingFinalScreen(
                page = onboardingPages.last(),
                pageCount = 3,
                currentPage = 2,
                onGetStarted = {},
                onLogin = {}
            )
        }
    }
}
