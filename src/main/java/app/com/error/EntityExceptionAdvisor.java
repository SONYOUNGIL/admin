package app.com.error;

import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import app.com.util.Result;

@RestControllerAdvice
public class EntityExceptionAdvisor {
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getConstraintViolations().forEach(cv -> {
            String field = null;
            for (Node node : cv.getPropertyPath()) {
                field = node.getName();
            }
            sb.append(field + " : " + cv.getMessage() + System.lineSeparator());
        }); 
        Result result = new Result();
        result.setFailMsg(ex, sb.toString());
        return new ResponseEntity(result.getResult(), HttpStatus.OK);
    }
}
