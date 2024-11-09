package bzh.nv.melishop_api.controllers

import bzh.nv.melishop_api.data.Label
import bzh.nv.melishop_api.data.LabelParams
import bzh.nv.melishop_api.services.LabelServices
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class LabelController(private val services: LabelServices) {

    @GetMapping("/labels")
    fun getLabels(@RequestHeader("Accept-Language") acceptLanguage: String?): List<Label> =
        services.getLabels(acceptLanguage ?: "en")

    @PostMapping("label")
    fun postLabel(
        @RequestBody labelParams: LabelParams,
        @RequestHeader("Accept-Language") acceptLanguage: String?
    ) = services.insertOrUpdateLabel(labelParams, acceptLanguage ?: "en")

    @PostMapping("labels")
    fun postLabels(
        @RequestBody labelParams: List<LabelParams>,
        @RequestHeader("Accept-Language") acceptLanguage: String?
    ) = services.insertOrUpdateLabels(labelParams, acceptLanguage ?: "en")
}