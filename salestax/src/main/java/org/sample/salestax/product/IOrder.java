package org.sample.salestax.product;

import java.math.BigDecimal;

public interface IOrder {
	public Long getOrderId();

	public String getOrderName();

	public IProduct[] getProducts();

	public BigDecimal getTax();
	
	public BigDecimal getPrice();
}
