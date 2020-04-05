-- alter table declaracion_factura alter column id_monto INTEGER REFERENCES monto_factura(id);
-- XXX: https://stackoverflow.com/questions/13643806/alter-table-set-null-in-not-null-column-postgresql-9-1/13643952
alter table declaracion_factura alter column id_monto drop not null;
