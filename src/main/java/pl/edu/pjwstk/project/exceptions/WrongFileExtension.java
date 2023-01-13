package pl.edu.pjwstk.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Definition of an exception, in case the element is not found. It throws a status of 400.
 *
 * @author Zuzanna Borkowska
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongFileExtension extends RuntimeException{
}
