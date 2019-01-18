package ${package}.dto;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.Valid;
import javax.validation.constraints.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class UserDto {

  @NotNull
  //@Pattern(regexp = SecurityUtils.LOGIN_REGEX)
  @Size(min = 1, max = 50)
  private String login;

  @Size(max = 50)
  private String firstName;

  @Size(max = 50)
  private String lastName;

  @Email
  @Size(min = 5, max = 100)
  private String email;

  private boolean activated = false;
  
  @Size(min = 5, max = 40)
  private String password;

  @Size(min = 2, max = 5)
  private String langKey;

  private Set<String> authorities = new HashSet<>();
  
  @ToStringExclude
  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  /**
   * 
   * @param login to set
   * @param firstName to set
   * @param lastName to set
   * @param email to set
   * @param activated to set
   * @param langKey to set
   * @param authorities to set
   */
  public UserDto(String login, String firstName, String lastName, String email, boolean activated, String langKey, Set<String> authorities) {
    this.login = login;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.activated = activated;
    this.langKey = langKey;
    this.authorities = authorities;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

}


