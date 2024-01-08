package com.slc.morse.ui.lantern

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.slc.morse.ui.theme.MorseLanternTheme

class LanternActivity: ComponentActivity() {

    private lateinit var viewModel: LanternViewModel
    private lateinit var cameraManager: CameraManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MorseLanternTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Lantern")
                            }
                        )
                    },
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
    val lanternOn: Boolean by viewModel.lanternOn.observeAsState(initial = false)
    var text by remember { mutableStateOf("") }

    ConstraintLayout(modifier.fillMaxSize()) {
        val (editText, btnStart) = createRefs()

        TextField(
            value = text,
            placeholder = { Text(text = "")},
            singleLine = false,
            maxLines = 5,
            onValueChange = {
                text = it
            },
            modifier = Modifier.fillMaxWidth().constrainAs(editText){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, margin = 20.dp)
                end.linkTo(parent.end, margin = 20.dp)
            },
            )

        ActionButton(modifier = modifier.constrainAs(btnStart) {
            bottom.linkTo(parent.bottom, margin = 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            viewModel.start(text, lanternOn, cameraManager)
        }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LanternActivity()
}