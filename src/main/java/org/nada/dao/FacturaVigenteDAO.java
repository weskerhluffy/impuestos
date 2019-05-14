package org.nada.dao;

import java.util.Date;
import java.util.List;

import org.nada.models.FacturaVigente;
import org.nada.models.FacturaVigenteExtendida;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// XXX: https://www.mkyong.com/spring-data/spring-data-add-custom-method-to-repository/
@Repository
public interface FacturaVigenteDAO extends CrudRepository<FacturaVigente, Integer>, FacturaVigenteDAOCustom {

	// @formatter:off
	// XXX: https://stackoverflow.com/questions/878573/java-multiline-string?page=2&tab=votes
    // @formatter:on
//	@Query(select f from FacturaVigente f)

	// TODO: Filtrar facturas que ya se depreciaron del todo
	@Query(value = "SELECT f.* FROM   factura_vigente f WHERE  ( Mesdif(:periodo, Coalesce(f.fecha_inicio_depreciacion, :periodo)) > 0 OR Mismo_periodo(f.fecha_inicio_depreciacion, date('2019-01-01')) ) AND ( ( ( f.monto * ( Coalesce(f.porcentaje, 0) / 100 ) ) / 12 ) + 0.005 ) * Mesdif( :periodo, Coalesce(f.fecha_inicio_depreciacion, :periodo)) < f.monto ORDER  BY f.periodo", nativeQuery = true, name = "findFacturasDepreciadas")
//    @Query(value = `SELECT u.* FROM Users u WHERE u.status = :periodo AND u.name = :periodo`, nativeQuery = true)
	public List<FacturaVigente> findFacturasDepreciadas(@Param("periodo") Date periodo);

	public List<FacturaVigente> findByAnoAndMesOrderByPeriodo(Double ano, Double mes);
}
