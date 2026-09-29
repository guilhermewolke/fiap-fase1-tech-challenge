package com.github.techChallenge.application.exceptions;

import com.github.techChallenge.shared.ConflictException;
import com.github.techChallenge.shared.NotFoundException;
import com.github.techChallenge.shared.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ProblemDetailFactory problemDetailFactory;

    public GlobalExceptionHandler(
            ProblemDetailFactory problemDetailFactory
    ) {
        this.problemDetailFactory = problemDetailFactory;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(
            UserNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Usuário não encontrado",
                exception.getMessage(),
                exception.getErrorCode(),
                request
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateEmail(
            DuplicateEmailException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "E-mail já cadastrado",
                exception.getMessage(),
                exception.getErrorCode(),
                request
        );
    }

    @ExceptionHandler(DuplicateLoginException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateLogin(
            DuplicateLoginException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Login já cadastrado",
                exception.getMessage(),
                exception.getErrorCode(),
                request
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCredentials(
            InvalidCredentialsException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas",
                exception.getMessage(),
                exception.getErrorCode(),
                request
        );
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ProblemDetail> handleInvalidPassword(
            InvalidPasswordException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Senha inválida",
                exception.getMessage(),
                exception.getErrorCode(),
                request
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflict(
            ConflictException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                exception.getTitle(),
                exception.getMessage(),
                exception.getCode(),
                request
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
            NotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                exception.getTitle(),
                exception.getMessage(),
                exception.getCode(),
                request
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorized(
            UnauthorizedException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getTitle(),
                exception.getMessage(),
                exception.getCode(),
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> violations = new LinkedHashMap<>();

        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                    violations.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                    )
                );

        ProblemDetail problemDetail = problemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "Dados inválidos",
                "Um ou mais campos da requisição são inválidos.",
                "VALIDATION_ERROR",
                ((ServletWebRequest) request).getRequest()
        );

        problemDetail.setProperty("violations", violations);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        logger.warn(
                "Uma restrição de integridade do banco foi violada.",
                exception
        );

        return buildResponse(
                HttpStatus.CONFLICT,
                "Conflito de dados",
                "A operação viola uma restrição de integridade dos dados.",
                "DATA_INTEGRITY_CONFLICT",
                request
        );
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.valueOf(statusCode.value());

        String title;
        String detail;

        switch (status) {
            case NOT_FOUND -> {
                title = "Recurso não encontrado";
                detail = "O endereço solicitado não existe nesta API.";
            }
            case METHOD_NOT_ALLOWED -> {
                title = "Método não suportado";
                detail = "Este endereço não aceita o método HTTP utilizado.";
            }
            case UNSUPPORTED_MEDIA_TYPE -> {
                title = "Formato não suportado";
                detail = "O tipo de conteúdo enviado não é aceito por este endereço.";
            }
            default -> {
                title = "Requisição inválida";
                detail = "A requisição possui parâmetros ou corpo inválidos.";
            }
        }

        ProblemDetail problemDetail = problemDetailFactory.create(
                status,
                title,
                detail,
                status.name(),
                ((ServletWebRequest) request).getRequest()
        );

        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        logger.error(
                "Erro inesperado ao processar a requisição.",
                exception
        );

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                "Ocorreu um erro inesperado ao processar a solicitação.",
                "INTERNAL_SERVER_ERROR",
                request
        );
    }

    private ResponseEntity<ProblemDetail> buildResponse(
            HttpStatus status,
            String title,
            String detail,
            String code,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = problemDetailFactory.create(
                status,
                title,
                detail,
                code,
                request
        );

        return ResponseEntity
                .status(status)
                .body(problemDetail);
    }
}