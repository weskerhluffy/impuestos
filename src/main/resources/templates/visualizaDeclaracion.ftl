<!DOCTYPE html>
<html>
<head>
<title>Declaracion: ${declaracionVigente.periodo?string["yyyy-MM-dd, HH:mm"]}</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
table, th, td {
	border: 1px solid black;
}
</style>
</head>
<body>

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
					<th>Depreciacion mensual acumulada</th>
				</tr>
			</thead>
			<tbody>
				<#list declaracionVigente.declaracionFacturas> <#items as declaracionFactura>
				<tr>
					<td>${declaracionFactura.facturaVigente.id}</td>
					<td>${declaracionFactura.facturaVigente.rfcEmisor}</td>
					<td>${declaracionFactura.facturaVigente.razonSocialEmisor}</td>
					<td>${declaracionFactura.facturaVigente.descripcion}</td>
					<td>${declaracionFactura.facturaVigente.folio}</td>
					<td style="text-align: right;">${declaracionFactura.facturaVigente.monto?string(",##0.00")}</td>
					<td style="text-align: right;">${declaracionFactura.facturaVigente.porcentaje}</td>
					<td style="text-align: right;">${declaracionFactura.facturaVigente.fechaInicioDepreciacion?string["yyyy-MM-dd"]}</td>
					<td style="text-align: right;">${declaracionFactura.facturaVigente.periodo?string["yyyy-MM-dd"]}</td>
					<td style="text-align: right;">${declaracionFactura.facturaVigente.montoDepreciacionMensual?string(",##0.00")}</td>
					<td style="text-align: right;">

												${montoDepreciacionMensualAcumuladaPorFacturaId[declaracionFactura.factura.id?string]?string(",##0.00")}


					</td>
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
		
</body>
</html>