package bg.sofia.uni.fmi.mjt.netflix;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContentTest {

    @Test
    void testOfCorrectLineWithShow() {

        String line = "1,Sample Title,SHOW,Sample Description,2022,120,['crime';'mystery'],1,1,9.9,1003";

        assertEquals(new Content("1", "Sample Title", ContentType.SHOW, "Sample Description",
                2022, 120, List.of("crime", "mystery"), 1, "1", 9.9, 1003),
            Content.of(line), "Expected Content is not equal to the actual");
    }

    @Test
    void testOfCorrectLineWithMovie() {

        String line = "1,Sample Title,MOVIE,Sample Description,2022,120,['crime';'mystery'],1,1,9.9,1003";

        assertEquals(new Content("1", "Sample Title", ContentType.MOVIE, "Sample Description",
                2022, 120, List.of("crime", "mystery"), 1, "1", 9.9, 1003),
            Content.of(line), "Expected Content is not equal to the actual");
    }

}
