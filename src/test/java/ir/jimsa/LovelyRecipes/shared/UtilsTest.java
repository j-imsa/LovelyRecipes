package ir.jimsa.LovelyRecipes.shared;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test the general Util class")
class UtilsTest {

    @RepeatedTest(10)
    @DisplayName("testing publicId generator")
    void testGetPublicId_WhenInputAny_ShouldReturnString() {
        // Arrange (Given)
        Utils utils = new Utils();

        // Act (When)
        String generatedPublicId = utils.getPublicId();

        // Assert (Then)
        assertNotNull(generatedPublicId, "getPublicId returned null");
        assertEquals(Constants.PUBLIC_ID_LENGTH, generatedPublicId.length(), "getPublicId returned a bad-length string");
    }
}