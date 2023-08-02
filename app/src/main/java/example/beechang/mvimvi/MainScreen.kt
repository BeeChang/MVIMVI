package example.beechang.mvimvi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import example.beechang.mvimvi.data.Fruit
import example.beechang.mvimvi.eventIntent.MainEvent
import example.beechang.mvimvi.ui.theme.MVIMVITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
) {
    MVIMVITheme {
        val coroutineScope: CoroutineScope = rememberCoroutineScope()
        val viewModel: MainViewModel = viewModel()
        val state = viewModel.mainState.collectAsState().value

        val count = state.count
        val fruit = state.fruit

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CountView(
                    viewModel = viewModel,
                    count = count,
                    coroutineScope = coroutineScope
                )

                Spacer(modifier = Modifier.height(52.dp))

                FruitView(
                    viewModel = viewModel,
                    fruit = fruit,
                    coroutineScope = coroutineScope
                )

                Spacer(modifier = Modifier.height(104.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        viewModel.intent(MainEvent.Toast("here view"))
                    }
                }) {
                    Text(text = "Make Toast")
                }
            }
        }
    }
}

@Composable
fun CountView(
    viewModel: MainViewModel,
    count: Int = 0,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    modifier: Modifier = Modifier,
) {

    Text(
        text = "count is $count"
    )

    Button(
        onClick = {
            coroutineScope.launch {
                viewModel.intent(MainEvent.Count(count))
            }
        }
    ) {
        Text(text = "COUNT UP")
    }

}

@Composable
fun FruitView(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    fruit: Fruit,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    CompositionLocalProvider(LocalContentColor provides Color.Red) {

        Text(
            text = "fruit name : ${fruit.name}"
        )
        Text(
            text = "fruit color : ${fruit.color}"
        )
        Text(
            text = "fruit size : ${fruit.size}"
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.intent(MainEvent.GetFruit(fruit))
                }
            },
        ) {
            Text(text = "Suffle Fruit")
        }

    }
}



