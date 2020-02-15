<!DOCTYPE html>
<html>
<head>
<title>Gastos ${periodo?string["yyyy-MM-dd, HH:mm"]}</title>
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

	<form action="/registraDeclaracion" method="post">
		<table>
			<thead>
				<tr>
					<th>Id</th>
					<th>RFC emisor</th>
					<th>Razon sensual emisor</th>
					<th>Descripcion</th>
					<th>Folio</th>
					<th>Monto deducible</th>
					<th>IVA</th>
				</tr>
			</thead>
			<tbody>
				<#assign ivaTotal=0> 
				<#list facturasNoDepreciadas>
				<#items as factura>
				<tr>
					<td>${factura.id}</td>
					<td>${factura.rfcEmisor}</td>
					<td>${factura.razonSocialEmisor}</td>
					<td>${factura.descripcion}</td>
					<td>${factura.folio}</td>
					<!-- TODO: Añadir monto original -->
					<!-- XXX: https://stackoverflow.com/questions/20960069/how-to-customize-number-format-in-freemarker -->
					<td style="text-align: right;">
						${factura.monto?string(",##0.00")} <input type="hidden"
						name="declaracionFacturasNoDepreciadas[${factura?index}].montoFactura"
						value="${factura.idMonto}" /> <input type="hidden"
						name="declaracionFacturasNoDepreciadas[${factura?index}].factura"
						value="${factura.id}" /> <input type="hidden"
						name="declaracionFacturasNoDepreciadas[${factura?index}].montoDeducibleFactura"
						value="${factura.idMontoDeducible}" />
						
					<td>
					
				<#assign ivaActual=0> 
<#if factura.factura.conceptoFacturas?has_content >
				<#assign ivaActual=factura.monto*0.16> 
							
<#else>
</#if>
					${ivaActual} <#assign
							ivaTotal+=ivaActual>
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
					<td style="text-align: right;">${sumaNoDepreciadas}</td>
					<td style="text-align: right;">${ivaTotal}</td>
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
					<th>Depreciacion mensual acumulada</th>
					<th>IVA</th>
				</tr>
			</thead>
			<tbody>
				<#assign ivaTotalDepreciacion=0> <#list
					facturasDepreciadas> <#items as factura>
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
					<td style="text-align: right;">${factura.montoDepreciacionMensual?string(",##0.00")}</td>
					<!-- XXX: https://stackoverflow.com/questions/14821329/freemarker-and-hashmap-how-do-i-get-key-value -->
					<td style="text-align: right;">

						${montoDepreciacionMensualAcumuladaPorFacturaId[factura.id?string]?string(",##0.00")}
						<input type="hidden"
						name="declaracionFacturasDepreciadas[${factura?index}].montoFactura"
						value="${factura.idMonto}" /> <input type="hidden"
						name="declaracionFacturasDepreciadas[${factura?index}].factura"
						value="${factura.id}" /> <input type="hidden"
						name="declaracionFacturasDepreciadas[${factura?index}].montoDeducibleFactura"
						value="${(factura.idMontoDeducible?string)!''}" /> <input
						type="hidden"
						name="declaracionFacturasDepreciadas[${factura?index}].fechaInicioDepreciacionFactura"
						value="${factura.idFechaInicioDepreciacion}" /> <input
						type="hidden"
						name="declaracionFacturasDepreciadas[${factura?index}].porcentajeDepreciacionAnualFactura"
						value="${factura.idPorcentaje}" />

					</td>

					<#assign
						ivaDepreciacion=montoDepreciacionMensualAcumuladaPorFacturaId[factura.id?string]*0.16>
					<#assign ivaTotalDepreciacion+=ivaDepreciacion>
					<td>${ivaDepreciacion}</td>
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
					<td></td>
					<td style="text-align: right;">${sumaDepreciadas}</td>
					<td style="text-align: right;">${ivaTotalDepreciacion}</td>
				</tr>
			</tfoot>
		</table>
		<input type="submit" name="Registrar" />
	</form>
	<p>Grand total: ${sumaNoDepreciadas+sumaDepreciadas}</p>
	<p>Grand IVA total: ${ivaTotal+ivaTotalDepreciacion}</p>

</body>
</html>