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
import kotlinx.coroutines.flow.map
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

    val mainState: StateFlow<MainState> = mainEvent
        .transform { event -> handleEvent(event, this) } //사이드이팩트 확인
        .map { event -> processBusinessLogic(event) }  // 비즈니스 로직 처리
        .scan(MainState(), ::reduce) // 상태생성
        .stateIn(viewModelScope, SharingStarted.Eagerly, MainState())

    private val _sideEffects = Channel<MainEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    suspend fun intent(event: MainEvent) = mainEvent.emit(event) //1. 유저이벤트 트리거

    private suspend fun handleEvent(event: MainEvent, collector: FlowCollector<MainEvent>) {
        when (event) {
            is MainEvent.Toast -> { //사이드 이팩트인경우 사이드이팩트 스트림으로 처리
                _sideEffects.send(MainEffect.Toast("toast start"))
            }

            is MainEvent.Count, is MainEvent.GetFruit -> {
                collector.emit(event)
            }
        }
    }

    private suspend fun processBusinessLogic(event: MainEvent): MainEvent {
        return when (event) {
            is MainEvent.Count -> event.copy(count = event.count + 1)
            is MainEvent.GetFruit -> {
                if (Random.nextFloat() <= 0.7) {
                    event.copy(fruit = repository.getData())
                } else {
                    _sideEffects.send(MainEffect.Fail)
                    event
                }
            }
            else -> event
        }
    }

    private fun reduce(current: MainState, event: MainEvent): MainState {
        return when (event) {
            is MainEvent.Count -> current.copy(count = event.count)
            is MainEvent.GetFruit -> current.copy(fruit = event.fruit)
            else -> {
                current
            }
        }
    }
}

