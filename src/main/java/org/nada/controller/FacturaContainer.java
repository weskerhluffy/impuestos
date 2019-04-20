package org.nada.controller;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.nada.models.FacturaVigente;

// XXX: https://stackoverflow.com/questions/28770050/binding-form-freemarker-spring-mvc
// XXX: https://netjs.blogspot.com/2018/09/spring-mvc-binding-list-of-objects-example.html
// XXX: https://viralpatel.net/blogs/spring-mvc-multi-row-submit-java-list/
// XXX: https://stackoverflow.com/questions/21952041/how-to-pass-multiple-values-from-the-form-to-a-controller-using-spring-mvc
public class FacturaContainer {
	private List<FacturaVigente> facturas;

	public List<FacturaVigente> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<FacturaVigente> facturas) {
		this.facturas = facturas;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
