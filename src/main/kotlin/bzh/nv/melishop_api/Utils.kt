package bzh.nv.melishop_api

fun Double.formatToTwoDecimalPlaces(): String {
    val integerPart = this.toInt()
    val decimalPart = ((this - integerPart) * 100).toInt()
    return "$integerPart.${decimalPart.toString().padStart(2, '0')}"
}