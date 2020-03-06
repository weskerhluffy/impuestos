CREATE TABLE impuesto_monto_deducible_concepto_factura(
 id serial PRIMARY KEY,
 id_monto_deducible_concepto INTEGER not null REFERENCES monto_deducible_concepto_factura(id),
 base float not null,
 impuesto varchar (10) not null,
 tipo_factor VARCHAR (255) not null,
 tasa_cuota float not null,
 importe float not null,
 tipo VARCHAR (255) not null
);