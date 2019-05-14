# XXX: https://stackoverflow.com/questions/7216358/date-command-on-os-x-doesnt-have-iso-8601-i-option
respa=$1
psql -p 5434 -U algo -h localhost impuestos < $respa
