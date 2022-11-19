package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DailyFoodDiaryTest {

    private NutritionInfoAPI nutritionInfoAPIMock = Mockito.mock(NutritionInfoAPI.class);

    private DailyFoodDiary dailyFoodDiary = new DailyFoodDiary(nutritionInfoAPIMock);

    @Test
    void testAddFoodMealNull() {

        assertThrows(IllegalArgumentException.class,
            () -> dailyFoodDiary.addFood(null, "SampleFoodName", 1 ),
            "Meal must not be null when trying to add food");
    }

    @Test
    void testAddFoodFoodNameNull() {

        assertThrows(IllegalArgumentException.class,
            () -> dailyFoodDiary.addFood(Meal.BREAKFAST, null , 1 ),
            "Food name must not be null when trying to add food");
    }

    @Test
    void testAddFoodFoodNameEmpty() {

        assertThrows(IllegalArgumentException.class,
            () -> dailyFoodDiary.addFood(Meal.BREAKFAST, "" , 1 ),
            "Food name cannot be empty when trying to add food");
    }

    @Test
    void testAddFoodFoodNameBlank() {

        assertThrows(IllegalArgumentException.class,
            () -> dailyFoodDiary.addFood(Meal.BREAKFAST, "    " , 1 ),
            "Food name cannot be blank when trying to add food");
    }

    @Test
    void testAddFoodServingSizeNegative() {

        assertThrows(IllegalArgumentException.class,
            () -> dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName" , -6 ),
            "Serving size cannot be negative when trying to add food");
    }

    @Test
    void testAddFoodNutritionInfoUnavailableWithoutMeals() throws UnknownFoodException{

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName")).
            thenThrow(new UnknownFoodException("There is no data for current food"));

        assertThrows(UnknownFoodException.class,
            () -> this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName", 12),
            "UnknownFoodException expected to be thrown when there is no data related to foodName");
    }

    @Test
    void testAddFoodNutritionInfoUnavailableWithMeal() throws UnknownFoodException{

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName")).
            thenThrow(new UnknownFoodException("There is no data for current food", new NullPointerException()));

        when(nutritionInfoAPIMock.getNutritionInfo("AnotherSampleFoodName")).
            thenReturn(new NutritionInfo(30, 30, 40));

        this.dailyFoodDiary.addFood(Meal.SNACKS, "AnotherSampleFoodName", 12);

        assertThrows(UnknownFoodException.class,
            () -> this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName", 12),
            "UnknownFoodException expected to be thrown when there is no data related to foodName");
    }

    @Test
    void testAddFoodNutritionFoodEntryCorrectMealEmpty() throws UnknownFoodException{

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName")).
            thenReturn(new NutritionInfo(30, 30, 40));

        Map<Meal, List<FoodEntry>> meals = new EnumMap<>(Meal.class);

        meals.put(Meal.BREAKFAST, List.of(new FoodEntry("SampleFoodName", 12,
            new NutritionInfo(30, 30, 40))));


        assertIterableEquals(meals.get(Meal.BREAKFAST),
            List.of(this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName", 12)),
            "Returned FoodEntry List must be equal to the expected one with one element" );

        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName");
    }


    @Test
    void testAddFoodNutritionFoodEntryCorrect() throws UnknownFoodException{

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName")).
            thenReturn(new NutritionInfo(30, 30, 40));

        when(nutritionInfoAPIMock.getNutritionInfo("AnotherSampleFoodName")).
            thenReturn(new NutritionInfo(10, 10, 80));

        this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName", 12);
        this.dailyFoodDiary.addFood(Meal.BREAKFAST, "AnotherSampleFoodName", 10);

      /*  assertIterableEquals(List.of(new FoodEntry("SampleFoodName", 12,
                    new NutritionInfo(30, 30, 40)),
                new FoodEntry("AnotherSampleFoodName", 10,
                    new NutritionInfo(10, 10, 80))),
            this.dailyFoodDiary.getAllFoodEntries(),
            "Returned FoodEntry List must be equal to the expected one with two elements");*/

        assertTrue(List.of(new FoodEntry("SampleFoodName", 12,
                    new NutritionInfo(30, 30, 40)),
                new FoodEntry("AnotherSampleFoodName", 10,
                    new NutritionInfo(10, 10, 80))).containsAll
                (this.dailyFoodDiary.getAllFoodEntries()) &&
            this.dailyFoodDiary.getAllFoodEntries().containsAll(List.of(new FoodEntry("SampleFoodName", 12,
                        new NutritionInfo(30, 30, 40)),
                    new FoodEntry("AnotherSampleFoodName", 10,
                        new NutritionInfo(10, 10, 80)))),
            "Returned FoodEntry List must be equal to the expected one with two elements");

        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName");
        verify(nutritionInfoAPIMock).getNutritionInfo("AnotherSampleFoodName");
    }


    Set<FoodEntry> prepareExpectedForTestGetAllFoodEntriesSuccessfulWithDifferentMealsAtEachTime() {

        Set<FoodEntry> expected = new HashSet<>();

        expected.add(new FoodEntry("SampleFoodName1", 1,
            new NutritionInfo(10.4, 10.6, 79)));

        expected.add(new FoodEntry("SampleFoodName2", 2,
            new NutritionInfo(10, 10, 80)));

        expected.add(new FoodEntry("SampleFoodName3", 3,
            new NutritionInfo(0, 0, 100)));

        return expected;
    }

    @Test
    void testGetAllFoodEntriesSuccessfulWithDifferentMealsAtEachTime() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName1")).
            thenReturn(new NutritionInfo(10.4, 10.6, 79));

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName2")).
            thenReturn(new NutritionInfo(10, 10, 80));

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName3")).
            thenReturn(new NutritionInfo(0, 0, 100));

        this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName1", 1);
        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName2", 2);
        this.dailyFoodDiary.addFood(Meal.DINNER, "SampleFoodName3", 3);

        assertIterableEquals(Set.copyOf
                (prepareExpectedForTestGetAllFoodEntriesSuccessfulWithDifferentMealsAtEachTime()),
            this.dailyFoodDiary.getAllFoodEntries(),
            "Returned three food entries are not the same as the expected ones");

        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName1");
        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName2");
        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName3");
    }


    Set<FoodEntry> prepareExpectedForTestGetAllFoodEntriesSuccessfulWithEqualsMeals() {

        Set<FoodEntry> expected = new HashSet<>();

        expected.add(new FoodEntry("SampleFoodName1", 1,
            new NutritionInfo(10.4, 10.6, 79)));

        expected.add(new FoodEntry("SampleFoodName2", 2,
            new NutritionInfo(10, 10, 80)));

        return expected;
    }

    @Test
    void testGetAllFoodEntriesSuccessfulWithEqualsMeals() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName1")).
            thenReturn(new NutritionInfo(10.4, 10.6, 79));

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName2")).
            thenReturn(new NutritionInfo(10, 10, 80));

        this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName1", 1);
        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName2", 2);
        this.dailyFoodDiary.addFood(Meal.DINNER, "SampleFoodName2", 2);

        assertIterableEquals(Set.copyOf
                (prepareExpectedForTestGetAllFoodEntriesSuccessfulWithEqualsMeals()),
            this.dailyFoodDiary.getAllFoodEntries(),
            "Returned two food entries are not the same as the expected ones");

        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName1");
        verify(nutritionInfoAPIMock, times(2)).getNutritionInfo("SampleFoodName2");
    }

    Set<FoodEntry> prepareExpectedForTestGetAllFoodEntriesSuccessfulWithoutOneMeal() {

        Set<FoodEntry> expected = new HashSet<>();

        expected.add(new FoodEntry("SampleFoodName1", 1,
            new NutritionInfo(10.4, 10.6, 79)));

        expected.add(new FoodEntry("SampleFoodName2", 2,
            new NutritionInfo(10, 10, 80)));

        return expected;
    }

    @Test
    void testGetAllFoodEntriesSuccessfulWithoutOneMeal() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName1")).
            thenReturn(new NutritionInfo(10.4, 10.6, 79));

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName2")).
            thenReturn(new NutritionInfo(10, 10, 80));

        this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName1", 1);
        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName2", 2);

        assertIterableEquals(Set.copyOf
                (prepareExpectedForTestGetAllFoodEntriesSuccessfulWithoutOneMeal()),
            this.dailyFoodDiary.getAllFoodEntries(),
            "Returned two food entries are not the same as the expected ones");

        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName1");
        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName2");
    }

    List<FoodEntry> prepareExpectedForTestGetAllFoodEntriesByProteinContentSuccessfullySorted() {

        List<FoodEntry> expected = new ArrayList<>();

        expected.add(new FoodEntry("SampleFoodName1", 1,
            new NutritionInfo(10.4, 70.6, 19)));

        expected.add(new FoodEntry("SampleFoodName2", 2,
            new NutritionInfo(10, 10, 80)));

        return expected;
    }

    @Test
    void testGetAllFoodEntriesByProteinContentSuccessfullySorted() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName1")).
            thenReturn(new NutritionInfo(10.4, 70.6, 19));

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName2")).
            thenReturn(new NutritionInfo(10, 10, 80));

        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName2", 2);
        this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName1", 1);

        assertIterableEquals(List.copyOf
                (prepareExpectedForTestGetAllFoodEntriesByProteinContentSuccessfullySorted()),
            this.dailyFoodDiary.getAllFoodEntriesByProteinContent(),
            "Returned two food entries are not the same as the expected ones");

        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName1");
        verify(nutritionInfoAPIMock).getNutritionInfo("SampleFoodName2");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealMealNull() {

        assertThrows(IllegalArgumentException.class,
            () -> dailyFoodDiary.getDailyCaloriesIntakePerMeal(null),
            "Meal must cannot be null");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealNoFoodForMeal() {

        assertEquals(0.0, dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.SNACKS),
            0.001, "Expected 0.0 as there is not registered food entry for the meal");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealSuccessfulCalculation() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName1")).
            thenReturn(new NutritionInfo(10.4, 70.6, 19));

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName2")).
            thenReturn(new NutritionInfo(10, 10, 80));

        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName2", 2);
        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName1", 1);
        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName1", 1);
        this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName1", 1);

        assertEquals(2406, dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.LUNCH),
            0.001, "Calculated calories are not the  same as expected");
    }

    @Test
    void testGetDailyCaloriesIntakeNoFood() {

        assertEquals(0.0, dailyFoodDiary.getDailyCaloriesIntake(),
            0.001, "Expected 0.0 as there is not registered food entry at all");
    }

    @Test
    void testGetDailyCaloriesIntakeWithoutAllMeals() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName1")).
            thenReturn(new NutritionInfo(10.4, 70.6, 19));

        when(nutritionInfoAPIMock.getNutritionInfo("SampleFoodName2")).
            thenReturn(new NutritionInfo(10, 10, 80));

        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName2", 2);
        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName1", 1);
        this.dailyFoodDiary.addFood(Meal.LUNCH, "SampleFoodName1", 1);
        this.dailyFoodDiary.addFood(Meal.BREAKFAST, "SampleFoodName1", 1);

        assertEquals(3159, dailyFoodDiary.getDailyCaloriesIntake(),
            0.001, "Expected total calories differ from the result");
    }

}
