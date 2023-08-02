package example.beechang.mvimvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.beechang.mvimvi.eventIntent.MainEvent
import example.beechang.mvimvi.model.MainState
import example.beechang.mvimvi.repository.GetFruitRepository
import example.beechang.mvimvi.sideEffect.Effect
import example.beechang.mvimvi.sideEffect.MainEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainViewModel(
    private val repository: GetFruitRepository = GetFruitRepository()
) : ViewModel() {

    private val mainEvent = MutableSharedFlow<MainEvent>()

     val mainState : StateFlow<MainState> = mainEvent
        .transform { event -> handleEvent(event, this) }
        .scan(MainState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MainState())

    private val _sideEffects = Channel<MainEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    suspend fun intent(event: MainEvent) = mainEvent.emit(event)

    private suspend fun handleEvent(event: MainEvent, collector: FlowCollector<MainEvent>) {
        when (event) {
            is MainEvent.Toast -> {
                Log.e("toast" , "toast")
                _sideEffects.send(MainEffect.Toast("toast start"))
            }

            is MainEvent.Count,  is MainEvent.GetFruit -> {
                collector.emit(event)
            }
        }
    }

    private suspend fun reduce(current: MainState, event: MainEvent): MainState {
        return when (event) {
            is MainEvent.Count -> current.copy(count = current.count + 1) // count ++
            is MainEvent.GetFruit -> {
                if (Random.nextFloat() <= 0.7) { // 70% error or success
                    current.copy(fruit = repository.getData())
                } else { // fail go sideeffect
                    Log.e("toast" , "toast")
                    _sideEffects.send(MainEffect.Fail)
                    current
                }
            }
            else -> {
                current
            }
        }
    }

}

