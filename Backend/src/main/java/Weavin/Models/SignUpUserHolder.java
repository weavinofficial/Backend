package Weavin.Models;

import Weavin.Enums.Field;
import lombok.Data;

@Data
public class SignUpUserHolder {
    
    private String username;

    private String email;

    private String password;

    private Field field;
}
