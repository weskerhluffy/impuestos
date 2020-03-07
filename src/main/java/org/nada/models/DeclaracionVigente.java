package org.nada.models;
// Generated Mar 7, 2020, 10:58:02 AM by Hibernate Tools 5.2.5.Final


import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DeclaracionVigente generated by hbm2java
 */
@Entity
@Table(name="declaracion_vigente"
    ,schema="public"
)
public class DeclaracionVigente  implements java.io.Serializable {


     private Integer id;
     private Date periodo;
     private Date tiempoCreacion;
     private Date ultimoCreado;

    public DeclaracionVigente() {
    }

    public DeclaracionVigente(Date periodo, Date tiempoCreacion, Date ultimoCreado) {
       this.periodo = periodo;
       this.tiempoCreacion = tiempoCreacion;
       this.ultimoCreado = ultimoCreado;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    
    @Column(name="periodo", length=13)
    public Date getPeriodo() {
        return this.periodo;
    }
    
    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="tiempo_creacion", length=29)
    public Date getTiempoCreacion() {
        return this.tiempoCreacion;
    }
    
    public void setTiempoCreacion(Date tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ultimo_creado", length=29)
    public Date getUltimoCreado() {
        return this.ultimoCreado;
    }
    
    public void setUltimoCreado(Date ultimoCreado) {
        this.ultimoCreado = ultimoCreado;
    }



  // The following is extra code specified in the hbm.xml files

        
// Aca nomas probando controla la autodegeneracion con <<class-code >>
// TODO: https://stackoverflow.com/questions/2653268/what-are-the-shortcut-to-auto-generating-tostring-method-in-eclipse
public String toString() { return ToStringBuilder.reflectionToString(this); }

	private Set<DeclaracionFactura> declaracionFacturas;

	@OneToMany(mappedBy = "declaracionVigente", targetEntity = DeclaracionFactura.class)
	public Set<DeclaracionFactura> getDeclaracionFacturas() {
		return declaracionFacturas;
	}

	public void setDeclaracionFacturas(Set<DeclaracionFactura> declaracionFacturas) {
		this.declaracionFacturas = declaracionFacturas;
	}

        
		
  // end of extra code specified in the hbm.xml files

}


