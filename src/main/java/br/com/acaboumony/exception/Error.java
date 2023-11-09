package br.com.acaboumony.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@RestControllerAdvice
public class Error implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            errors.put("Codigo", HttpStatus.BAD_REQUEST.value());
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handleValidationExceptions(ConstraintViolationException ex) {

        Map<String, Object> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            errors.put("Mensagem", error.getMessage());
            errors.put("Codigo", HttpStatus.BAD_REQUEST.value());
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Map<String, Object> handleValidationNoSuchElementException(NoSuchElementException ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("Erro", ex.getMessage()+" Não foi encontrado");
        errors.put("Codigo", HttpStatus.NOT_FOUND.value());
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NumberFormatException.class)
    public Map<String, Object> handleValueNumberFormatException(NumberFormatException ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("Erro", "O formato de dado " + ex.getMessage() + " é inválido!");
        errors.put("Codigo", HttpStatus.NOT_FOUND.value());
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleValidationInternalException(Exception ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("Erro", "O endpoint falhou: " + ex.getMessage());
        errors.put("Codigo", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("Erro", "Falha de integridade de dados: " + ex.getCause().getCause().getMessage());
        errors.put("Codigo", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public Map<String, Object> handleSQLException(SQLException ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("Erro", "Falha SQL: " + ex.getMessage());
        errors.put("Codigo", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public Map<String, Object> handleIllegalStateException(IllegalStateException ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("Erro", "O endpoint falhou: " + ex.getMessage());
        errors.put("Codigo", HttpStatus.BAD_REQUEST.value());
        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityExistsException.class)
    public Map<String, Object> handleEntityExistsException(EntityExistsException ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("Erro", ex.getMessage());
        errors.put("Codigo", HttpStatus.BAD_REQUEST.value());
        return errors;
    }
}