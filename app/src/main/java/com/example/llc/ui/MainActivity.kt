package com.example.llc.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.applyCanvas
import com.example.llc.R
import com.example.llc.application.PointsEffect
import com.example.llc.application.PointsEvent
import com.example.llc.application.PointsState
import com.example.llc.application.PointsViewModel
import com.example.llc.ui.compose.InitContent
import com.example.llc.ui.compose.PointsContent
import com.example.llc.ui.theme.LLCTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class MainActivity : ComponentActivity() {

    private val viewModel: PointsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = viewModel.state.collectAsState().value
            LLCTheme {
                when (state) {
                    is PointsState.InitContent ->
                        InitContent(
                            count = state.count,
                            buttonLoading = state.buttonLoading,
                            onCountChange = { viewModel.sendEvent(PointsEvent.Ui.InputCount(it)) },
                            onGoClick = { viewModel.sendEvent(PointsEvent.Ui.GoClick) }
                        )
                    is PointsState.PointsContent ->
                        PointsContent(
                            points = state.points,
                            unitState = state.unitPoint,
                            zoomInClick = { viewModel.sendEvent(PointsEvent.Ui.ZoomIn) },
                            zoomOutClick = { viewModel.sendEvent(PointsEvent.Ui.ZoomOut) },
                            saveClick = { viewModel.sendEvent(PointsEvent.Ui.SaveClick) }
                        )
                }
            }

            val scope = rememberCoroutineScope()
            val view = LocalView.current
            LaunchedEffect(Unit) {
                scope.launch {
                    viewModel.effect
                        .filterIsInstance<PointsEffect.Ui>()
                        .collect {
                            when (it) {
                                is PointsEffect.Ui.ShowErrorToast -> {
                                    Toast.makeText(
                                        this@MainActivity,
                                        R.string.error_message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                is PointsEffect.Ui.SaveFile -> {
                                    saveFile(view)
                                    Toast.makeText(
                                        this@MainActivity,
                                        R.string.file_saved,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                }
            }
        }
    }

    private suspend fun saveFile(view: View) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            .applyCanvas { view.draw(this) }
        withContext(Dispatchers.IO) {
            val file = File(cacheDir, "screenshot.png")
            file.outputStream()
                .use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    out.flush()
                }
        }
    }
}