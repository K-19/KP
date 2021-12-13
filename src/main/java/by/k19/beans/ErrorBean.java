package by.k19.beans;

import lombok.Data;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@ViewScoped
public class ErrorBean implements Serializable {
    private String message;
}
