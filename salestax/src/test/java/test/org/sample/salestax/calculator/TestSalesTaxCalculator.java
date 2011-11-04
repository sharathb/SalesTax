package test.org.sample.salestax.calculator;

import java.math.BigDecimal;

import org.junit.Test;
import org.sample.salestax.calculator.SalestaxCalculator;
import org.sample.salestax.product.Product;
import org.sample.salestax.product.ProductClassification;

public class TestSalesTaxCalculator {

	@Test
	public void testSalesTax() {
		Product product;
		product = new Product();
		product.setPrice(new BigDecimal("12.49"));
		System.out.println("Sales Tax : "
				+ SalestaxCalculator.getSalesTax(product));
	}

	@Test
	public void testSalesTaxWithExemption() {
		Product product;
		product = new Product();
		product.setProductType(ProductClassification.Books);
		product.setPrice(new BigDecimal("12.49"));
		System.out.println("Sales Tax With Exemption: "
				+ SalestaxCalculator.getSalesTax(product));
	}

	@Test
	public void testImportSalesTax() {
		Product product;
		product = new Product();
		product.setProductType(ProductClassification.Books);
		product.setPrice(new BigDecimal("12.49"));
		System.out.println("Import Tax : "
				+ SalestaxCalculator.getImportSalesTax(product));
	}

	@Test
	public void testImportSalesTaxWithImportedProduct() {
		Product product;
		product = new Product();
		product.setProductType(ProductClassification.Books);
		product.setPrice(new BigDecimal("12.49"));
		product.setImported(true);
		System.out.println("Import Tax On Impoted Product: "
				+ SalestaxCalculator.getImportSalesTax(product));
	}

}
