package org.nada.models;
// Generated 18 may 2019 8:23:10 by Hibernate Tools 5.2.5.Final


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Declaracion generated by hbm2java
 */
@Entity
@Table(name="declaracion"
    ,schema="public"
)
public class Declaracion  implements java.io.Serializable {


     private Integer id;
     private Date periodo;
     private Date tiempoCreacion;
     private Set<DeclaracionFactura> declaracionFacturas = new HashSet<DeclaracionFactura>(0);

    public Declaracion() {
    }

	
    public Declaracion(Integer id, Date periodo) {
        this.id = id;
        this.periodo = periodo;
    }
    public Declaracion(Integer id, Date periodo, Date tiempoCreacion, Set<DeclaracionFactura> declaracionFacturas) {
       this.id = id;
       this.periodo = periodo;
       this.tiempoCreacion = tiempoCreacion;
       this.declaracionFacturas = declaracionFacturas;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    
    @Column(name="periodo", nullable=false, length=13)
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

@OneToMany(fetch=FetchType.LAZY, mappedBy="declaracion")
    public Set<DeclaracionFactura> getDeclaracionFacturas() {
        return this.declaracionFacturas;
    }
    
    public void setDeclaracionFacturas(Set<DeclaracionFactura> declaracionFacturas) {
        this.declaracionFacturas = declaracionFacturas;
    }




}

