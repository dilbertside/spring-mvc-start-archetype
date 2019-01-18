package ${package}.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * An authority (a security role) used by Spring Security.
 */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@Entity
@Table(name = "auth")
public class Authority implements Serializable {

  private static final long serialVersionUID = 4826741868037091278L;

  @NotNull
  @Size(min = 0, max = 50)
  @Id
  @Column(length = 50)
  private String name;

  @Transient
  public String getNameReadable() {
    return StringUtils.removeStart(name, "ROLE_");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Authority authority = (Authority) o;

    if (name != null ? !name.equals(authority.name) : authority.name != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Authority{" + "name='" + name + '\'' + "}";
  }
}
