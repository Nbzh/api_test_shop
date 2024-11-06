package bzh.nv.melishop_api

data class ArticleResponse(
    var id: String,
    val category: Category,
    val name: String,
    val image: String,
    val description: String,
    val isVeggies: Boolean,
    val price: Double,
    val priceUnit: String,
    val labels: List<Label>
)

data class Label(val id: String, val name: String, val image: String)

data class Category(val id: String, val name: String, val image: String, val color: String)

data class Article(
    var id: String,
    val categoryId: String,
    val name: String,
    val image: String,
    val description: String,
    val isVeggies: Boolean,
    val price: Double,
    val priceUnit: String
)

data class ArticleParams(
    var id: String,
    val categoryId: String,
    val name: String,
    val image: String,
    val description: String,
    val labelIds : List<String>,
    val isVeggies: Boolean,
    val price: Double,
    val priceUnit: String
)

data class ArticleLabel(val articleId: String, val labelId: String)