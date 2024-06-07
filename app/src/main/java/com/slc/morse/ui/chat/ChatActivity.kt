package com.slc.morse.ui.chat

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.slc.morse.R
import com.slc.morse.ui.theme.MorseTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.slc.morse.domain.entities.Message
import kotlinx.coroutines.launch

class ChatActivity: ComponentActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var cameraManager: CameraManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MorseTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onSurface,
                            ),
                            title = {
                                Text("Morse Light", modifier = Modifier.padding(start = 10.dp))
                            }
                            /*navigationIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_morse),
                                    contentDescription = "topBarIcon",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(top = 5.dp, start = 10.dp)
                                )
                            }*/
                        )
                    },
                ) { innerPadding ->
                    BodyContent(viewModel, cameraManager, modifier = Modifier.padding(innerPadding))
                }
            }
        }
        viewModel = ChatViewModel()
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}

@Composable
fun BodyContent(viewModel: ChatViewModel, cameraManager: CameraManager, modifier: Modifier) {
    val lightOn: Boolean by viewModel.lightOn.observeAsState(initial = false)
    val countdown: Float by viewModel.countdown.observeAsState(initial = 0f)
    val loading: Boolean by viewModel.loading.observeAsState(initial = false)
    val messages: List<Message> by viewModel.messages.observeAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(modifier.fillMaxSize()) {
        val (chatList, progressBar, chatBox) = createRefs()

        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .constrainAs(chatList) {
                    top.linkTo(parent.top, margin = 20.dp)
                    bottom.linkTo(chatBox.top, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            items(items = messages) { message ->
                MessageItem(
                    message = message
                )
            }
        }

        LinearDeterminateIndicator(
            modifier = modifier
                .constrainAs(progressBar) {
                    bottom.linkTo(chatBox.top)
                    start.linkTo(parent.start, margin = 20.dp)
                    end.linkTo(parent.end, margin = 20.dp)
            },
            loading,
            countdown
        )

        ChatBox(
            lightOn = lightOn,
            onStartClickListener = {
                viewModel.start(it, cameraManager)
                coroutineScope.launch {
                    // Animate scroll to the 10th item
                    listState.animateScrollToItem(index = messages.size)
                }
            },
            onStopClickListener = {
                viewModel.stop()
            },
            modifier = modifier
                .fillMaxWidth()
                .constrainAs(chatBox) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(
    lightOn: Boolean,
    onStartClickListener: (String) -> Unit,
    onStopClickListener: () -> Unit,
    modifier: Modifier,
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    Row(modifier = modifier.padding(10.dp)) {
        TextField(
            value = chatBoxValue,
            onValueChange = { newText ->
                if (!lightOn)
                    chatBoxValue = newText
            },
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(28.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.White
            ),
            placeholder = {
                Text(text = "Type something")
            },
        )

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = {
                if (lightOn) {
                    onStopClickListener()
                }
                else {
                    val msg = chatBoxValue.text
                    if (msg.isBlank()) return@IconButton
                    onStartClickListener(chatBoxValue.text)
                    chatBoxValue = TextFieldValue("")
                }
            },
            modifier = Modifier
                .clip(CircleShape)
                .width(55.dp)
                .height(55.dp)
                .background(color = Color.White)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = when {
                    lightOn -> ImageVector.vectorResource(R.drawable.ic_stop)
                    else -> Icons.Filled.Send
                },
                contentDescription = "Send",
                tint = Color.DarkGray,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
            )
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalAlignment = when { // 2
            message.isMine -> Alignment.End
            else -> Alignment.Start
        },
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            shape = cardShapeFor(message),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = message.text,
                color = when {
                    message.isMine -> MaterialTheme.colorScheme.onPrimary
                    else -> MaterialTheme.colorScheme.onSecondary
                },
            )
        }
    }
}

@Composable
fun cardShapeFor(message: Message): RoundedCornerShape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        message.isMine -> roundedCorners.copy(bottomEnd = CornerSize(0))
        else -> roundedCorners.copy(bottomStart = CornerSize(0))
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
        modifier = modifier
            .width(100.dp)
            .height(100.dp)
    )
}

@Composable
fun LinearDeterminateIndicator(modifier: Modifier, loading: Boolean, currentProgress: Float) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
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