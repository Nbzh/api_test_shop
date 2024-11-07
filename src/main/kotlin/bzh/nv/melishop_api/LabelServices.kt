package bzh.nv.melishop_api

import com.jetbrains.exported.JBRApi.Service
import org.springframework.jdbc.core.JdbcTemplate

@Service
class LabelServices(private val db: JdbcTemplate) {

    fun getLabel(labelId: String): Label =
        db.queryForObject("select * from label where id = ?", Label::class.java, labelId)


    fun getLabels(): List<Label> =
        db.queryForList("select * from label", Label::class.java)

    fun insertOrUpdateLabel(label: Label): Label {
        val sqlInsert =
            """
            INSERT INTO label (id, name, image ) VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE id = ?, name = ?, image = ?
            """
        db.update(sqlInsert, label.id, label.name, label.image)
        return getLabel(label.id)
    }

}