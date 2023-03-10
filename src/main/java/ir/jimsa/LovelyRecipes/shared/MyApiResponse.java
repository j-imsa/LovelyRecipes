package ir.jimsa.LovelyRecipes.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MyApiResponse {
    private boolean action;
    private String message;
    private Date date;
    private Object result;
}
