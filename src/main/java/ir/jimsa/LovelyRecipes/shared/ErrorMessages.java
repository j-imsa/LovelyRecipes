package ir.jimsa.LovelyRecipes.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record with provided id is not found"),
    AUTHENTICATION_FAILED("Authentication failed"),
    INVALID_INPUT_VALUE("Input has some invalid parameters"),
    DATABASE_IO_EXCEPTION("Could not read/write into database"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    COULD_NOT_READ_RECORD("Could not read from device"),
    COULD_NOT_DELETE_RECORD("Could not delete record");

    @Setter
    @Getter
    private String errorMessage;
}
