package org.setu.returnpoint

object AppData {
    val returnPoints = ReturnPointStore()

    init {
        returnPoints.create(
            ReturnPoint(
                name = "Example Supermarket",
                address = "1 Example Street",
                town = "Waterford",
                county = "Waterford",
                returnMethod = ReturnMethod.RVM,
                notes = "Demo record",
                isDemo = true
            )
        )
        returnPoints.create(
            ReturnPoint(
                name = "Example Corner Shop",
                address = "2 Sample Road",
                town = "Galway",
                county = "Galway",
                returnMethod = ReturnMethod.MANUAL,
                notes = "Demo record",
                isDemo = true
            )
        )
        returnPoints.create(
            ReturnPoint(
                name = "Example Petrol Station",
                address = "3 Placeholder Avenue",
                town = "Cork",
                county = "Cork",
                returnMethod = ReturnMethod.UNKNOWN,
                notes = "Demo record",
                isDemo = true
            )
        )
    }
}
