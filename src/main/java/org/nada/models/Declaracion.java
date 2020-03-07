package org.nada.models;
// Generated Mar 7, 2020, 10:58:02 AM by Hibernate Tools 5.2.5.Final


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
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

    public Declaracion() {
    }

	
    public Declaracion(Date periodo) {
        this.periodo = periodo;
    }
    public Declaracion(Date periodo, Date tiempoCreacion) {
       this.periodo = periodo;
       this.tiempoCreacion = tiempoCreacion;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
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




}


