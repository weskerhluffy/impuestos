-- XXX: https://www.baeldung.com/database-migrations-with-flyway
CREATE TABLE monto_deducible_concepto_factura(
 id serial PRIMARY KEY,
 monto float not null,
 tiempo_creacion timestamp not null default CURRENT_DATE,
 id_concepto_factura INTEGER REFERENCES concepto_factura(id)
);
CREATE OR replace VIEW monto_deducible_concepto_factura_vigente 
AS 
  SELECT a.* 
  FROM   (SELECT m.*, 
                 Max(m.tiempo_creacion) 
                   over ( 
                     PARTITION BY m.id_concepto_factura) AS ultimo_creado 
          FROM   monto_deducible_concepto_factura m) AS a 
  WHERE  a.tiempo_creacion = a.ultimo_creado; 