package ir.jimsa.LovelyRecipes.config.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
public class SystemServiceException extends RuntimeException {

    private String exception;
    private HttpStatus httpStatus;

    public SystemServiceException(String exception, HttpStatus httpStatus) {
        super(exception);
        this.exception = exception;
        this.httpStatus = httpStatus;
    }
}
