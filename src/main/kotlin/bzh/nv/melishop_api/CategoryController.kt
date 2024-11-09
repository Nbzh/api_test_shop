package bzh.nv.melishop_api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CategoryController(private val services: CategoryServices) {

    @GetMapping("/categories")
    fun getCategories(@RequestHeader("Accept-Language") acceptLanguage: String?): List<Category> =
        services.getCategories(acceptLanguage ?: "en")

    @PostMapping("category")
    fun postCategory(
        @RequestBody categoryParams: CategoryParams,
        @RequestHeader("Accept-Language") acceptLanguage: String?
    ) =
        services.insertOrUpdateCategory(categoryParams, acceptLanguage ?: "en")

    @PostMapping("categories")
    fun postCategories(
        @RequestBody categoryParams: List<CategoryParams>,
        @RequestHeader("Accept-Language") acceptLanguage: String?
    ) =
        services.insertOrUpdateCategories(categoryParams, acceptLanguage ?: "en")
}