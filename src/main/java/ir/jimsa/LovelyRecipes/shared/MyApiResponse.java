package ir.jimsa.LovelyRecipes.shared;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "Response", description = "Api Response Model")
public class MyApiResponse {
    private boolean action;
    private String message;
    private Date date;
    private Object result;
}
