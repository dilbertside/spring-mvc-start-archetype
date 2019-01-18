package ${package}.dto;

import java.io.Serializable;

/**
 * Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 * 
 *
 */
@lombok.Data
public class ProblemFieldError implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String objectName;

  private final String field;

  private final String message;

  /**
   * 
   * @param dto to set
   * @param field to set
   * @param message to set
   */
  public ProblemFieldError(String dto, String field, String message) {
    this.objectName = dto;
    this.field = field;
    this.message = message;
  }

}
