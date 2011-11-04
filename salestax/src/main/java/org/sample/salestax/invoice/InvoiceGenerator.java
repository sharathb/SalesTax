package org.sample.salestax.invoice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

import org.sample.salestax.product.IOrder;
import org.sample.salestax.product.IProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceGenerator {

	private Logger logger = LoggerFactory.getLogger(InvoiceGenerator.class);
	private List<IOrder> orders;

	public InvoiceGenerator(List<IOrder> orders) {
		this.orders = orders;
	}

	public void generateInvoice() {
		for (IOrder order : orders) {
			try {
				logger.info("Creating invoice for the order {}", order);
				createInvoice(order);
			} catch (IOException ex) {
				logger.error("IOException raised while processing {}", order,
						ex);
			}
		}
	}

	private void createInvoice(IOrder order) throws IOException {
		BufferedWriter writer;
		writer = getInvoiceFileWriter(order);
		writer.write(getInvoiceContent(order));
		writer.close();
	}

	private String getInvoiceContent(IOrder order) {
		StringBuilder buffer;
		buffer = new StringBuilder();
		buffer.append("Invoice for Order:");
		buffer.append(order.getOrderName());
		buffer.append("(");
		buffer.append(order.getOrderId());
		buffer.append(")");
		buffer.append("\n");
		buffer.append("ProductName|\tProductType|\tImported|\tShelfPrice|\tNetPrice\n");
		buffer.append("----------------------------------------------------------------\n");
		for (IProduct product : order.getProducts()) {
			buffer.append(product.getProductName());
			buffer.append("\t\t|\t");
			buffer.append(product.getProductType());
			buffer.append("\t\t|\t");
			buffer.append(product.isImported());
			buffer.append("\t\t|\t");
			buffer.append(product.getPrice());
			buffer.append("\t\t|\t");
			buffer.append(product.getPriceAfterTax());
			buffer.append("|\n");
		}
		buffer.append("----------------------------------------------------------------\n");
		buffer.append("Total Tax : " + order.getTax());
		buffer.append("\tOrder Price : " + order.getPrice());
		return buffer.toString();
	}

	private BufferedWriter getInvoiceFileWriter(IOrder order)
			throws IOException {
		StringBuilder fileName;
		fileName = new StringBuilder();
		fileName.append("INVOICE_");
		fileName.append(order.getOrderName() == null ? "" : order
				.getOrderName());
		fileName.append("(");
		fileName.append(order.getOrderId() == null ? "" : order.getOrderId());
		fileName.append(")");
		fileName.append(Calendar.getInstance().get(Calendar.YEAR));
		fileName.append(Calendar.getInstance().get(Calendar.MONTH));
		fileName.append(Calendar.getInstance().get(Calendar.DATE));
		fileName.append(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		fileName.append(Calendar.getInstance().get(Calendar.MINUTE));
		fileName.append(Calendar.getInstance().get(Calendar.SECOND));
		fileName.append(Calendar.getInstance().get(Calendar.MILLISECOND));

		Path folderPath = FileSystems.getDefault().getPath("Invoice");
		if (!Files.exists(folderPath)) {
			logger.info("Invoice Directory does not exist hence creating one");
			Files.createDirectory(folderPath);
		}

		Path invoiceFilePath = FileSystems.getDefault().getPath("Invoice",
				fileName.toString());
		if (!Files.exists(invoiceFilePath)) {
			Files.createFile(invoiceFilePath);
		}
		BufferedWriter writer = Files.newBufferedWriter(invoiceFilePath,
				StandardCharsets.UTF_8);
		return writer;
	}
}
