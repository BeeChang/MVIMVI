package example.beechang.mvimvi.eventIntent

import example.beechang.mvimvi.data.Fruit

interface Event

sealed interface MainEvent : Event {
    data class Count(val count: Int) : MainEvent
    data class GetFruit(val fruit: Fruit) : MainEvent
    data class Toast(val msg: String) : MainEvent
}