package com.aamer.labs.TransactionAnalyser.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aamer Abbas
 */
@RestController
public class GlobalExceptionHandler implements ErrorController {

    public GlobalExceptionHandler() {
    }

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        HttpStatus httpStatus = getHttpStatus(request);
        String message = getErrorMessage(request, httpStatus);

        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("code", httpStatus.value());
        response.put("message", message);
        response.put("errors", Collections.singletonList(message));

        return ResponseEntity.status(httpStatus).body(response);
    }

    private HttpStatus getHttpStatus(HttpServletRequest request) {

        //get the standard error code set by Spring Context
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            return HttpStatus.valueOf(status);
        }

        // maybe we're the one that trigger the redirect
        // with the code param
        String code = request.getParameter("code");
        if (code != null && !code.isBlank()) {
            return HttpStatus.valueOf(code);
        }

        //default fallback
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String getErrorMessage(HttpServletRequest request, HttpStatus httpStatus) {

        //get the error message set by Spring context
        // and return it if it's not null
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if (message != null && !message.isEmpty()) {
            return message;
        }

        //if the default message is null,
        //let's construct a message based on the HTTP status
        switch (httpStatus) {
            case NOT_FOUND:
                message = "The resource does not exist";
                break;
            case INTERNAL_SERVER_ERROR:
                message = "Something went wrong internally. Please check the Request Body and try again.";
                break;
            case FORBIDDEN:
                message = "Permission denied";
                break;
            case TOO_MANY_REQUESTS:
                message = "Too many requests";
                break;
            default:
                message = httpStatus.getReasonPhrase();
        }

        return message;
    }
}
