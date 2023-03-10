package ir.jimsa.LovelyRecipes.shared;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test the general Util class")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UtilsTest {
    private static Utils utils;

    @BeforeAll
    static void setup() {
        utils = new Utils();
    }

    @Order(1)
    @RepeatedTest(5)
    @DisplayName("testing publicId generator")
    void testGetPublicId_WhenInputAny_ShouldReturnString() {
        assertNotNull(utils.getPublicId());
        assertEquals(Constants.PUBLIC_ID_LENGTH, utils.getPublicId().length());
    }
}