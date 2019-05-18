package org.nada.models;
// Generated 18 may 2019 8:23:10 by Hibernate Tools 5.2.5.Final


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DeclaracionFactura generated by hbm2java
 */
@Entity
@Table(name="declaracion_factura"
    ,schema="public"
)
public class DeclaracionFactura  implements java.io.Serializable {


     private Integer id;
     private Declaracion declaracion;
     private Factura factura;
     private FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura;
     private MontoDeducibleFactura montoDeducibleFactura;
     private MontoFactura montoFactura;
     private PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura;
     private Date tiempoCreacion;

    public DeclaracionFactura() {
    }

	
    public DeclaracionFactura(Integer id, Declaracion declaracion, Factura factura, MontoFactura montoFactura, Date tiempoCreacion) {
        this.id = id;
        this.declaracion = declaracion;
        this.factura = factura;
        this.montoFactura = montoFactura;
        this.tiempoCreacion = tiempoCreacion;
    }
    public DeclaracionFactura(Integer id, Declaracion declaracion, Factura factura, FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura, MontoDeducibleFactura montoDeducibleFactura, MontoFactura montoFactura, PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura, Date tiempoCreacion) {
       this.id = id;
       this.declaracion = declaracion;
       this.factura = factura;
       this.fechaInicioDepreciacionFactura = fechaInicioDepreciacionFactura;
       this.montoDeducibleFactura = montoDeducibleFactura;
       this.montoFactura = montoFactura;
       this.porcentajeDepreciacionAnualFactura = porcentajeDepreciacionAnualFactura;
       this.tiempoCreacion = tiempoCreacion;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_declaracion", nullable=false)
    public Declaracion getDeclaracion() {
        return this.declaracion;
    }
    
    public void setDeclaracion(Declaracion declaracion) {
        this.declaracion = declaracion;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_factura", nullable=false)
    public Factura getFactura() {
        return this.factura;
    }
    
    public void setFactura(Factura factura) {
        this.factura = factura;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_fecha_inicio_depreciacion")
    public FechaInicioDepreciacionFactura getFechaInicioDepreciacionFactura() {
        return this.fechaInicioDepreciacionFactura;
    }
    
    public void setFechaInicioDepreciacionFactura(FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura) {
        this.fechaInicioDepreciacionFactura = fechaInicioDepreciacionFactura;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_monto_deducible")
    public MontoDeducibleFactura getMontoDeducibleFactura() {
        return this.montoDeducibleFactura;
    }
    
    public void setMontoDeducibleFactura(MontoDeducibleFactura montoDeducibleFactura) {
        this.montoDeducibleFactura = montoDeducibleFactura;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_monto", nullable=false)
    public MontoFactura getMontoFactura() {
        return this.montoFactura;
    }
    
    public void setMontoFactura(MontoFactura montoFactura) {
        this.montoFactura = montoFactura;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_porcentaje_depreciacion")
    public PorcentajeDepreciacionAnualFactura getPorcentajeDepreciacionAnualFactura() {
        return this.porcentajeDepreciacionAnualFactura;
    }
    
    public void setPorcentajeDepreciacionAnualFactura(PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura) {
        this.porcentajeDepreciacionAnualFactura = porcentajeDepreciacionAnualFactura;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="tiempo_creacion", nullable=false, length=29)
    public Date getTiempoCreacion() {
        return this.tiempoCreacion;
    }
    
    public void setTiempoCreacion(Date tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }




}


