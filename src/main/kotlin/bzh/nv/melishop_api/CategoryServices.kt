package bzh.nv.melishop_api

import com.jetbrains.exported.JBRApi.Service
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet

@Service
class CategoryServices(private val db: JdbcTemplate) {

    fun getCategory(categoryId: String): Category =
        db.queryForObject("select * from category where id = ?", Category::class.java, categoryId)

    fun getCategories(): List<Category> =
        db.queryForList("select * from category", Category::class.java)

    fun insertOrUpdateCategory(category: Category): Category {
        val sqlInsert =
            """
            INSERT INTO category (id, name, image, color ) VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE id = ?, name = ?, image = ?, color = ?
            """
        db.update(sqlInsert, category.id, category.name, category.image, category.color)
        return getCategory(category.id)
    }
}