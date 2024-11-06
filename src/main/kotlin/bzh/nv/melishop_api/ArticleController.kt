package bzh.nv.melishop_api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController {
    @GetMapping("/")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"

    @GetMapping("/articles")
    fun getArticles() = listOf(
        fakeArticle(),
        fakeArticle(),
        fakeArticle(),
        fakeArticle(),
        fakeArticle()
    )
}