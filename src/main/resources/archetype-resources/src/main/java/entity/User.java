package ${package}.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ${package}.web.support.SecurityUtils;

/**
 * A security user.
 */
@Entity
@Table(name = "usr")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class User extends AbstractAuditingEntity implements UserDetails, Serializable {

  private static final long serialVersionUID = 7983199314952469321L;

  @ToStringExclude
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Pattern(regexp = SecurityUtils.LOGIN_REGEX)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(length = 50, unique = true, nullable = false)
  private String login;

  @ToStringExclude
  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 255)
  @Column(name = "pwd_hash", length = 255, nullable = false)
  private String password;

  @Size(max = 50)
  @Column(name = "fn", length = 50)
  private String firstName;

  @Size(max = 50)
  @Column(name = "ln", length = 50)
  private String lastName;

  @Email
  @Size(max = 100)
  @Column(length = 100, unique = true, nullable = false)
  private String email;

  @NotNull
  @Column(nullable = false)
  private boolean activated = false;

  @Size(min = 2, max = 5)
  @Column(name = "lang_key", length = 5)
  private String langKey;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
    name = "usr_auth", 
    joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, 
    inverseJoinColumns = {@JoinColumn(name = "auth_name", referencedColumnName = "name") }
  )
  private Set<Authority> roles = new HashSet<>();
  
  @Column(name = "creexp")
  protected Boolean credentialsExpired = false;
  
  @JsonIgnore
  @Column(name = "falo")
  protected Integer failedLogins;
  
  /**
   * null if not locked, locking date will be set to lock the user
   */
  @JsonIgnore
  @Column(name = "dtloou")
  protected Instant lockedOut;
  
  public User(String login, String email, String password, Set<Authority> authorities) {
    this.login = login;
    this.email = email;
    this.password = password;
    this.roles = authorities;
  }

  // Lowercase the login before saving it in database
  public void setLogin(String login) {
    this.login = login.toLowerCase(Locale.ENGLISH);
  }

  @Transient
  public boolean hasAuthority(Authority authority) {
    return this.getRoles().stream().anyMatch(a -> a.getName().equalsIgnoreCase(authority.getName()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    if (!login.equals(user.login)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  
  @Transient
  public String toStringFormated() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  @Transient
  @Override
  public String getUsername() {
    return login;
  }

  @Transient
  @Override
  public boolean isAccountNonExpired() {
    return !activated;
  }

  @Transient
  @Override
  public boolean isAccountNonLocked() {
    return null == lockedOut;
  }

  @Transient
  @Override
  public boolean isCredentialsNonExpired() {
    return !credentialsExpired;
  }

  @Transient
  @Override
  public boolean isEnabled() {
    return activated;
  }

  @Transient
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.getRoles()
        .stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return password;
  }
  
  @Transient
  public Locale getLocale() {
    return Locale.forLanguageTag(ObjectUtils.firstNonNull(langKey, "en"));
  }
}
