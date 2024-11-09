package bzh.nv.melishop_api.controllers

import bzh.nv.melishop_api.data.ArticleParams
import bzh.nv.melishop_api.data.ArticleResponse
import bzh.nv.melishop_api.services.ArticleServices
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ArticleController(private val services: ArticleServices) {

    @CrossOrigin(origins = ["http://localhost:8081"], allowedHeaders = ["*"], allowCredentials = "true")
    @GetMapping("/articles")
    fun getArticles(
        @RequestParam("categories", required = false) categoryIds: List<String>? = null,
        @RequestHeader("Accept-Language") acceptLanguage: String? = null
    ): List<ArticleResponse> = services.getArticles(categoryIds, acceptLanguage ?: "en")


    @CrossOrigin(origins = ["http://localhost:8081"], allowedHeaders = ["*"], allowCredentials = "true")
    @GetMapping("/articles/{articleId}")
    fun getArticleById(
        @PathVariable articleId: String,
        @RequestHeader("Accept-Language") acceptLanguage: String? = null
    ): ArticleResponse? =
        services.getArticle(articleId, acceptLanguage ?: "en")

    @PostMapping("/article")
    fun postArticle(
        @RequestBody params: ArticleParams,
        @RequestHeader("Accept-Language") acceptLanguage: String? = null
    ) =
        services.insertOrUpdateArticle(params, acceptLanguage ?: "en")

    @PostMapping("/articles")
    fun postArticles(
        @RequestBody params: List<ArticleParams>,
        @RequestHeader("Accept-Language") acceptLanguage: String? = null
    ) =
        services.insertOrUpdateArticles(params, acceptLanguage ?: "en")
}