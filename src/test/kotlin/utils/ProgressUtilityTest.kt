package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.ProgressUtility.progress
import utils.ProgressUtility.isValidProgress

internal class ProgressUtilityTest {
    @Test
    fun progressReturnsFullProgressSet(){
        Assertions.assertEquals(3, progress.size)
        Assertions.assertTrue(progress.contains("To-do"))
        Assertions.assertTrue(progress.contains("Doing"))
        Assertions.assertTrue(progress.contains("Done"))
        Assertions.assertFalse(progress.contains(""))
    }

    @Test
    fun isValidProgressTrueWhenProgressExists(){
        Assertions.assertTrue(isValidProgress("To-do"))
        Assertions.assertTrue(isValidProgress("to-do"))
        Assertions.assertTrue(isValidProgress("DOING"))
    }

    @Test
    fun isValidProgressFalseWhenProgressDoesNotExist(){
        Assertions.assertFalse(isValidProgress("todo"))
        Assertions.assertFalse(isValidProgress("doiing"))
        Assertions.assertFalse(isValidProgress(""))
    }
}