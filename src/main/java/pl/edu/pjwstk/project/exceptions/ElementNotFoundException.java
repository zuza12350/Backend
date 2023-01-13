package pl.edu.pjwstk.project.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Definition of an exception, in case the element is not found. It throws a status of 404.
 *
 * @author Zuzanna Borkowska
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ElementNotFoundException extends RuntimeException{
}
