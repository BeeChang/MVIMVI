package example.beechang.mvimvi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import example.beechang.mvimvi.sideEffect.MainEffect
import example.beechang.mvimvi.ui.theme.MVIMVITheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.sideEffects.collect { effect ->
                when (effect) {
                    is MainEffect.Toast ->
                        Toast.makeText(this@MainActivity, effect.msg, Toast.LENGTH_SHORT).show()
                    is MainEffect.Fail -> {
                        Toast.makeText(this@MainActivity, "Fruit Fail", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        setContent {
            MainScreen()
        }
    }
}

