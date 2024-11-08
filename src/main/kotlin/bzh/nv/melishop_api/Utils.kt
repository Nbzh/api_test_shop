package bzh.nv.melishop_api

fun Double.formatToTwoDecimalPlaces(): String {
    val integerPart = this.toInt()
    val decimalPart = ((this - integerPart) * 100).toInt()
    return "$integerPart.${decimalPart.toString().padStart(2, '0')}"
}

fun String.getContentTable() =
    when(this.substring(0, 2).lowercase()) {
        "fr" -> "content_fr"
        else -> "content_en"
    }