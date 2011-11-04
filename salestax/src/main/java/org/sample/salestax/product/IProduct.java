package org.sample.salestax.product;

import java.math.BigDecimal;

public interface IProduct {

	public String getProductName();

	public ProductClassification getProductType();

	public BigDecimal getPrice();

	public boolean isImported();

	public BigDecimal getPriceAfterTax();

}
