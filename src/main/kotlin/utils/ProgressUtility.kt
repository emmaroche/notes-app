package utils

object ProgressUtility {

    @JvmStatic
    val progress = setOf ("To-do", "Doing", "Done")

    @JvmStatic
    fun isValidProgress(progressToCheck: String?): Boolean {
        for (progress in progress) {
            if (progress.equals(progressToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}