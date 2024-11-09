package bzh.nv.melishop_api

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.util.*

@Service
class ArticleServices(
    private val db: JdbcTemplate,
    private val categoryService: CategoryServices,
    private val labelServices: LabelServices
) {

    private fun ResultSet.toArticleResponse(language: String): ArticleResponse {
        val category = categoryService.getCategory(getString("categoryId"), language)
        val labels = labelServices.getLabelsFromArticle(getString("id"), language)
        return ArticleResponse(
            getString("id"),
            category,
            getString("name"),
            getString("image"),
            getString("description"),
            getBoolean("isVeggan"),
            getDouble("price"),
            getString("priceUnit"),
            labels
        )
    }

    private fun Article.toArticleResponse(language: String): ArticleResponse {
        val category = categoryService.getCategory(categoryId, language)
        val labels = labelServices.getLabelsFromArticle(id, language)
        return ArticleResponse(
            id,
            category,
            name,
            image,
            description,
            isVeggan,
            price,
            priceUnit,
            labels
        )
    }

    fun getArticle(article: String, language: String): ArticleResponse =
        db.queryForObject("SELECT * FROM article where id = ?", Article::class.java, article)
            .toArticleResponse(language)


    fun getArticles(categories: List<String>?, language: String): List<ArticleResponse> =
        if (categories.isNullOrEmpty()) {
            db.query("SELECT * FROM article") { rs, _ -> rs.toArticleResponse(language) }
        } else {
            db.query(
                "SELECT * FROM article WHERE categoryId IN (?)",
                { rs, _ -> rs.toArticleResponse(language) },
                categories.joinToString(",") { "'$it'" })
        }

    fun insertOrUpdateArticle(article: ArticleParams, language: String): ArticleResponse {
        article.id = article.id ?: UUID.randomUUID().toString()
        val sqlInsert =
            """
                INSERT INTO article (id, categoryId, name, image, description, isVeggan, price, priceUnit ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE id = ?, categoryId = ?, name = ?, image = ?,
                description = ?, isVeggan = ?, price = ?, priceUnit = ?
                 """
        db.update(
            sqlInsert,
            article.id,
            article.categoryId,
            article.name,
            article.image,
            article.description,
            article.isVeggan,
            article.price,
            article.priceUnit
        )
        val labelSqlInsert =
            """
            INSERT INTO article_label (articleId, labelId) VALUES (?, ?)
            ON DUPLICATE KEY UPDATE article_id = ?, label_id = ?
             """
        article.labelIds.forEach { labelId ->
            db.update(labelSqlInsert, article.id, labelId)
        }
        return getArticle(article.id!!, language)
    }

    fun insertOrUpdateArticles(articles: List<ArticleParams>, language: String) =
        articles.map { article -> insertOrUpdateArticle(article, language) }
}