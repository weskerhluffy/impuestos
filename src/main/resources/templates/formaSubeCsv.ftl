<html xmlns:th="http://www.thymeleaf.org">
<body>

<!-- XXX: https://stackoverflow.com/questions/306732/how-to-check-if-a-variable-exists-in-a-freemarker-template -->
<#if message??>
   Hi ${message}, How are you?
</#if>

	<div>
		<form method="POST" enctype="multipart/form-data" action="/subeCsv">
			<table>
				<tr><td>File to upload:</td><td><input type="file" name="file" /></td></tr>
				<tr><td></td><td><input type="submit" value="Upload" /></td></tr>
			</table>
		</form>
	</div>


</body>
</html>