package example.beechang.mvimvi.sideEffect

import example.beechang.mvimvi.eventIntent.Event
import example.beechang.mvimvi.eventIntent.MainEvent
import example.beechang.mvimvi.model.MainState

interface Effect

sealed interface MainEffect : Effect {
    object Fail : MainEffect
    class Toast(val msg : String ):MainEffect
}

