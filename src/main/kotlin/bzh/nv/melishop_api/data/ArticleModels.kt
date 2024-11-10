package bzh.nv.melishop_api.data

data class User(
    val id: String,
    val username: String,
    val password: String,
    val roles: String // Comma-separated roles
)

data class ArticleResponse(

    var id: String,
    val category: Category,
    val name: String,
    val image: String?,
    val description: String?,
    val isVeggies: Boolean,
    val price: Double,
    val priceUnit: String,
    val labels: List<Label>
)

data class Label(var id: String, val name: String, val image: String?)

data class Category(var id: String, val name: String, val image: String?, val color: String?)

data class LabelParams(var id: String?, val name: ContentParams, val image: String?)

data class CategoryParams(var id: String?, val name: ContentParams, val image: String?, val color: String?)

data class ContentParams(val key: String, val content: Map<String, String>)

data class Article(
    var id: String,
    val categoryId: String,
    val name: String,
    val image: String?,
    val description: String?,
    val isVeggan: Boolean,
    val price: Double,
    val priceUnit: String
)

data class ArticleParams(
    var id: String?,
    val categoryId: String,
    val name: ContentParams,
    val image: String?,
    val description: ContentParams?,
    val labelIds: List<String>,
    val isVeggan: Boolean,
    val price: Double,
    val priceUnit: String
)

data class ArticleLabel(val articleId: String, val labelId: String)