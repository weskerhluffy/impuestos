DROP TABLE IF EXISTS factura cascade;
CREATE TABLE factura(
 id serial PRIMARY KEY,
 rfc_emisor VARCHAR (50) NOT NULL,
 razon_social_emisor text NOT NULL,
 descripcion text NOT NULL,
 folio VARCHAR (100) NOT NULL,
 periodo date not null,
 fecha_creacion timestamp not null default CURRENT_DATE,
 fecha_ultima_modificacion timestamp not null default CURRENT_DATE,
 unique (rfc_emisor, folio)
);

drop table if exists monto_factura cascade;
CREATE TABLE monto_factura(
 id serial PRIMARY KEY,
 monto float not null,
 tiempo_creacion timestamp not null default CURRENT_DATE,
 id_factura INTEGER REFERENCES factura(id) 
);
CREATE OR replace VIEW monto_factura_vigente 
AS 
  SELECT a.* 
  FROM   (SELECT m.*, 
                 Max(m.tiempo_creacion) 
                   over ( 
                     PARTITION BY m.id_factura) AS ultimo_creado 
          FROM   monto_factura m) AS a 
  WHERE  a.tiempo_creacion = a.ultimo_creado; 
	
drop table if exists monto_deducible_factura cascade;
CREATE TABLE monto_deducible_factura(
 id serial PRIMARY KEY,
 monto float not null,
 tiempo_creacion timestamp not null default CURRENT_DATE,
 id_factura INTEGER REFERENCES factura(id)
);
CREATE OR replace VIEW monto_deducible_factura_vigente 
AS 
  SELECT a.* 
  FROM   (SELECT m.*, 
                 Max(m.tiempo_creacion) 
                   over ( 
                     PARTITION BY m.id_factura) AS ultimo_creado 
          FROM   monto_deducible_factura m) AS a 
  WHERE  a.tiempo_creacion = a.ultimo_creado; 

drop table if exists porcentaje_depreciacion_anual_factura cascade;
CREATE TABLE porcentaje_depreciacion_anual_factura(
 id serial PRIMARY KEY,
 porcentaje float not null check(porcentaje<100),
 tiempo_creacion timestamp not null default CURRENT_DATE,
 id_factura INTEGER REFERENCES factura(id)
);
CREATE OR replace VIEW porcentaje_depreciacion_anual_factura_vigente 
AS 
  SELECT a.* 
  FROM   (SELECT m.*, 
                 Max(m.tiempo_creacion) 
                   over ( 
                     PARTITION BY m.id_factura) AS ultimo_creado 
          FROM   porcentaje_depreciacion_anual_factura m) AS a
  WHERE  a.tiempo_creacion = a.ultimo_creado; 

drop table if exists fecha_inicio_depreciacion_factura cascade;
CREATE TABLE fecha_inicio_depreciacion_factura(
 id serial PRIMARY KEY,
 fecha date not null,
 tiempo_creacion timestamp not null default CURRENT_DATE,
 id_factura INTEGER REFERENCES factura(id)
);
CREATE OR replace VIEW fecha_inicio_depreciacion_factura_vigente 
AS 
  SELECT a.* 
  FROM   (SELECT m.*, 
                 Max(m.tiempo_creacion) 
                   over ( 
                     PARTITION BY m.id_factura) AS ultimo_creado 
          FROM   fecha_inicio_depreciacion_factura m) AS a
  WHERE  a.tiempo_creacion = a.ultimo_creado; 

 
drop view if exists factura_vigente;  
CREATE OR REPLACE view factura_vigente 
AS 
  SELECT f.id, 
         f.rfc_emisor, 
         f.folio, 
         Coalesce(md.monto, m.monto)                                      AS 
         monto , 
         p.porcentaje, 
         fd.fecha as fecha_inicio_depreciacion, 
         Extract (year FROM f.periodo)                                     AS ano 
         , 
         Date_part('month', f.periodo)
         AS mes,
         f.periodo,
         f.razon_social_emisor,
         f.descripcion,
         m.id id_monto,
         md.id id_monto_deducible,
         p.id id_porcentaje,
         fd.id id_fecha_inicio_depreciacion
  FROM   factura f 
         INNER JOIN monto_factura_vigente m 
                 ON f.id = m.id_factura 
         LEFT JOIN monto_deducible_factura_vigente md 
                ON f.id = md.id_factura 
         LEFT JOIN porcentaje_depreciacion_anual_factura_vigente p 
                ON f.id = p.id_factura 
         LEFT JOIN fecha_inicio_depreciacion_factura_vigente fd 
                ON f.id = fd.id_factura; 
                   
                
