package bzh.nv.melishop_api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class LabelController(private val services: LabelServices) {

    @GetMapping("/labels")
    fun getLabels() : List<Label> = services.getLabels()

    @PostMapping("label")
    fun postLabel(@RequestBody labelParams : LabelParams) =
        services.insertOrUpdateLabel(labelParams)

    @PostMapping("labels")
    fun postLabels(@RequestBody labelParams : List<LabelParams>) =
        services.insertOrUpdateLabels(labelParams)
}