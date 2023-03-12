package ir.jimsa.LovelyRecipes.shared;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {
    Logger logger = LoggerFactory.getLogger(Utils.class);

    private String generateRandomString() {
        logger.info("GenerateRandomString() has called");

        Random random = new SecureRandom();
        String alphabet = "0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        StringBuilder returnValue = new StringBuilder();
        int length = Constants.PUBLIC_ID_LENGTH;
        for (int i = 0; i < length; i++) {
            returnValue.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return returnValue.toString();
    }

    public String getPublicId() {
        // idea: creating public method as a wrapper to make it testable
        return generateRandomString();
    }

}
