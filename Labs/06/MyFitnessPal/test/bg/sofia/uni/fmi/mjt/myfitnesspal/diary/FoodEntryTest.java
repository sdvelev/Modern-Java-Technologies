package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodEntryTest {

    @Test
    void testFoodEntryWithFoodNull() {

        assertThrows(IllegalArgumentException.class, () -> new FoodEntry(null, 1,
            new NutritionInfo(100, 0, 0)));
    }

    @Test
    void testFoodEntryWithFoodBlank() {

        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("    ", 1,
            new NutritionInfo(100, 0, 0)));
    }

    @Test
    void testFoodEntryWithNegativeServingSize() {

        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("SampleFoodName", -10,
            new NutritionInfo(100, 0, 0)));
    }

    @Test
    void testFoodEntryWithNutritionInfoNull() {

        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("Cheese", 100, null));
    }

    @Test
    void testFoodEntryCorrectInformation() {

        assertDoesNotThrow(() -> new FoodEntry("Cheese", 100,
            new NutritionInfo(20, 40, 40)));
    }

}
