package utils

//reference for the code below: https://reader.tutors.dev/#/lab/sdt-sept-2022.netlify.app/topic-07-lambdas/unit-02-labs/book-01/Assignment2
object CategoryUtility {

    //NOTE: JvmStatic annotation means that the categories variable is static i.e. we can reference it through the class

    @JvmStatic
    val categories = setOf ("Home", "College", "Work" , "Hobby", "Holiday")

    @JvmStatic
    fun isValidCategory(categoryToCheck: String?): Boolean {
        for (category in categories) {
            if (category.equals(categoryToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}