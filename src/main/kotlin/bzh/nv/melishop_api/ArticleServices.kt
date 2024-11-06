package bzh.nv.melishop_api

import com.jetbrains.exported.JBRApi.Service
import org.springframework.jdbc.core.JdbcTemplate

@Service
class ArticleServices(private val db : JdbcTemplate){

    fun getArticles(categories : List<String>){

    }
}