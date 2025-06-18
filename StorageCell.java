import java.util.HashMap;
import java.util.Map;

public class StorageCell implements Printable{
    private static int nextId = 1; // Счетчик для Id
    private int id;
    private int capacity;
    private Map<Product, Integer> stock;// товар- количество

    public StorageCell(int capacity) {
        this.id = nextId++;
        this.capacity = capacity;
        this.stock = new HashMap<>();
    }
    // геттеры
    public int getId() {
        return id;
    }
    public Map<Product, Integer> getStock() {
        return stock;
    }
    public void setId(int id) { this.id = id; }
    public int getCapacity(){ return capacity; }

    // общее кол-во товаров
    private int getCurrentStock() {
        return stock.values().stream().mapToInt(Integer::intValue).sum();
    }

    // + товар
    public boolean addProduct(Product product, int quantity) {
        if (getCurrentStock() + quantity > capacity) {
            System.out.println("Недостаточно места в ячейке " + id);
            return false;
        }
        stock.merge(product,quantity, Integer::sum);
        return true;
    }

    // - товар
    public boolean removeProduct(Product product, int quantity) {
        if (!stock.containsKey(product) || stock.get(product) < quantity) {
            System.out.println("Недостаточно товара в ячейке " + id);
            return false;
        }
        stock.put(product, stock.get(product) - quantity);
        if (stock.get(product) == 0) {
            stock.remove(product);
        }
        return true;
    }


    @Override
    public void printInfo() {
        System.out.println("Ячейка ID: " + id + "\n"+ "Вместимость: " + capacity +"\n"+ "Занято: " + getCurrentStock());
        System.out.println("Товары в ячейке:");
        if (stock.isEmpty()) {
            System.out.println("  Пусто");
        } else {
            stock.forEach((product, quantity) ->
                    System.out.println("  " + product.getName() + " (ID: " + product.getId() + "), Количество: " + quantity));
        }
    }

    // обновление счетчика
    public static void updateNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }




}
