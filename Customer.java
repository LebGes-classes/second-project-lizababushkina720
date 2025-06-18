import java.util.HashMap;
import java.util.Map;

public class Customer extends Human implements Printable {
    private final Map<Product, Integer> order; // товар-количество
    private final int orderNumber;

    Customer(int age, String name, boolean sex, int orderNumber) {
        super(age, name, sex);
        this.order = new HashMap<>();
        this.orderNumber = orderNumber;

    }

    //геттеры
    public Map<Product, Integer> getOrder() {
        return new HashMap<>(order);
    }
    public int getOrderNumber() {
        return orderNumber;
    }
    // + покупка
    public void addOrder(Product product, int quantity) {
        order.merge(product, quantity, Integer::sum);
    }

    //был ли товар куплен
    public boolean hasPurchased(Product product, int quantity) {
        return order.containsKey(product) && order.get(product) >= quantity;
    }

    // Удаление покупки при возврате
    public void removePurchase(Product product, int quantity) {
        if (order.containsKey(product)) {
            order.put(product, order.get(product) - quantity);
            if (order.get(product) == 0) {
                order.remove(product);
            }
        }
    }
    // стоимость заказа
    public Map<String, Integer> calculatePrice() {
        Map<String, Integer> totalPrice = new HashMap<>();
        for (Map.Entry<Product, Integer> entry : order.entrySet()) {
            Product orderedProduct = entry.getKey();
            int orderedQuantity = entry.getValue();
            String price = orderedProduct.getPrice();
            if (price != null) {
                totalPrice.merge(price, orderedQuantity, Integer::sum);
            }
        }

        return totalPrice;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println(
                "Номер заказа: " + orderNumber + "\n"
        );
        System.out.println("Товар                | Количество ");
        for (Map.Entry<Product, Integer> entry : order.entrySet()) {
            String productName = entry.getKey().getName();
            int quantity = entry.getValue();
            System.out.printf(
                    "%-20s | %-10d \n",
                    productName,
                    quantity
            );
        }
    }
}
