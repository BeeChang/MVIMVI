package example.beechang.mvimvi.eventIntent

interface Event

sealed interface MainEvent : Event {
    class Count(val count: Int) : MainEvent
    object GetFruit : MainEvent
    class Toast(val msg: String) : MainEvent
}