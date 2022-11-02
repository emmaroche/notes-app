package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.CategoryUtility.categories
import utils.CategoryUtility.isValidCategory

//reference for the code below: https://reader.tutors.dev/#/lab/sdt-sept-2022.netlify.app/topic-07-lambdas/unit-02-labs/book-01/Assignment2
internal class CategoryUtilityTest {
    @Test
    fun categoriesReturnsFullCategoriesSet(){
        Assertions.assertEquals(5, categories.size)
        Assertions.assertTrue(categories.contains("Home"))
        Assertions.assertTrue(categories.contains("College"))
        Assertions.assertFalse(categories.contains(""))
    }

    @Test
    fun isValidCategoryTrueWhenCategoryExists(){
        Assertions.assertTrue(isValidCategory("Home"))
        Assertions.assertTrue(isValidCategory("home"))
        Assertions.assertTrue(isValidCategory("COLLEGE"))
    }

    @Test
    fun isValidCategoryFalseWhenCategoryDoesNotExist(){
        Assertions.assertFalse(isValidCategory("Hom"))
        Assertions.assertFalse(isValidCategory("colllege"))
        Assertions.assertFalse(isValidCategory(""))
    }
}