package bzh.nv.melishop_api

import com.jetbrains.exported.JBRApi.Service
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet

@Service
class ArticleServices(private val db: JdbcTemplate) {

    private fun getCategory(categoryId: String): Category? =
        db.queryForObject("select * from DbCategory where id = $categoryId", Category::class.java)

    private fun getLabels(articleId: String): List<Label> {
        val labelQuery =
            "select l.* from Label as l inner join ArticleLabel as al on l.id = al.labelId where al.articleId = $articleId"
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
            getBoolean("isVeggies"),
            getDouble("price"),
            getString("priceUnit"),
            labels
        )
    }

    fun getArticle(article: String): ArticleResponse? {
        val sqlQuery = "SELECT * FROM Article where id = $article"
        return db.query(sqlQuery) { rs, _ ->
            rs.toArticleResponse()
        }.firstOrNull()
    }

    fun getArticles(categories: List<String>?): List<ArticleResponse> {
        val query = if (categories.isNullOrEmpty()) {
            "SELECT * FROM Article"
        } else {
            "SELECT * FROM Article WHERE categoryId IN (${categories.joinToString(",") { "'$it'" }})"
        }
        return db.query(query) { rs, _ -> rs.toArticleResponse() }
    }

    fun insertOrUpdateArticle(article: ArticleParams): ArticleResponse {
        val sqlInsert =
            """
                INSERT INTO Article (id, categoryId, name, image, description, isVeggies, price, priceUnit ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE title = VALUES(id), categoryId = VALUES(categoryId), name = VALUES(name), image = VALUES(image),
                description = VALUES(description), isVeggies = VALUES(isVeggies), price = VALUES(price), priceUnit = VALUES(priceUnit)
                 """
        db.update(
            sqlInsert,
            article.id,
            article.categoryId,
            article.name,
            article.image,
            article.description,
            article.isVeggies,
            article.price,
            article.priceUnit
        )
        val labelSqlInsert =
            """
            INSERT INTO ArticleLabel (articleId, labelId) VALUES (?, ?)
            ON DUPLICATE KEY UPDATE articleId = VALUES(articleId), labelId = VALUES(labelId)
             """
        article.labelIds.forEach { labelId ->
            db.update(labelSqlInsert, article.id, labelId)
        }
        return getArticle(article.id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Article not inserted")
    }
}