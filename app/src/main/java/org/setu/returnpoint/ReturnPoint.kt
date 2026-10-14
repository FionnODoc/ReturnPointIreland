package org.setu.returnpoint

enum class ReturnMethod(val label: String) {
    RVM("Reverse vending machine"),
    MANUAL("Manual (at the counter)"),
    UNKNOWN("Unknown")
}

val irishCounties = listOf(
    "Antrim", "Armagh", "Carlow", "Cavan", "Clare", "Cork", "Derry", "Donegal",
    "Down", "Dublin", "Fermanagh", "Galway", "Kerry", "Kildare", "Kilkenny",
    "Laois", "Leitrim", "Limerick", "Longford", "Louth", "Mayo", "Meath",
    "Monaghan", "Offaly", "Roscommon", "Sligo", "Tipperary", "Tyrone",
    "Waterford", "Westmeath", "Wexford", "Wicklow"
)

data class ReturnPoint(
    val id: Long = 0L,
    val name: String = "",
    val address: String = "",
    val town: String = "",
    val county: String = "",
    val returnMethod: ReturnMethod = ReturnMethod.UNKNOWN,
    val notes: String = "",
    val isFavourite: Boolean = false,
    val isDemo: Boolean = false
)

fun validate(point: ReturnPoint): Map<String, String> {
    val errors = mutableMapOf<String, String>()

    if (point.name.isBlank()) {
        errors["name"] = "Name is required"
    }
    if (point.address.isBlank()) {
        errors["address"] = "Address is required"
    }
    if (point.town.isBlank()) {
        errors["town"] = "Town is required"
    }
    if (point.county !in irishCounties) {
        errors["county"] = "Choose a county from the list"
    }

    return errors
}

fun searchPoints(points: List<ReturnPoint>, query: String): List<ReturnPoint> {
    val text = query.trim()

    if (text.isEmpty()) {
        return points
    }

    return points.filter { p ->
        p.name.contains(text, ignoreCase = true) ||
            p.address.contains(text, ignoreCase = true) ||
            p.town.contains(text, ignoreCase = true)
    }
}

fun filterPoints(
    points: List<ReturnPoint>,
    county: String?,
    method: ReturnMethod?
): List<ReturnPoint> {
    return points.filter { p ->
        (county == null || p.county == county) &&
            (method == null || p.returnMethod == method)
    }
}
