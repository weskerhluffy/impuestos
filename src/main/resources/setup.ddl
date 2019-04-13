DROP TABLE IF EXISTS factura cascade;
CREATE TABLE factura(
 id serial PRIMARY KEY,
 rfc_emisor VARCHAR (50) NOT NULL,
 folio VARCHAR (100) NOT NULL,
 fecha_creacion timestamp not null default CURRENT_DATE,
 fecha_ultima_modificacion timestamp not null default CURRENT_DATE
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

CREATE FUNCTION mesdif(fecha1 date, fecha2 date) RETURNS integer AS $$
 BEGIN
 RETURN (DATE_PART('year',  fecha1) - DATE_PART('year',  fecha2)) * 12 +
        (DATE_PART('month', fecha1) - DATE_PART('month', fecha2));
 END; $$
 LANGUAGE PLPGSQL;  
 
drop view if exists factura_vigente;  
CREATE OR REPLACE view factura_vigente 
AS 
  SELECT f.id, 
         f.rfc_emisor, 
         f.folio, 
         Coalesce(md.monto, m.monto)                                      AS 
         monto 
            , 
         p.porcentaje, 
         fd.fecha, 
         Extract (year FROM fd.fecha)                                     AS ano 
         , 
         Date_part('month', fd.fecha) 
         AS mes, 
         ( ( ( Coalesce(md.monto, 0) * ( Coalesce(p.porcentaje, 0) / 100 ) ) / 
             12 
           ) + 
           0.005 ) * Mesdif(Date(Now()), Coalesce(fd.fecha, Date(Now()))) AS 
         monto_depreciado 
  FROM   factura f 
         INNER JOIN monto_factura_vigente m 
                 ON f.id = m.id_factura 
         LEFT JOIN monto_deducible_factura_vigente md 
                ON f.id = md.id_factura 
         LEFT JOIN porcentaje_depreciacion_anual_factura_vigente p 
                ON f.id = p.id_factura 
         LEFT JOIN fecha_inicio_depreciacion_factura_vigente fd 
                ON f.id = fd.id_factura 
                   AND Mesdif(Date(Now()), Coalesce(fd.fecha, Date(Now()))) > 0; 
                 
-- TODO: Trigger para guardar actualizaciones pendientes de declaraciones cuando haya nuevos vigentes.

drop table if exists declaracion cascade;
CREATE TABLE declaracion(
 id serial PRIMARY KEY,
 monto_egresos float not null,
 monto_ingresos float not null,
 periodo date not null,
 tiempo_creacion timestamp not null default CURRENT_DATE
);

drop table if exists factura_declarada cascade;
CREATE TABLE factura_declarada(
 id serial PRIMARY KEY,
 id_factura INTEGER not null REFERENCES factura(id),
 id_monto INTEGER not null REFERENCES monto_factura(id),
 id_monto_deducible INTEGER not null REFERENCES monto_deducible_factura(id),
 id_porcentaje_depreciacion INTEGER not null REFERENCES porcentaje_depreciacion_anual_factura(id),
 id_fecha_inicio_depreciacion INTEGER not null REFERENCES fecha_inicio_depreciacion_factura(id),
 id_declaracion INTEGER not null REFERENCES declaracion(id),
 tiempo_creacion timestamp not null default CURRENT_DATE
);


-- select date('2019-02-01')-INTERVAL '1 month'

-- XXX: http://www.postgresqltutorial.com/postgresql-create-function/
-- XXX: http://www.sqlines.com/postgresql/how-to/datediff
 