package org.nada.models;
// Generated Mar 5, 2020, 7:25:33 PM by Hibernate Tools 5.2.5.Final


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
 * MontoDeducibleConceptoFacturaVigente generated by hbm2java
 */
@Entity
@Table(name="monto_deducible_concepto_factura_vigente"
    ,schema="public"
)
public class MontoDeducibleConceptoFacturaVigente  implements java.io.Serializable {


     private Integer id;
     private Double monto;
     private Date tiempoCreacion;
     private Integer idConceptoFactura;
     private Date ultimoCreado;

    public MontoDeducibleConceptoFacturaVigente() {
    }

    public MontoDeducibleConceptoFacturaVigente(Double monto, Date tiempoCreacion, Integer idConceptoFactura, Date ultimoCreado) {
       this.monto = monto;
       this.tiempoCreacion = tiempoCreacion;
       this.idConceptoFactura = idConceptoFactura;
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

    
    @Column(name="monto", precision=17, scale=17)
    public Double getMonto() {
        return this.monto;
    }
    
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="tiempo_creacion", length=29)
    public Date getTiempoCreacion() {
        return this.tiempoCreacion;
    }
    
    public void setTiempoCreacion(Date tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

    
    @Column(name="id_concepto_factura")
    public Integer getIdConceptoFactura() {
        return this.idConceptoFactura;
    }
    
    public void setIdConceptoFactura(Integer idConceptoFactura) {
        this.idConceptoFactura = idConceptoFactura;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ultimo_creado", length=29)
    public Date getUltimoCreado() {
        return this.ultimoCreado;
    }
    
    public void setUltimoCreado(Date ultimoCreado) {
        this.ultimoCreado = ultimoCreado;
    }




}