CREATE FUNCTION mesdif(fecha1 date, fecha2 date) RETURNS integer AS $$
 BEGIN
 RETURN (DATE_PART('year',  fecha1) - DATE_PART('year',  fecha2)) * 12 +
        (DATE_PART('month', fecha1) - DATE_PART('month', fecha2));
 END; $$
 LANGUAGE PLPGSQL;  
 
-- drop function mismo_periodo;
 create function mismo_periodo(fecha1 date, fecha2 date) RETURNS bool AS $$
 BEGIN
 RETURN (DATE_PART('year',  fecha1) = DATE_PART('year',  fecha2)) and
        (DATE_PART('month', fecha1) = DATE_PART('month', fecha2));
 END; $$
 LANGUAGE PLPGSQL;  
                   
SELECT f.*, 
         ( ( ( f.monto * ( Coalesce(f.porcentaje, 0) / 100 ) ) / 
             12 
           ) + 
           0.005 ) * Mesdif(Date('2020-01-01'), Coalesce(f.fecha, Date('2020-01-01'))) AS 
         monto_depreciado 
  FROM   factura_vigente f 
                  where Mesdif(Date('2020-01-01'), Coalesce(f.fecha, Date('2020-01-01'))) > 0;
                 

-- TODO: Trigger para guardar actualizaciones pendientes de declaraciones cuando haya nuevos vigentes.

drop table if exists declaracion cascade;
CREATE TABLE declaracion(
 id serial PRIMARY KEY,
 periodo date not null,
 tiempo_creacion timestamp default CURRENT_DATE
);

drop table if exists declaracion_factura cascade;
CREATE TABLE declaracion_factura (
 id serial PRIMARY KEY,
 id_factura INTEGER not null REFERENCES factura(id),
 id_monto INTEGER not null REFERENCES monto_factura(id),
 id_monto_deducible INTEGER REFERENCES monto_deducible_factura(id),
 id_porcentaje_depreciacion INTEGER REFERENCES porcentaje_depreciacion_anual_factura(id),
 id_fecha_inicio_depreciacion INTEGER REFERENCES fecha_inicio_depreciacion_factura(id),
 id_declaracion INTEGER not null REFERENCES declaracion(id),
 tiempo_creacion timestamp not null default CURRENT_DATE
);

CREATE OR replace VIEW declaracion_vigente
AS 
  SELECT a.* 
  FROM   (SELECT m.*, 
                 Max(m.tiempo_creacion) 
                   over ( 
                     PARTITION BY m.periodo) AS ultimo_creado 
          FROM declaracion m) AS a
  WHERE  a.tiempo_creacion = a.ultimo_creado; 


-- select date('2019-02-01')-INTERVAL '1 month'

-- XXX: http://www.postgresqltutorial.com/postgresql-create-function/
-- XXX: http://www.sqlines.com/postgresql/how-to/datediff
SELECT f.* 
FROM   factura_vigente f 
WHERE  ( Mesdif(Date('2019-01-01'), Coalesce(f.fecha_inicio_depreciacion, Date('2019-01-01'))) > 0 
          OR Mismo_periodo(f.fecha_inicio_depreciacion, date('2019-01-01')) ) 
       AND ( ( ( f.monto * ( Coalesce(f.porcentaje, 0) / 100 ) ) / 12 ) + 0.005 
           ) * Mesdif( 
               Date('2019-01-01'), Coalesce(f.fecha_inicio_depreciacion, 
           Date('2019-01-01'))) < f.monto 
ORDER  BY f.periodo 
 

SELECT f.* FROM   factura_vigente f WHERE  ( Mesdif(:periodo, Coalesce(f.fecha_inicio_depreciacion, :periodo)) > 0 OR Mismo_periodo(f.fecha_inicio_depreciacion, date('2019-01-01')) ) AND ( ( ( f.monto * ( Coalesce(f.porcentaje, 0) / 100 ) ) / 12 ) + 0.005 ) * Mesdif( :periodo, Coalesce(f.fecha_inicio_depreciacion, :periodo)) < f.monto ORDER  BY f.periodo 

update factura set descripcion='IVA acreditable', folio='83' where id=121;