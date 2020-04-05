package org.nada.models;
// Generated Mar 9, 2020, 6:42:38 PM by Hibernate Tools 5.2.5.Final


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ImpuestoMontoDeducibleConceptoFactura generated by hbm2java
 */
@Entity
@Table(name="impuesto_monto_deducible_concepto_factura"
    ,schema="public"
)
public class ImpuestoMontoDeducibleConceptoFactura  implements java.io.Serializable {


     private Integer id;
     private MontoDeducibleConceptoFactura montoDeducibleConceptoFactura;
     private Double base;
     private String impuesto;
     private String tipoFactor;
     private Double tasaCuota;
     private Double importe;
     private String tipo;

    public ImpuestoMontoDeducibleConceptoFactura() {
    }

    public ImpuestoMontoDeducibleConceptoFactura(Integer id, MontoDeducibleConceptoFactura montoDeducibleConceptoFactura, Double base, String impuesto, String tipoFactor, Double tasaCuota, Double importe, String tipo) {
       this.id = id;
       this.montoDeducibleConceptoFactura = montoDeducibleConceptoFactura;
       this.base = base;
       this.impuesto = impuesto;
       this.tipoFactor = tipoFactor;
       this.tasaCuota = tasaCuota;
       this.importe = importe;
       this.tipo = tipo;
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
    @JoinColumn(name="id_monto_deducible_concepto", nullable=false)
    public MontoDeducibleConceptoFactura getMontoDeducibleConceptoFactura() {
        return this.montoDeducibleConceptoFactura;
    }
    
    public void setMontoDeducibleConceptoFactura(MontoDeducibleConceptoFactura montoDeducibleConceptoFactura) {
        this.montoDeducibleConceptoFactura = montoDeducibleConceptoFactura;
    }

    
    @Column(name="base", nullable=false, precision=17, scale=17)
    public Double getBase() {
        return this.base;
    }
    
    public void setBase(Double base) {
        this.base = base;
    }

    
    @Column(name="impuesto", nullable=false, length=10)
    public String getImpuesto() {
        return this.impuesto;
    }
    
    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    
    @Column(name="tipo_factor", nullable=false)
    public String getTipoFactor() {
        return this.tipoFactor;
    }
    
    public void setTipoFactor(String tipoFactor) {
        this.tipoFactor = tipoFactor;
    }

    
    @Column(name="tasa_cuota", nullable=false, precision=17, scale=17)
    public Double getTasaCuota() {
        return this.tasaCuota;
    }
    
    public void setTasaCuota(Double tasaCuota) {
        this.tasaCuota = tasaCuota;
    }

    
    @Column(name="importe", nullable=false, precision=17, scale=17)
    public Double getImporte() {
        return this.importe;
    }
    
    public void setImporte(Double importe) {
        this.importe = importe;
    }

    
    @Column(name="tipo", nullable=false)
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }




}


