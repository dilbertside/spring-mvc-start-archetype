#set( $dollar = '$' )
#set( $pound = '#' )
#set( $escape = '\' )
package ${package}.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Abstract base class for entities. Allows parameterization of id type, chooses auto-generation and implements {@link #equals(Object)} and {@link #hashCode()} based on that id. <br>
 * add jaxb extension
 * 
 */
@XmlTransient
@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

  @XmlTransient
  @ToStringExclude
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private PK id;

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Persistable#getId()
   */
  @Override
  public PK getId() {
    return this.id;
  }

  /**
   * Sets the id of the entity.
   * 
   * @param id
   *          the id to set
   */
  public void setId(final PK id) {
    this.id = id;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Persistable#isNew()
   */
  @JsonIgnore
  @Override
  public boolean isNew() {
    return null == getId();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object${pound}toString()
   */
  @Override
  public String toString() {
    return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
  }
  
  @Transient
  @JsonIgnore
  public String toStringFormated() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object${pound}equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (null == obj) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (!getClass().equals(obj.getClass())) {
      return false;
    }
    AbstractPersistable<?> that = (AbstractPersistable<?>) obj;
    return null == this.getId() ? false : this.getId().equals(that.getId());
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object${pound}hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}