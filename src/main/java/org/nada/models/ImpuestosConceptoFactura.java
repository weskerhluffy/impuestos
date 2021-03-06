package org.nada.models;
// Generated Mar 9, 2020, 6:42:38 PM by Hibernate Tools 5.2.5.Final


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ImpuestosConceptoFactura generated by hbm2java
 */
@Entity
@Table(name="impuestos_concepto_factura"
    ,schema="public"
)
public class ImpuestosConceptoFactura  implements java.io.Serializable {


     private Integer id;
     private ConceptoFactura conceptoFactura;
     private Double base;
     private String impuesto;
     private String tipoFactor;
     private Double tasaCuota;
     private Double importe;
     private String tipo;

    public ImpuestosConceptoFactura() {
    }

	
    public ImpuestosConceptoFactura(ConceptoFactura conceptoFactura, Double base, String impuesto, Double tasaCuota, Double importe) {
        this.conceptoFactura = conceptoFactura;
        this.base = base;
        this.impuesto = impuesto;
        this.tasaCuota = tasaCuota;
        this.importe = importe;
    }
    public ImpuestosConceptoFactura(ConceptoFactura conceptoFactura, Double base, String impuesto, String tipoFactor, Double tasaCuota, Double importe, String tipo) {
       this.conceptoFactura = conceptoFactura;
       this.base = base;
       this.impuesto = impuesto;
       this.tipoFactor = tipoFactor;
       this.tasaCuota = tasaCuota;
       this.importe = importe;
       this.tipo = tipo;
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
    @JoinColumn(name="id_concepto", nullable=false)
    public ConceptoFactura getConceptoFactura() {
        return this.conceptoFactura;
    }
    
    public void setConceptoFactura(ConceptoFactura conceptoFactura) {
        this.conceptoFactura = conceptoFactura;
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

    
    @Column(name="tipo_factor")
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

    
    @Column(name="tipo")
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }




}


