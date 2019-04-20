package org.nada.models;
// Generated 19 abr 2019 8:27:01 by Hibernate Tools 5.2.5.Final


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * FechaInicioDepreciacionFactura generated by hbm2java
 */
@Entity
@Table(name="fecha_inicio_depreciacion_factura"
    ,schema="public"
)
public class FechaInicioDepreciacionFactura  implements java.io.Serializable {


     private Integer id;
     private Factura factura;
     private Date fecha;
     private Date tiempoCreacion;

    public FechaInicioDepreciacionFactura() {
    }

	
    public FechaInicioDepreciacionFactura(Date fecha, Date tiempoCreacion) {
        this.fecha = fecha;
        this.tiempoCreacion = tiempoCreacion;
    }
    public FechaInicioDepreciacionFactura(Factura factura, Date fecha, Date tiempoCreacion) {
       this.factura = factura;
       this.fecha = fecha;
       this.tiempoCreacion = tiempoCreacion;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_factura")
    public Factura getFactura() {
        return this.factura;
    }
    
    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    
    @Column(name="fecha", nullable=false, length=13)
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="tiempo_creacion", nullable=false, length=29)
    public Date getTiempoCreacion() {
        return this.tiempoCreacion;
    }
    
    public void setTiempoCreacion(Date tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

    public String toString() { return ToStringBuilder.reflectionToString(this); }



}

