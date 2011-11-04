package org.sample.salestax.product;

import java.math.BigDecimal;

public class Product implements IProduct {

	private String productName;
	private String productDescription;
	private ProductClassification productType;
	private BigDecimal price;
	private BigDecimal priceAfterTax;
	private boolean imported;

	@Override
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public void setProductType(ProductClassification productType) {
		this.productType = productType;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setImported(boolean imported) {
		this.imported = imported;
	}

	protected void setPriceAfterTax(BigDecimal price) {
		this.priceAfterTax = price;
	}

	@Override
	public ProductClassification getProductType() {
		return this.productType;
	}

	@Override
	public BigDecimal getPrice() {
		return this.price;
	}

	@Override
	public boolean isImported() {
		return this.imported;
	}

	@Override
	public BigDecimal getPriceAfterTax() {
		return this.priceAfterTax;
	}

	public String toString() {
		return "Product : " + productName + "(" + this.productType + ")";
	}

}
