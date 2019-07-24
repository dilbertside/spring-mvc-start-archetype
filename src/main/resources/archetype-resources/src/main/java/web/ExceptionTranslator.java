#set( $dollar = '$' )
#set( $pound = '#' )
#set( $escape = '\' )
package ${package}.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import com.google.common.base.Throwables;
import ${package}.dto.ProblemFieldError;
import ${package}.web.support.ErrorConstants;

/**
 * General error handler for the application.
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

  /**
   * Handle exceptions thrown by handlers.
   */
  @ExceptionHandler(value = Exception.class)  
  public ModelAndView exception(Exception exception, WebRequest request) {
    ModelAndView modelAndView = new ModelAndView("error/general");
    modelAndView.addObject("errorMessage", Throwables.getRootCause(exception));
    return modelAndView;
  }
  
  /**
   * Post-process Problem payload to add the message key for front-end if needed
   */
  @Override
  public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
    if (entity == null || entity.getBody() == null) {
      return entity;
    }
    Problem problem = entity.getBody();
    if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
      return entity;
    }
    ProblemBuilder builder = Problem.builder()
        .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
        .withStatus(problem.getStatus())
        .withTitle(problem.getTitle())
        .with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());

    if (problem instanceof ConstraintViolationProblem) {
      builder.with("violations", ((ConstraintViolationProblem) problem).getViolations()).with("message", ErrorConstants.ERR_VALIDATION);
      return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    } else {
      builder
      .withCause(((DefaultProblem) problem).getCause())
      .withDetail(problem.getDetail())
        .withInstance(problem.getInstance());
      problem.getParameters().forEach(builder::with);
      if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
        builder.with("message", "error.http." + problem.getStatus().getStatusCode());
      }
      return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }
  }

  @Override
  public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
    BindingResult result = ex.getBindingResult();
    List<ProblemFieldError> fieldErrors = result.getFieldErrors().stream().map(f -> new ProblemFieldError(f.getObjectName(), f.getField(), f.getCode()))
        .collect(Collectors.toList());

    Problem problem = Problem.builder()
        .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
        .withTitle("Method argument not valid")
        .withStatus(defaultConstraintViolationStatus())
        .with("message", ErrorConstants.ERR_VALIDATION)
        .with("fieldErrors", fieldErrors)
        .build();
    return create(ex, problem, request);
  }

  @ExceptionHandler(ServletRequestBindingException.class)
  public ResponseEntity<Problem> handleServletRequestBinding(final ServletRequestBindingException exception, final NativeWebRequest request) {
    return create(Status.BAD_REQUEST, exception, request);
  }

  /* (non-Javadoc)
   * @see org.zalando.problem.spring.web.advice.http.MethodNotAllowedAdviceTrait${pound}handleRequestMethodNotSupportedException(
   * org.springframework.web.HttpRequestMethodNotSupportedException, org.springframework.web.context.request.NativeWebRequest)
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @Override
  public ResponseEntity<Problem> handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, NativeWebRequest request) {
    
    return ProblemHandling.super.handleRequestMethodNotSupportedException(exception, request);
  }
  
  /*
   * @ExceptionHandler(BadRequestAlertException.class)
   * public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
   * return create(ex, request, HeaderUtil.createFailureAlert(ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
   * }
   */

  /*
   * @ExceptionHandler(ConcurrencyFailureException.class)
   * public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
   * Problem problem = Problem.builder()
   * .withStatus(Status.CONFLICT)
   * .with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE)
   * .build();
   * return create(ex, problem, request);
   * }
   */
}
