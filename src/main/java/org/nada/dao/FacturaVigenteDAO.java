package org.nada.dao;

import java.util.Date;
import java.util.List;

import org.nada.models.FacturaVigente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// XXX: https://www.mkyong.com/spring-data/spring-data-add-custom-method-to-repository/
@Repository
public interface FacturaVigenteDAO extends CrudRepository<FacturaVigente, Integer>, FacturaVigenteDAOCustom {

	// XXX:
	// https://stackoverflow.com/questions/878573/java-multiline-string?page=2&tab=votes
//	@Query(select f from FacturaVigente f)

	@Query(value = "SELECT f.*  FROM factura_vigente f where Mesdif(:periodo, Coalesce(f.periodo, :periodo)) > 0", nativeQuery = true)
//    @Query(value = `SELECT u.* FROM Users u WHERE u.status = :periodo AND u.name = :periodo`, nativeQuery = true)
	public List<FacturaVigente> findFacturasDepreciadas(@Param("periodo") Date periodo);
}
