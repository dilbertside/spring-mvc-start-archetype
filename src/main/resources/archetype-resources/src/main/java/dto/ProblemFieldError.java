package ${package}.dto;

import java.io.Serializable;

/**
 * Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 * 
 *
 */
@lombok.Data
@lombok.RequiredArgsConstructor
public class ProblemFieldError implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String objectName;

  private final String field;

  private final String message;

}
