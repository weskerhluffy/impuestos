<!DOCTYPE html>
<html>
<head>
<title>Show message</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<!-- XXX: https://freemarker.apache.org/docs/ref_builtins_date.html -->
	Periodo: ${periodo?string["yyyy-MM-dd, HH:mm"]}

	<table>
		<thead>
			<tr>
				<th>Id</th>
				<th>RFC</th>
				<th>Folio</th>
				<th>Monto deducible</th>
			</tr>
		</thead>
		<tbody>
			<#list facturasNoDepreciadas> <#items as factura>
			<tr>
				<td>${factura.id}</td>
				<td>${factura.rfcEmisor}</td>
				<td>${factura.folio}</td>
				<!-- TODO: Añadir monto original -->
				<td>${factura.monto}</td>
			</tr>
			</#items> <#else></#list>
		</tbody>
	</table>

	<table>
		<thead>
			<tr>
				<th>Id</th>
				<th>RFC</th>
				<th>Folio</th>
				<th>Monto deducible</th>
				<th>Porcentaje de depreciacion</th>
				<th>Fecha de inicio de depreciacion</th>
				<th>Periodo inicial</th>
				<th>Depreciacion mensual</th>
			</tr>
		</thead>
		<tbody>
			<#list facturasDepreciadas> <#items as factura>
			<tr>
				<td>${factura.id}</td>
				<td>${factura.rfcEmisor}</td>
				<td>${factura.folio}</td>
				<td>${factura.monto}</td>
				<td>${factura.porcentaje}</td>
				<td>${factura.fechaInicioDepreciacion?string["yyyy-MM-dd"]}</td>
				<td>${factura.periodo?string["yyyy-MM-dd"]}</td>
				<!-- XXX: https://stackoverflow.com/questions/14821329/freemarker-and-hashmap-how-do-i-get-key-value -->
				<td>${montoDepreciacionAnualPorFacturaId[factura.id?string]}</td>
			</tr>
			</#items> <#else></#list>
		</tbody>
	</table>

</body>
</html>