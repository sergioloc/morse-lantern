package com.slc.morse

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slc.morse.ui.theme.MorseLanternTheme

class MainActivity : ComponentActivity() {

    private lateinit var cameraManager: CameraManager
    private var lanternOn = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MorseLanternTheme {
                BodyContent(modifier = Modifier.clickable {
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, !lanternOn)
                    lanternOn = !lanternOn;
                })
            }
        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}

@Composable
fun BodyContent(modifier: Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MorseLanternTheme {
        //BodyContent()
    }
}