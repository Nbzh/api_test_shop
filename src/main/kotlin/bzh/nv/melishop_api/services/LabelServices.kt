package bzh.nv.melishop_api.services

import bzh.nv.melishop_api.data.Label
import bzh.nv.melishop_api.data.LabelParams
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class LabelServices(private val db: JdbcTemplate, private val contentServices: ContentServices) {

    fun getLabel(labelId: String, language: String): Label? =
        try {
            db.queryForObject(
                "select * from label where id = ?",
                { rs, _ ->
                    Label(
                        id = rs.getString("id"),
                        name = rs.getString("name").let { name ->
                            contentServices.getContent(labelId, name, language) ?: name
                        },
                        image = rs.getString("image")
                    )
                },
                labelId
            )
        } catch (e: Exception) {
            null
        }

    fun getLabels(language: String): List<Label> =
        db.query("select * from label") { rs, _ ->
            val labelId = rs.getString("id")
            Label(
                id = labelId,
                name = rs.getString("name").let { name ->
                    contentServices.getContent(labelId, "label_name", language) ?: name
                },
                image = rs.getString("image")
            )
        }

    fun getLabelsFromArticle(articleId: String, language: String): List<Label> {
        val labelQuery =
            "select l.* from label as l inner join article_label as al on l.id = al.labelId where al.articleId = ?"
        return db.query(labelQuery, { rs, _ ->
            val labelId = rs.getString("id")
            Label(
                id = labelId,
                name = rs.getString("name").let { name ->
                    contentServices.getContent(labelId, "label_name", language) ?: name
                },
                image = rs.getString("image")
            )
        }, articleId)
    }

    fun insertOrUpdateLabel(label: LabelParams, language: String): Label {
        label.id = label.id ?: UUID.randomUUID().toString()
        val nameFr = label.name.content["fr"]
        val nameEn = label.name.content["en"]
        if (nameFr != null)
            contentServices.insertOrUpdateContent("content_fr", label.id!!, label.name.key, nameFr)
        if (nameEn != null)
            contentServices.insertOrUpdateContent("content_en", label.id!!, label.name.key, nameEn)
        val sqlMerge = "MERGE INTO label KEY (id) VALUES (?, ?, ?)"
        db.update(sqlMerge, label.id, label.name.key, label.image)
        return getLabel(label.id!!, language) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Label ${label.id} not found")
    }

    fun insertOrUpdateLabels(labels: List<LabelParams>, language: String) =
        labels.map { label -> insertOrUpdateLabel(label, language) }
}