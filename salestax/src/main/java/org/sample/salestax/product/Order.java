package org.sample.salestax.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sample.salestax.calculator.SalestaxCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Order implements IOrder {

	private Logger logger = LoggerFactory.getLogger(Order.class);
	private Long orderId;
	private String orderName;
	private List<Product> products;
	private BigDecimal taxOnOrder;
	private BigDecimal totalPrice;

	@Override
	public IProduct[] getProducts() {
		return this.products.toArray(new IProduct[] {});
	}

	public void setProducts(Product[] products) {
		if (products == null) {
			throw new IllegalArgumentException("Products array cannot be null");
		}
		this.products = Arrays.asList(products);
	}

	public synchronized void addProduct(Product product) {
		if (product == null) {
			throw new IllegalArgumentException(
					"Product to be added cannot be null");
		}
		if (products == null) {
			products = new ArrayList<Product>();
		}
		products.add(product);
	}

	public void processOrder() {
		BigDecimal tax;
		BigDecimal priceAfterTax;
		BigDecimal totalTax = BigDecimal.ZERO;
		BigDecimal totalPrice = BigDecimal.ZERO;
		synchronized (products) {
			if (products == null) {
				return;
			}
			for (Product product : products) {
				tax = calculateTax(product);
				priceAfterTax = tax.add(product.getPrice());
				product.setPriceAfterTax(priceAfterTax);
				totalTax = totalTax.add(tax);
				totalPrice = totalPrice.add(priceAfterTax);
			}
			this.taxOnOrder = totalTax;
			this.totalPrice = totalPrice;
			logger.info("Total Tax on Order {} is {}", this, totalTax);
			logger.info("Total Cost of the Order {} is {}", this, totalPrice);
		}
	}

	private BigDecimal calculateTax(Product product) {
		return SalestaxCalculator.getTax(product);
		/*
		 * BigDecimal tax, salesTax, importTax;
		 * logger.debug("Calculating Sales Tax on Product : {}", product);
		 * salesTax = SalestaxCalculator.getSalesTax(product);
		 * logger.debug("Calculating Import Sales Tax on Product : {}",
		 * product); importTax = SalestaxCalculator.getImportSalesTax(product);
		 * tax = salesTax.add(importTax);
		 * logger.info("Total tax on Product {} is {}", product, tax); return
		 * tax;
		 */
	}

	@Override
	public BigDecimal getTax() {
		return this.taxOnOrder;
	}

	@Override
	public Long getOrderId() {
		return this.orderId;
	}

	@Override
	public String getOrderName() {
		return this.orderName;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public BigDecimal getPrice() {
		return this.totalPrice;
	}

	public String toString() {
		return "Order:" + orderName + "(" + orderId + ")";
	}

}
