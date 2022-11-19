package bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NutritionInfoTest {

    @Test
    void testNutritionInfoWithNegativeCarbohydrates() {

        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-10, 80, 10),
            "Carbohydrates must not be negative");
    }

    @Test
    void testNutritionInfoWithNegativeFats() {

        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(80, -10, 10),
            "Fats must not be negative");
    }

    @Test
    void testNutritionInfoWithNegativeProteins() {

        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(80.5, 9.5, -10),
            "Proteins must not be negative");
    }

    @Test
    void testNutritionInfoWithNegativeCarbohydratesAndProteins() {

        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-10, 80.5, -9.5),
            "Carbohydrates and proteins must not be negative.");
    }

    @Test
    void testNutritionInfoWithNegativeFatsAndProteins() {

        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(80.8, -9.2, -10),
            "Fats and proteins must not be negative");
    }

    @Test
    void testNutritionInfoWithNegativeCarbohydratesAndFats() {

        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-1, -1, 102),
            "Carbohydrates and fats must not be negative");
    }

    @Test
    void testNutritionInfoWithAllNutrientNegative() {

        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-80, -10, -10),
            "All nutrients must be non-negative");
    }

    @Test
    void testNutritionInfoWithCarbohydratesZero() {

        assertDoesNotThrow(() -> new NutritionInfo(0, 80.2, 19.8),
            "Carbohydrates can be zero");
    }

    @Test
    void testNutritionInfoWithCarbohydratesAndFatsZero() {

        assertDoesNotThrow(() -> new NutritionInfo(0, 0, 100),
            "Carbohydrates and fats can be zero");
    }

    @Test
    void testNutritionInfoSumIsHundred() {

        assertDoesNotThrow(() -> new NutritionInfo(10, 8.5, 81.5),
            "Sum of all nutrients must be a hundred");
    }

    @Test
    void testNutritionInfoWithAllNutrientsZero() {

        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(0, 0, 0),
            "Sum of all nutrients must be a hundred");
    }

    @Test
    void testCaloriesRightCalculationWithZero() {

        assertEquals(400, new NutritionInfo(100, 0, 0 ).calories(),
            0.001,"Total number of calories must be 400");
    }

    @Test
    void testCaloriesRightCalculationWithoutZero() {

        assertEquals(450, new NutritionInfo(80, 10, 10 ).calories(),
            0.001,"Total number of calories must be 450");
    }

    @Test
    void testCaloriesWrongCalculationWithoutZero() {

        assertEquals(437.5, new NutritionInfo(90, 7.5, 2.5 ).calories(),
            0.001,"Total number of calories must be 437.5");
    }

    @Test
    void testCaloriesWrongCalculationWithZero() {

        assertEquals(449.5, new NutritionInfo(90.1, 9.9, 0 ).calories(),
            0.001,"Total number of calories must be 449.5");
    }
}

