for arch in $(ls $HOME/Documents/facturas_$1/*xml)
do
	# XXX: https://stackoverflow.com/questions/16090869/how-to-pretty-print-xml-from-the-command-line
	cat $arch |xmllint --format -
done
