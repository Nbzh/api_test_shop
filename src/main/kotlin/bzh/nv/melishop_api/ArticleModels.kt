package bzh.nv.melishop_api

data class Article(
    var id: String,
    val category: DbCategory,
    val name: String,
    val image: String,
    val description: String,
    val isVeggies: Boolean,
    val price: Double,
    val priceUnit: String,
    val labels: List<DbLabel>
)

data class DbLabel(val id: String, val name: String, val image: String)

data class DbCategory(val id: String, val name: String, val image: String, val color: String)

data class DbArticle(
    var id: String,
    val categoryId: String,
    val name: String,
    val image: String,
    val description: String,
    val isVeggies: Boolean,
    val price: Double,
    val priceUnit: String
)

data class DbArticleLabel(val articleId: String, val labelId: String)