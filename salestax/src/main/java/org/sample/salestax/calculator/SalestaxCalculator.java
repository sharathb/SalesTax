package org.sample.salestax.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.sample.salestax.product.IProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalestaxCalculator {

	public static final double SalesTax = 10;
	public static final double ImportDuty = 5;
	private static final BigDecimal twenty = new BigDecimal("20");

	private static Logger logger = LoggerFactory
			.getLogger(SalestaxCalculator.class);

	private SalestaxCalculator() {
	}

	public static BigDecimal getTax(IProduct product) {
		BigDecimal tax, salesTax, importTax;
		logger.info("Calculating tax for product {} is :", product);
		salesTax = getSalesTax(product);
		importTax = getImportSalesTax(product);
		tax = salesTax.add(importTax);
		logger.debug("Tax before rounding off: {}", tax);
		tax = roundOf(tax);
		logger.debug("Tax after rounding off: {}", tax);
		return tax;
	}

	public static BigDecimal getSalesTax(IProduct product) {
		BigDecimal tax;
		BigDecimal price;
		if (isExempted(product)) {
			logger.info("Product {} is exempted from salesTax.", product);
			return BigDecimal.ZERO;
		}
		tax = new BigDecimal(SalesTax);
		price = product.getPrice();
		logger.debug("Price to which sales tax has to be calculated. {}", price);
		tax = tax.multiply(price).divide(new BigDecimal(100));
		/*
		 * logger.debug("Sales Tax before rounding off: {}", tax); tax =
		 * roundOf(tax);
		 */
		logger.info("Sales Tax on product {} is {}", product, tax);
		return tax;
	}

	public static BigDecimal getImportSalesTax(IProduct product) {
		BigDecimal tax;
		BigDecimal price;
		if (!product.isImported()) {
			logger.info("Product {} is not imported, so no tax will be levied",
					product);
			return BigDecimal.ZERO;
		}
		tax = new BigDecimal(ImportDuty);
		price = product.getPrice();
		logger.debug("Price to which import tax has to be calculated. {}",
				price);
		tax = tax.multiply(price).divide(new BigDecimal(100));
		/*
		 * logger.debug("Import Tax before rounding off: {}", tax); tax =
		 * roundOf(tax);
		 */
		logger.info("Import Tax on product {} is {}", product, tax);
		return tax;
	}

	private static BigDecimal roundOf(BigDecimal tax) {
		BigDecimal value;
		BigInteger valueInt;
		value = twenty.multiply(tax);
		value = value.round(new MathContext(2));
		valueInt = value.toBigInteger();
		value = new BigDecimal(valueInt).divide(twenty);
		return value;
	}

	private static boolean isExempted(IProduct product) {
		SalesTaxExemptions exemption;
		exemption = SalesTaxExemptions.getExemption(product.getProductType());
		if (exemption != null) {
			logger.info("Reason for exemption of product {} is {}", product,
					exemption.getReason());
			return true;
		} else {
			return false;
		}
	}

}
