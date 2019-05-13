# XXX: https://stackoverflow.com/questions/7216358/date-command-on-os-x-doesnt-have-iso-8601-i-option
estampa_tiempo=$(date -u +"%Y_%m_%d_%H_%M_%SZ")
pg_dump -p 5434 -U algo -h localhost impuestos &> impuestos_${estampa_tiempo}.sql
