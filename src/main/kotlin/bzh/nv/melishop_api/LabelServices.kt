package bzh.nv.melishop_api

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LabelServices(private val db: JdbcTemplate) {

    fun getLabel(labelId: String): Label =
        db.queryForObject("select * from label where id = ?", Label::class.java, labelId)


    fun getLabels(): List<Label> =
        db.queryForList("select * from label", Label::class.java)

    fun getLabelsFromArticle(articleId: String): List<Label> {
        val labelQuery =
            "select l.* from label as l inner join article_label as al on l.id = al.labelId where al.articleId = ?"
        return db.queryForList(labelQuery, Label::class.java, articleId)
    }

    fun insertOrUpdateLabel(label: LabelParams): Label {
        label.id = label.id ?: UUID.randomUUID().toString()
        val sqlInsert =
            """
            INSERT INTO label (id, name, image ) VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE id = ?, name = ?, image = ?
            """
        db.update(sqlInsert, label.id, label.name, label.image)
        return getLabel(label.id!!)
    }

    fun insertOrUpdateLabels(labels: List<LabelParams>) =
        labels.map { label -> insertOrUpdateLabel(label)  }
}