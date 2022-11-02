package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.Utilities.validRange

//reference for the code below: https://reader.tutors.dev/#/lab/sdt-sept-2022.netlify.app/topic-07-lambdas/unit-02-labs/book-01/Assignment2
class UtilitiesTest {

    @Test
    fun validRangeWorksWithPositiveTestData() {
        Assertions.assertTrue(validRange(1, 1, 1))
        Assertions.assertTrue(validRange(1, 1, 2))
        Assertions.assertTrue(validRange(1, 0, 1))
        Assertions.assertTrue(validRange(1, 0, 2))
        Assertions.assertTrue(validRange(-1, -2, -1))
    }

    @Test
    fun validRangeWorksWithNegativeTestData() {
        Assertions.assertFalse(validRange(1, 0, 0))
        Assertions.assertFalse(validRange(1, 1, 0))
        Assertions.assertFalse(validRange(1, 2, 1))
        Assertions.assertFalse(validRange(-1, -1, -2))
    }
}