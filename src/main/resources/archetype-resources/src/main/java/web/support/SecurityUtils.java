/**
 * SecurityUtils
 */
package ${package}.web.support;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author lch on Nov 2, 2016 10:09:25 AM
 * @since 0.1.4
 * @version 1.0.0
 *
 */
public class SecurityUtils {

  private SecurityUtils() {
    // defeat instantiation
  }

  public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
  
  public static final String ADMIN = "ROLE_ADMIN";

  public static final String USER = "ROLE_USER";

  public static final String ANONYMOUS = "ROLE_ANONYMOUS";
  
  public static final String SYSADMIN = "ROLE_SYSADMIN";
  
  public static final String API = "ROLE_API";
  
  public static final String SYSTEM_ACCOUNT = "system";

  /**
   * extract from org.springframework.boot.context.properties.ConfigurationProperties a triplet of multiple values for configurable creation of security users
   * <br>
   * for roles if the list is not equal will use the first one
   * @param userNames {@link Set} of user name
   * @param userPasswords {@link Set} of user password
   * @param userRoles {@link Set} set of roles
   * @param i number in the list to retrieve
   * @return to {@link Triple} of username, password and roles array
   */
  public static Triple<String, String, String[]> extractUser(Set<String> userNames, Set<String> userPasswords, String userRoles, int i) {
    String username = (String) userNames.toArray()[i];
    String pw = (String) userPasswords.toArray()[i];
    String[] roles = StringUtils.splitPreserveAllTokens(StringUtils.splitPreserveAllTokens(userRoles, ',')[i], '|');
    return Triple.of(username, pw, roles);
  }
  
  /**
   * Get the login of the current user.
   *
   * @return the login of the current user
   */
  public static String getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    String userName = null;
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        userName = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
        userName = (String) authentication.getPrincipal();
      } /*else if (authentication.getPrincipal() instanceof LdapUserDetails) {
        LdapUserDetails ldapUser = (LdapUserDetails) authentication.getPrincipal();
        return ldapUser.getUsername();
      }*/
    }
    return userName;
  }

  /**
   * Check if a user is authenticated.
   *
   * @return true if the user is authenticated, false otherwise
   */
  public static boolean isAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null) {
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      if (authorities != null) {
        for (GrantedAuthority authority : authorities) {
          if (authority.getAuthority().equals(ANONYMOUS)) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }

  /**
   * If the current user has a specific authority (security role).
   *
   * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
   *
   * @param authority the authority to check
   * @return true if the current user has the authority, false otherwise
   */
  public static boolean isCurrentUserInRole(String authority) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
      }
    }
    return false;
  }
}
