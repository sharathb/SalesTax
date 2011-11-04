package org.sample.salestax;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.sample.salestax.invoice.InvoiceGenerator;
import org.sample.salestax.product.IOrder;
import org.sample.salestax.product.Order;
import org.sample.salestax.product.Product;
import org.sample.salestax.product.ProductClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderProcessor {

	private Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

	public static void main(String[] args) {
		OrderProcessor processor;
		Order[] orders;
		List<IOrder> processedOrderList;
		processor = new OrderProcessor();
		if (args.length == 0) {
			processor.logger.warn("No Orders to be processed");
			System.exit(1);
		}
		orders = processor.createOrdersFromFiles(args);
		processedOrderList = processor.processOrders(orders);
		processor.generateInvoice(processedOrderList);

	}

	public void generateInvoice(List<IOrder> processedOrderList) {
		InvoiceGenerator generator;
		logger.info("Generating the Invoice for the orders");
		generator = new InvoiceGenerator(processedOrderList);
		generator.generateInvoice();
		logger.debug("Invoice generation complete");
	}

	public List<IOrder> processOrders(Order[] orders) {
		List<IOrder> processedOrders;
		processedOrders = new ArrayList<IOrder>();
		for (Order order : orders) {
			if (order == null) {
				continue;
			}
			logger.info("Processing Order {}", order);
			order.processOrder();
			processedOrders.add(order);
		}
		logger.debug("Count of Orders processed : {}", processedOrders.size());
		return processedOrders;
	}

	public Order[] createOrdersFromFiles(String[] args) {
		Path orderPath;
		Order[] orders = new Order[args.length];
		for (int index = 1; index < args.length; index++) {
			orderPath = FileSystems.getDefault().getPath(args[0], args[index]);
			try {
				orders[index] = preareOrderFromFile(orderPath);
			} catch (IOException ex) {
				logger.error("Unable to process order in file : " + args[index]);
			}
		}
		logger.info("Count of orders obtained from the files : {}",
				orders.length);
		return orders;
	}

	private Order preareOrderFromFile(Path orderPath) throws IOException {
		Order order;
		Boolean isFirst = true;
		String line;
		BufferedReader reader = Files.newBufferedReader(orderPath,
				StandardCharsets.UTF_8);
		order = new Order();
		while ((line = reader.readLine()) != null) {
			if (isFirst) {
				createOrder(order, line);
				isFirst = false;
			} else {
				addProduct(order, line);
			}
		}
		return order;
	}

	private void addProduct(Order order, String line) {
		String[] tokens;
		Product product = new Product();
		tokens = line.split(",");
		if (tokens.length != 5) {
			throw new RuntimeException(
					"Invalid contents for product , should be in the format 'Name,type,imported,price,desc'");
		}
		product.setProductName(tokens[0]);
		product.setProductType(ProductClassification.valueOf(tokens[1]));
		product.setImported(Boolean.valueOf(tokens[2]));
		product.setPrice(new BigDecimal(tokens[3]));
		if (tokens.length > 4) {
			product.setProductDescription(tokens[4]);
		}
		order.addProduct(product);

	}

	private void createOrder(Order order, String line) {
		String[] tokens;
		tokens = line.split(",");
		if (tokens.length != 2) {
			throw new RuntimeException(
					"Invalid contents for order , should be in the format 'Ordername,OrderId'");
		}
		order.setOrderId(Long.valueOf(tokens[1]));
		order.setOrderName(tokens[0]);
	}
}
