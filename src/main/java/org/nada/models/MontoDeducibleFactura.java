package org.nada.models;
// Generated Mar 5, 2020, 7:25:33 PM by Hibernate Tools 5.2.5.Final


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MontoDeducibleFactura generated by hbm2java
 */
@Entity
@Table(name="monto_deducible_factura"
    ,schema="public"
)
public class MontoDeducibleFactura  implements java.io.Serializable {


     private Integer id;
     private Factura factura;
     private Double monto;
     private Date tiempoCreacion;
     private Set<DeclaracionFactura> declaracionFacturas = new HashSet<DeclaracionFactura>(0);

    public MontoDeducibleFactura() {
    }

	
    public MontoDeducibleFactura(Double monto, Date tiempoCreacion) {
        this.monto = monto;
        this.tiempoCreacion = tiempoCreacion;
    }
    public MontoDeducibleFactura(Factura factura, Double monto, Date tiempoCreacion, Set<DeclaracionFactura> declaracionFacturas) {
       this.factura = factura;
       this.monto = monto;
       this.tiempoCreacion = tiempoCreacion;
       this.declaracionFacturas = declaracionFacturas;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
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

    
    @Column(name="monto", nullable=false, precision=17, scale=17)
    public Double getMonto() {
        return this.monto;
    }
    
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="tiempo_creacion", nullable=false, length=29)
    public Date getTiempoCreacion() {
        return this.tiempoCreacion;
    }
    
    public void setTiempoCreacion(Date tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="montoDeducibleFactura")
    public Set<DeclaracionFactura> getDeclaracionFacturas() {
        return this.declaracionFacturas;
    }
    
    public void setDeclaracionFacturas(Set<DeclaracionFactura> declaracionFacturas) {
        this.declaracionFacturas = declaracionFacturas;
    }




}


