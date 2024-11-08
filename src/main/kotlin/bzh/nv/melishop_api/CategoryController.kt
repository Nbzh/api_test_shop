package bzh.nv.melishop_api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CategoryController(private val services: CategoryServices) {

    @GetMapping("/categories")
    fun getCategories() : List<Category> = services.getCategories()

    @PostMapping("category")
    fun postCategory(@RequestBody categoryParams : CategoryParams) =
        services.insertOrUpdateCategory(categoryParams)

    @PostMapping("categories")
    fun postCategories(@RequestBody categoryParams : List<CategoryParams>) =
        services.insertOrUpdateCategories(categoryParams)
}