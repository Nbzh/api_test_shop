package bzh.nv.melishop_api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ArticleController(private val services: ArticleServices) {
    @GetMapping("/")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"

    @GetMapping("/articles")
    fun getArticles(@RequestParam("categories", required = false) categoryIds: List<String>? = null): List<ArticleResponse> =
        services.getArticles(categoryIds)

    @GetMapping("/articles/{articleId}")
    fun getArticleById(@PathVariable articleId: String): ArticleResponse? =
        services.getArticle(articleId)
}