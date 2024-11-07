package bzh.nv.melishop_api

import com.jetbrains.exported.JBRApi.Service
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet

@Service
class ArticleServices(private val db: JdbcTemplate) {

    private fun getCategory(categoryId: String): Category? =
        db.queryForObject("select * from category where id = $categoryId", Category::class.java)

    private fun getLabels(articleId: String): List<Label> {
        val labelQuery =
            "select l.* from label as l inner join article_label as al on l.id = al.labelId where al.articleId = $articleId"
        return db.queryForList(labelQuery, Label::class.java)
    }

    private fun ResultSet.toArticleResponse(): ArticleResponse {
        val category = getCategory(getString("categoryId"))
            ?: throw ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Category not found")
        val labels = getLabels(getString("id"))
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

    private fun Article.toArticleResponse(): ArticleResponse {
        val category = getCategory(categoryId)
            ?: throw ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Category not found")
        val labels = getLabels(id)
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

    fun getArticle(article: String): ArticleResponse =
        db.queryForObject("SELECT * FROM article where id = ?", Article::class.java, article).toArticleResponse()


    fun getArticles(categories: List<String>?): List<ArticleResponse> =
        if (categories.isNullOrEmpty()) {
            db.query("SELECT * FROM article") { rs, _ -> rs.toArticleResponse() }
        } else {
            db.query(
                "SELECT * FROM article WHERE categoryId IN (?)",
                { rs, _ -> rs.toArticleResponse() },
                categories.joinToString(",") { "'$it'" })
        }

    fun insertOrUpdateArticle(article: ArticleParams): ArticleResponse {
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
        return getArticle(article.id)
    }
}