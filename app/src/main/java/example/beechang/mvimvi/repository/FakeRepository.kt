package example.beechang.mvimvi.repository

import example.beechang.mvimvi.data.Fruit
import kotlinx.coroutines.delay

class GetFruitRepository {

    private val fruits = mutableListOf(
        Fruit(name = "Apple", color = "Red", size = "Middle"),
        Fruit(name = "Pear", color = "Green", size = "Middle"),
        Fruit(name = "Strawberry", color = "Red", size = "Small"),
        Fruit(name = "Cherry", color = "Red", size = "Small"),
        Fruit(name = "Grape", color = "Purple", size = "Small"),
        Fruit(name = "Orange", color = "Orange", size = "Middle"),
        Fruit(name = "Banana", color = "Yellow", size = "Middle"),
        Fruit(name = "Watermelon", color = "Green", size = "Big"),
        Fruit(name = "Peach", color = "Orange", size = "Middle"),
        Fruit(name = "Kiwi", color = "Brown", size = "Small")
    )

    suspend fun getData(): Fruit {
        delay(500)
        fruits.shuffle()
        return fruits[0]
    }
}