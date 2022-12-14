package utils

//reference for the code below: https://reader.tutors.dev/#/lab/sdt-sept-2022.netlify.app/topic-07-lambdas/unit-02-labs/book-01/Assignment2

object Utilities {

    //NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun validRange(numberToCheck: Int, min: Int, max: Int): Boolean {
        return numberToCheck in min..max
    }

    //utility method to determine if an index is valid in a list.
    @JvmStatic
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

}