<!DOCTYPE html>
<html>
<head>
<title>Show message</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
table, th, td {
	border: 1px solid black;
}
</style>
</head>
<body>
	<!-- XXX: https://freemarker.apache.org/docs/ref_builtins_date.html -->
	Periodo: ${periodo?string["yyyy-MM-dd, HH:mm"]}

	<table>
		<thead>
			<tr>
				<th>Id</th>
				<th>RFC emisor</th>
				<th>Razon sensual emisor</th>
				<th>Descripcion</th>
				<th>Folio</th>
				<th>Monto deducible</th>
			</tr>
		</thead>
		<tbody>
			<#list facturasNoDepreciadas> <#items as factura>
			<tr>
				<td>${factura.id}</td>
				<td>${factura.rfcEmisor}</td>
				<td>${factura.razonSocialEmisor}</td>
				<td>${factura.descripcion}</td>
				<td>${factura.folio}</td>
				<!-- TODO: Añadir monto original -->
				<!-- XXX: https://stackoverflow.com/questions/20960069/how-to-customize-number-format-in-freemarker -->
				<td style="text-align: right;">${factura.monto?string(",##0.00")}</td>
			</tr>
			</#items> <#else></#list>
		</tbody>
		<tfoot>
			<tr>
				<td>Total:</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td style="text-align: right;">${sumaNoDepreciadas}</td>
			</tr>
		</tfoot>
	</table>

	<table>
		<thead>
			<tr>
				<th>Id</th>
				<th>RFC emisor</th>
				<th>Razon sensual emisor</th>
				<th>Descripcion</th>
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
				<td>${factura.razonSocialEmisor}</td>
				<td>${factura.descripcion}</td>
				<td>${factura.folio}</td>
				<td style="text-align: right;">${factura.monto?string(",##0.00")}</td>
				<td style="text-align: right;">${factura.porcentaje}</td>
				<td style="text-align: right;">${factura.fechaInicioDepreciacion?string["yyyy-MM-dd"]}</td>
				<td style="text-align: right;">${factura.periodo?string["yyyy-MM-dd"]}</td>
				<!-- XXX: https://stackoverflow.com/questions/14821329/freemarker-and-hashmap-how-do-i-get-key-value -->
				<td style="text-align: right;">${montoDepreciacionAnualPorFacturaId[factura.id?string]?string(",##0.00")}</td>
			</tr>
			</#items> <#else></#list>
		</tbody>
		<tfoot>
			<tr>
				<td>Total:</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td style="text-align: right;">${sumaDepreciadas}</td>
			</tr>
		</tfoot>
	</table>

	<p>Grand total: ${sumaNoDepreciadas+sumaDepreciadas}</p>

</body>
</html>