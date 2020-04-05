package org.nada.models;
// Generated Mar 9, 2020, 6:42:38 PM by Hibernate Tools 5.2.5.Final


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
 * MontoDeducibleConceptoFactura generated by hbm2java
 */
@Entity
@Table(name="monto_deducible_concepto_factura"
    ,schema="public"
)
public class MontoDeducibleConceptoFactura  implements java.io.Serializable {


     private Integer id;
     private ConceptoFactura conceptoFactura;
     private Double monto;
     private Date tiempoCreacion;
     private Set<ImpuestoMontoDeducibleConceptoFactura> impuestoMontoDeducibleConceptoFacturas = new HashSet<ImpuestoMontoDeducibleConceptoFactura>(0);

    public MontoDeducibleConceptoFactura() {
    }

	
    public MontoDeducibleConceptoFactura(Double monto, Date tiempoCreacion) {
        this.monto = monto;
        this.tiempoCreacion = tiempoCreacion;
    }
    public MontoDeducibleConceptoFactura(ConceptoFactura conceptoFactura, Double monto, Date tiempoCreacion, Set<ImpuestoMontoDeducibleConceptoFactura> impuestoMontoDeducibleConceptoFacturas) {
       this.conceptoFactura = conceptoFactura;
       this.monto = monto;
       this.tiempoCreacion = tiempoCreacion;
       this.impuestoMontoDeducibleConceptoFacturas = impuestoMontoDeducibleConceptoFacturas;
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
    @JoinColumn(name="id_concepto_factura")
    public ConceptoFactura getConceptoFactura() {
        return this.conceptoFactura;
    }
    
    public void setConceptoFactura(ConceptoFactura conceptoFactura) {
        this.conceptoFactura = conceptoFactura;
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

@OneToMany(fetch=FetchType.LAZY, mappedBy="montoDeducibleConceptoFactura")
    public Set<ImpuestoMontoDeducibleConceptoFactura> getImpuestoMontoDeducibleConceptoFacturas() {
        return this.impuestoMontoDeducibleConceptoFacturas;
    }
    
    public void setImpuestoMontoDeducibleConceptoFacturas(Set<ImpuestoMontoDeducibleConceptoFactura> impuestoMontoDeducibleConceptoFacturas) {
        this.impuestoMontoDeducibleConceptoFacturas = impuestoMontoDeducibleConceptoFacturas;
    }




}


