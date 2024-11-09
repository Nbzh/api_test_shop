package bzh.nv.melishop_api.services

import bzh.nv.melishop_api.data.Category
import bzh.nv.melishop_api.data.CategoryParams
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class CategoryServices(private val db: JdbcTemplate, private val contentServices: ContentServices) {

    fun getCategory(categoryId: String, language: String): Category =
        db.queryForObject(
            "select * from category where id = ?", { rs, _ ->
                Category(
                    id = rs.getString("id"),
                    name = rs.getString("name").let { name ->
                        contentServices.getContent(categoryId, name, language) ?: name
                    },
                    image = rs.getString("image"),
                    color = rs.getString("color")
                )
            },
            categoryId
        ) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Category with id $categoryId not found"
        )

    fun getCategories(language: String): List<Category> =
        db.query("select * from category") { rs, _ ->
            val categoryId = rs.getString("id")
            Category(
                id = categoryId,
                name = rs.getString("name").let {
                    contentServices.getContent(categoryId, it, language) ?: it
                },
                image = rs.getString("image"),
                color = rs.getString("color")
            )
        }

    fun insertOrUpdateCategory(category: CategoryParams, language: String): Category {
        category.id = category.id ?: UUID.randomUUID().toString()
        val nameFr = category.name.content["fr"]
        val nameEn = category.name.content["en"]
        if (nameFr != null)
            contentServices.insertOrUpdateContent("content_fr", category.id!!, category.name.key, nameFr)
        if(nameEn != null)
            contentServices.insertOrUpdateContent("content_en", category.id!!, category.name.key, nameEn)
        val sqlInsert = "MERGE INTO category KEY (id) VALUES (?, ?, ?, ?)"
        db.update(sqlInsert, category.id, category.name.key, category.image, category.color)
        return getCategory(category.id!!, language)
    }

    fun insertOrUpdateCategories(categories: List<CategoryParams>, language: String) =
        categories.map { category -> insertOrUpdateCategory(category, language) }
}