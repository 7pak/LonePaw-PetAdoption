package com.abdts.petadoption

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.abdts.petadoption.ui.theme.PetAdoptionTheme
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.core.common.utls.UserVerificationModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    @Inject
    lateinit var userVerificationModel: UserVerificationModel

    private var isOnboardingViewed by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {

            isOnboardingViewed = userVerificationModel.isOnboardingViewed.firstOrNull() ?: false

            setContent {

                var isSplashScreenFinished by remember {
                    mutableStateOf(false)
                }

                PetAdoptionTheme {
                    SplashScreen()
                    LaunchedEffect(key1 = true) {
                        delay(3000)
                        isSplashScreenFinished = true
                    }
                    if (isSplashScreenFinished) {
                        if (isOnboardingViewed) {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish() // Close SplashActivity
                        } else {
                            OnboardingScreen {
                                userVerificationModel.changeOnboardingStatus(true)
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    val animatedSize = remember { Animatable(220f) }



    LaunchedEffect(animatedSize) {
        animatedSize.animateTo(
            targetValue = 350f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            )
        )

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_image),
            contentDescription = "splash_screen",
            modifier = Modifier.size(animatedSize.value.dp)
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(context: Context = LocalContext.current, onclick: () -> Unit) {


    val onBoardingDetails = listOf(
        OnboardingPages(
            R.drawable.ic_pet,
            "Welcome to LonePaw!",
            context.getString(R.string.first_page)
        ),
        OnboardingPages(
            R.drawable.ic_pet,
            " Find Your Perfect Pet",
            context.getString(R.string.second_page)
        ),
        OnboardingPages(
            R.drawable.ic_pet,
            "Connect Through Chat",
            context.getString(R.string.third_page)
        )
    )

    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.background_post),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.background)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) { index ->
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context = context)
                        .data(
                            onBoardingDetails[index].image
                        )
                        .crossfade(500)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_pet)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {

                    Text(
                        text = onBoardingDetails[index].title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Beige,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Image(
                        painter = painter,
                        contentDescription = "pet detail image",
                        modifier = Modifier
                            .fillMaxWidth(0.7f),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = onBoardingDetails[index].desc,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Normal
                        ), textAlign = TextAlign.Center
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(5.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == iteration)
                                            MaterialTheme.colorScheme.tertiary
                                        else
                                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (pagerState.currentPage == pagerState.pageCount - 1) {
                                onclick()
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(
                                        pagerState.currentPage + 1, // Scroll to the next page
                                        animationSpec = tween(durationMillis = 500), // Specify animation duration
                                        pageOffsetFraction = 0f // Set page offset fraction
                                    )
                                }
                            }
                        },
                        modifier = Modifier.width(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = Beige
                        )
                    ) {
                        Text(text = if (pagerState.currentPage == pagerState.pageCount - 1) "Start" else "Next")
                    }
                }
            }

        }
    }
}

data class OnboardingPages(
    val image: Int,
    val title: String,
    val desc: String
)