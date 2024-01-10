package com.slc.morse.ui.lantern

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.slc.morse.R
import com.slc.morse.ui.theme.MorseLanternTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LanternActivity: ComponentActivity() {

    private lateinit var viewModel: LanternViewModel
    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MorseLanternTheme {
                Scaffold(
                ) { innerPadding ->
                    BodyContent(viewModel, cameraManager, modifier = Modifier.padding(innerPadding))
                }
            }
        }
        viewModel = LanternViewModel()
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}

@Composable
fun BodyContent(viewModel: LanternViewModel, cameraManager: CameraManager, modifier: Modifier) {
    //val lanternOn: Boolean by viewModel.lanternOn.observeAsState(initial = false)
    val code: String by viewModel.code.observeAsState(initial = "")
    var text by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var currentProgress by remember { mutableStateOf(0f) }

    ConstraintLayout(modifier.fillMaxSize()) {
        val (textTitle, dotLottie, textCode, progressIndicator, editText, btnStart) = createRefs()

        Text(
            text = "",
            style = TextStyle(color = Color.White, fontSize = 30.sp),
            modifier = Modifier.constrainAs(textTitle) {
                top.linkTo(parent.top, margin = 20.dp)
                start.linkTo(parent.start, margin = 20.dp)
                end.linkTo(parent.end, margin = 20.dp)
            }
        )

        Text(
            text = code,
            style = TextStyle(color = Color.White, fontSize = 50.sp),
            modifier = Modifier.constrainAs(textCode) {
                top.linkTo(textTitle.bottom)
                bottom.linkTo(btnStart.top)
                start.linkTo(parent.start, margin = 20.dp)
                end.linkTo(parent.end, margin = 20.dp)
            }
        )

        DotLottie(
            modifier = Modifier.constrainAs(dotLottie) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        ActionButton(modifier = modifier.constrainAs(btnStart) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            viewModel.start(text, cameraManager)
            loading = true
            scope.launch {
                loadProgress { progress ->
                    currentProgress = progress
                }
                loading = false // Reset loading when the coroutine finishes
            }
        }

        LinearDeterminateIndicator(
            loading = loading,
            currentProgress = currentProgress,
            modifier = modifier.padding(horizontal = 20.dp).constrainAs(progressIndicator) {
                bottom.linkTo(editText.top, margin = 10.dp)
                start.linkTo(parent.start,)
                end.linkTo(parent.end)
            }
        )

        TextField(
            value = text,
            placeholder = { Text(text = "")},
            singleLine = false,
            maxLines = 5,
            onValueChange = {
                text = it
            },
            modifier = Modifier
                .fillMaxWidth().height(200.dp).padding(all = 20.dp)
                .constrainAs(editText) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )
    }
}

@Composable
fun ActionButton(modifier: Modifier, onPressed:() -> Unit) {
    Surface(
        color = Color.White,
        shape = CircleShape,
        modifier = modifier
            .width(75.dp)
            .height(75.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onPressed()
            }
    ) {
        Icon(Icons.Default.PlayArrow, contentDescription = "play", tint = Color.Black, modifier = Modifier.padding(20.dp))
    }
}

@Composable
fun DotLottie(modifier: Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.radar_white
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier.scale(0.5f)
    )
}

@Composable
fun LinearDeterminateIndicator(modifier: Modifier, loading: Boolean, currentProgress: Float) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                progress = currentProgress,
                color = Color.White,
            )
        }
    }
}


/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LanternActivity()
}