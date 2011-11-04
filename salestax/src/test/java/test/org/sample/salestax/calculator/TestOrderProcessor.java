package test.org.sample.salestax.calculator;

import java.nio.file.FileSystems;
import java.util.List;

import org.junit.Test;
import org.sample.salestax.OrderProcessor;
import org.sample.salestax.product.IOrder;
import org.sample.salestax.product.Order;

public class TestOrderProcessor {

	@Test
	public void testOrderPrcoessor() {
		Order[] orders;
		List<IOrder> processedOrders;
		OrderProcessor processor;
		processor = new OrderProcessor();
		orders = processor.createOrdersFromFiles(new String[] {
				FileSystems.getDefault().getPath("Orders").toString(),
				"Order1.txt", "Order2.txt", "Order3.txt" });
		processedOrders = processor.processOrders(orders);
		processor.generateInvoice(processedOrders);
	}
}
