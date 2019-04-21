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

/* XXX: https: //stackoverflow.com /questions/8232713/how-to-display-scroll-bar-onto-a-html-table */
td {
	overflow: scroll;
	width: 10%;
	height: 20px;
}

.numerico {
	text-align: right;
}
</style>
</head>
<body>
	<!-- XXX: https://freemarker.apache.org/docs/ref_builtins_date.html -->
	Periodo: ${periodo?string["yyyy-MM-dd, HH:mm"]}

	<form action="/modificaDepreciacion" method="post">
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
				</tr>
			</thead>
			<tbody>
				<#list facturas> <#items as factura>
				<tr>
					<td>${factura.id} <input type="hidden"
						name="facturas[${factura?index}].id" value="${factura.id}" />
					</td>
					<td>${factura.rfcEmisor}</td>
					<td>${factura.razonSocialEmisor}</td>
					<td>${factura.descripcion}</td>
					<td>${factura.folio}</td>
					<!-- XXX: https://stackoverflow.com/questions/30130140/freemarker-is-there-a-way-to-format-an-integer-as-a-floating-point-number -->
					<td class="numerico"><input class="numerico" type="text"
						name="facturas[${factura?index}].monto" size="20"
						value="${factura.monto?string(",##0.0;; decimalSeparator='.' groupingSeparator=' '")}" /></td>
					<td class="numerico"><input class="numerico" type="text"
						name="facturas[${factura?index}].porcentaje" size="20"
						value="${(factura.porcentaje?string(",##0.0;; decimalSeparator='.' groupingSeparator=' '"))!''}" /></td>
					<td><input type="text"
						name="facturas[${factura?index}].fechaInicioDepreciacion"
						value="${(factura.fechaInicioDepreciacion?string["yyyy-MM-dd"])!''}" />
					</td>
					<td>${factura.periodo?string["yyyy-MM-dd"]}</td>
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
				</tr>
			</tfoot>
		</table>

		<input type="submit" value="Submitear" />
		<input type="hidden" value="${periodo?string["yyyy-MM-dd"]}" name="periodo"/>
	</form>

</body>
</html>