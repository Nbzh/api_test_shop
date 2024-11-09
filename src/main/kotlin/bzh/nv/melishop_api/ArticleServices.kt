package bzh.nv.melishop_api

import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet
import java.util.*

@Service
class ArticleServices(
    private val db: JdbcTemplate,
    private val contentServices: ContentServices,
    private val categoryService: CategoryServices,
    private val labelServices: LabelServices
) {

    private fun ResultSet.toArticleResponse(language: String): ArticleResponse {
        val category = categoryService.getCategory(getString("categoryId"), language)
        val labels = labelServices.getLabelsFromArticle(getString("id"), language)
        val articleId = getString("id")
        return ArticleResponse(
            articleId,
            category,
            getString("name").let { key -> contentServices.getContent(articleId, key, language) ?: key },
            getString("image"),
            getString("description")?.let { key -> contentServices.getContent(articleId, key, language) ?: key },
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
            name.let { key -> contentServices.getContent(id, key, language) ?: key },
            image,
            description?.let { key -> contentServices.getContent(id, key, language) ?: key },
            isVeggan,
            price,
            priceUnit,
            labels
        )
    }

    fun getArticle(article: String, language: String): ArticleResponse =
        db.queryForObject("SELECT * FROM article where id = ?", { rs, _ ->
            Article(
                rs.getString("id"),
                rs.getString("categoryId"),
                rs.getString("name"),
                rs.getString("image"),
                rs.getString("description"),
                rs.getBoolean("isVeggan"),
                rs.getDouble("price"),
                rs.getString("priceUnit")
            )
        }, article)?.toArticleResponse(language) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Article with id $article not found"
        )


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
        val nameFr = article.name.content["fr"]
        val nameEn = article.name.content["en"]
        val descriptionFr = article.description?.content?.get("fr")
        val descriptionEn = article.description?.content?.get("en")
        val sqlMerge = "MERGE INTO article KEY (id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        db.update(
            sqlMerge,
            article.id,
            article.categoryId,
            article.name.key,
            article.image,
            article.description?.key,
            article.isVeggan,
            article.price,
            article.priceUnit
        )
        if (nameFr != null)
            contentServices.insertOrUpdateContent("content_fr", article.id!!, article.name.key, nameFr)
        if(nameEn != null)
            contentServices.insertOrUpdateContent("content_en", article.id!!, article.name.key, nameEn)
        if (descriptionFr != null)
            contentServices.insertOrUpdateContent("content_fr", article.id!!, article.description.key, descriptionFr)
        if(descriptionEn != null)
            contentServices.insertOrUpdateContent("content_en", article.id!!, article.description.key, descriptionEn)
        val articleLabels = labelServices.getLabelsFromArticle(article.id!!, language)
        articleLabels.map { al -> al.id }.minus(article.labelIds.toSet()).also {
            val deleteAssociation = "DELETE FROM article_label WHERE articleId = ? AND labelId in (?)"
            db.update(deleteAssociation, article.id, it)
        }
        val labelSqlInsert = "MERGE INTO article_label KEY (articleId, labelId) VALUES (?, ?)"
        article.labelIds.forEach { labelId ->
            db.update(labelSqlInsert, article.id, labelId)
        }
        return getArticle(article.id!!, language)
    }

    fun insertOrUpdateArticles(articles: List<ArticleParams>, language: String) =
        articles.map { article -> insertOrUpdateArticle(article, language) }
}