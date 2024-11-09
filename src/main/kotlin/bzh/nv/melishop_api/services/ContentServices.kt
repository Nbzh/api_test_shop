package bzh.nv.melishop_api.services

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class ContentServices(private val db: JdbcTemplate) {
    fun getContent(resourceId: String, key: String, language: String) =
        when (language.substring(0, 2).lowercase()) {
            "fr" -> selectContent("content_fr", resourceId, key) ?: selectContent("content_en", resourceId, key)
            else -> selectContent("content_en", resourceId, key)
        }

    private fun selectContent(tableName: String, resourceId: String, key: String): String? =
        try {
            db.queryForObject(
                "select content from $tableName where resourceId = ? and contentKey = ?",
                { rs, _ -> rs.getString("content") },
                resourceId,
                key
            )
        } catch (e: Exception) {
            null
        }

    @Suppress("SqlNoInjection", "SqlUnsafe")
    fun insertOrUpdateContent(tableName: String, resourceId: String, key: String, content: String) {
        db.update("MERGE INTO $tableName KEY (resourceId, contentKey) VALUES (?, ?, ?)", resourceId, key, content)
    }
}