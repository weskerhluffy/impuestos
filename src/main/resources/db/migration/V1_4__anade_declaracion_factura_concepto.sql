CREATE TABLE declaracion_concepto_factura(
 id serial PRIMARY KEY,
 tiempo_creacion timestamp not null default CURRENT_DATE,
 id_declaracion_factura INTEGER REFERENCES declaracion_factura(id),
 id_concepto_factura INTEGER REFERENCES concepto_factura(id)
);