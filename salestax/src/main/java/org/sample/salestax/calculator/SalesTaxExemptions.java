package org.sample.salestax.calculator;

import org.sample.salestax.product.ProductClassification;

public enum SalesTaxExemptions {

	Medicines(ProductClassification.Medical, "Exempted from sales tax, as per Health Policy"),
	Books(ProductClassification.Books, "Exempted from sales tax, as per Education Policy"),
	Food(ProductClassification.Food, "Exempted from sales tax, as Basic Needs Policy");

	private ProductClassification productType;
	private String reason;

	public ProductClassification getProductType() {
		return productType;
	}

	public String getReason() {
		return reason;
	}

	private SalesTaxExemptions(ProductClassification type, String reason) {
		this.productType = type;
		this.reason = reason;
	}

	public static SalesTaxExemptions getExemption(ProductClassification type) {
		for (SalesTaxExemptions exemption : SalesTaxExemptions.values()) {
			if (exemption.productType.equals(type)) {
				return exemption;
			}
		}
		return null;
	}
}
