package example.beechang.mvimvi.model

import example.beechang.mvimvi.data.Fruit

interface State

data class MainState(
    val fruit: Fruit = Fruit(name = "Apple", color = "Red", size = "Middle"),
    val count: Int = 0
) : State