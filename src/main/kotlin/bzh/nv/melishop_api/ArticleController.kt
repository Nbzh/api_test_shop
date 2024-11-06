package bzh.nv.melishop_api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ArticleController {
    @GetMapping("/")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"

    @GetMapping("/articles")
    fun getArticles(@RequestParam("categories", required = false) categoryIds: List<String>? = null): List<Article> {
        val random = (5..20).random()
        return List(random) { fakeArticle() }.run {
            if (categoryIds.isNullOrEmpty()) this
            else filter { article -> article.category.id in categoryIds }
        }
    }

    @GetMapping("/articles/{articleId}")
    fun getArticleById(@PathVariable articleId: String): Article {
        return fakeArticle(id = articleId)
    }
}