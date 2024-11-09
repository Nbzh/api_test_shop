package bzh.nv.melishop_api.data

import bzh.nv.melishop_api.formatToTwoDecimalPlaces
import java.util.*
import kotlin.random.Random

val fakeLabels
    get() =
        listOf(
            Label(
                id = "label_fish",
                name = "Fish",
                image = "https://th.bing.com/th/id/OIP.N-qXcubQhRqU1O0kvRdG4QHaHa?w=178&h=180&c=7&r=0&o=5&dpr=2&pid=1.7"
            ),
            Label(
                id = "label_peanut",
                name = "Peanut",
                image = "https://th.bing.com/th/id/OIP.N-qXcubQhRqU1O0kvRdG4QHaHa?w=178&h=180&c=7&r=0&o=5&dpr=2&pid=1.7"
            ),
            Label(
                id = "label_meat",
                name = "Meat",
                image = "https://th.bing.com/th/id/OIP.N-qXcubQhRqU1O0kvRdG4QHaHa?w=178&h=180&c=7&r=0&o=5&dpr=2&pid=1.7"
            ),
            Label(
                id = "label_gluten_free",
                name = "Gluten Free",
                image = "https://th.bing.com/th/id/OIP.N-qXcubQhRqU1O0kvRdG4QHaHa?w=178&h=180&c=7&r=0&o=5&dpr=2&pid=1.7"
            ),
            Label(
                id = "label_healthy",
                name = "Healthy",
                image = "https://th.bing.com/th/id/OIP.N-qXcubQhRqU1O0kvRdG4QHaHa?w=178&h=180&c=7&r=0&o=5&dpr=2&pid=1.7"
            ),
            Label(
                id = "label_cheese",
                name = "Cheese",
                image = "https://th.bing.com/th/id/OIP.N-qXcubQhRqU1O0kvRdG4QHaHa?w=178&h=180&c=7&r=0&o=5&dpr=2&pid=1.7"
            ),
            Label(
                id = "label_egg",
                name = "Egg",
                image = "https://th.bing.com/th/id/OIP.N-qXcubQhRqU1O0kvRdG4QHaHa?w=178&h=180&c=7&r=0&o=5&dpr=2&pid=1.7"
            )
        )

val fakeCategories
    get() =
        listOf(
            Category(
                id = "category_italian",
                name = "Italian",
                color = "008000",
                image = "https://picsum.photos/32/32"
            ),
            Category(
                id = "category_chinese",
                name = "Chinese",
                color = "FFFF00",
                image = "https://picsum.photos/32/32"
            ),
            Category(
                id = "category_france",
                name = "French",
                color = "0074D9",
                image = "https://picsum.photos/32/32"
            ),
            Category(
                id = "category_argentina",
                name = "Argentina",
                color = "00BFFF",
                image = "https://picsum.photos/32/32"
            ),
            Category(
                id = "category_indonesia",
                name = "Indonesia",
                color = "FF0000",
                image = "https://picsum.photos/32/32"
            ),
        )

object FoodNameGenerator {
    private val adjectives = listOf(
        "Crispy",
        "Spicy",
        "Sweet",
        "Savory",
        "Tangy",
        "Cheesy",
        "Creamy",
        "Golden",
        "Fluffy",
        "Zesty"
    )
    private val foodNouns = listOf(
        "Pizza",
        "Burger",
        "Taco",
        "Pasta",
        "Salad",
        "Soup",
        "Sandwich",
        "Pancake",
        "Waffle",
        "Noodle"
    )

    fun generateRandomFoodName(): String {
        val randomAdjective = adjectives.random()
        val randomFoodNoun = foodNouns.random()
        return "$randomAdjective $randomFoodNoun"
    }
}

object FoodDescriptionGenerator {
    private val phrases = listOf(
        "A delightful blend of",
        "A symphony of flavors featuring",
        "A tantalizing explosion of",
        "A culinary masterpiece with",
        "A harmonious fusion of",
        "An irresistible combination of",
        "A classic dish with a modern twist,",
        "A taste of heaven with",
        "A culinary adventure featuring",
        "A feast for the senses with"
    )
    private val flavorProfiles = listOf(
        "sweet and savory notes",
        "spicy and tangy undertones",
        "rich and creamy textures",
        "fresh and vibrant flavors",
        "exotic and aromatic spices",
        "comforting and familiar tastes",
        "bold and robust flavors",
        "delicate and nuanced aromas",
        "smoky and earthy hints",
        "zesty and refreshing citrus"
    )

    fun generateRandomFoodDescription(): String {
        val randomPhrase = phrases.random()
        val randomFlavorProfile = flavorProfiles.random()
        return "$randomPhrase $randomFlavorProfile."
    }
}

fun fakeArticle(id: String = UUID.randomUUID().toString()): ArticleResponse {
    val category = fakeCategories.shuffled().first()
    val labels = fakeLabels.shuffled().take(Random.nextInt(1, fakeLabels.size))
    return ArticleResponse(
        id = id,
        category = category,
        name = FoodNameGenerator.generateRandomFoodName(),
        description = FoodDescriptionGenerator.generateRandomFoodDescription(),
        image = "https://picsum.photos/600/338",
        labels = labels,
        isVeggies = Random.nextBoolean(),
        price = Random.nextDouble(until = 34.99).formatToTwoDecimalPlaces().toDouble(),
        priceUnit = listOf("pièce", "plat", "lot").shuffled().first()
    )
}