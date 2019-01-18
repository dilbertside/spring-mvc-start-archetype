package ${package}.web.support;


import java.net.URI;

/**
 * used with Problem Spring Web a library that makes it easy to produce application/problem+json responses from a Spring application.
 * 
 *
 */
public final class ErrorConstants {
  private static String problemBaseUrl = "https://example.com/problem";

  public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
  public static final String ERR_VALIDATION = "error.validation";
  public static final URI DEFAULT_TYPE = URI.create(problemBaseUrl + "/problem-with-message");
  public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(problemBaseUrl + "/contraint-violation");
  public static final URI PARAMETERIZED_TYPE = URI.create(problemBaseUrl + "/parameterized");
  public static final URI INVALID_PASSWORD_TYPE = URI.create(problemBaseUrl + "/invalid-password");
  public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(problemBaseUrl + "/email-already-used");
  public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(problemBaseUrl + "/login-already-used");
  public static final URI EMAIL_NOT_FOUND_TYPE = URI.create(problemBaseUrl + "/email-not-found");

  private ErrorConstants() {
  }

  /**
   * @return the problemBaseUrl
   */
  public static String getProblemBaseUrl() {
    return problemBaseUrl;
  }

  /**
   * @param problemBaseUrl the problemBaseUrl to set
   */
  public static void setProblemBaseUrl(String problemBaseUrl) {
    ErrorConstants.problemBaseUrl = problemBaseUrl;
  }
}
