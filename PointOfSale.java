import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
public class PointOfSale implements Printable{
    private static int nextId = 1; // счетчик для iD
    private int id;
    private String name;
    private  List< Employee> employees;
    private  Map<Product,Integer> products;
    private  Map<String,Integer> revenue;

    PointOfSale(String name){
        this.id = nextId++;
        this.name=name;
        this.products=new HashMap<>();
        this.employees=new ArrayList<>();
        this.revenue=new HashMap<>();

    }

    // геттеры
    public int getId(){
        return id;
    }
    public List<Employee> getEmployees() {
        return employees;
    }
    public String getName(){
        return name;
    }
    public Map<Product,Integer> getProducts(){
        return products;
    }
    public Map<String, Integer> getRevenue() {
        return revenue;
    }
    public void setId(int id) { this.id = id; }

    // + товар
    public boolean addProduct(Product product, int quantity) {
        products.merge(product, quantity, Integer::sum);
        return true;
    }

    // - товар
    public boolean removeProduct(Product product, int quantity) {
        if (!products.containsKey(product) || products.get(product) < quantity) {
            System.out.println("недостаточно товара в пункте продаж " + id);
            return false;
        }
        // удаляем инфу о товаре из пункта продаж если закончился
        products.put(product, products.get(product) - quantity);
        if (products.get(product) == 0) {
            products.remove(product);
        }
        return true;
    }

    // Продажа товара
    public boolean sellProduct(Product product, int quantity, String exchangeProduct, int exchangeQuantity, Customer customer) {
        if (removeProduct(product, quantity)) {
            revenue.merge(exchangeProduct, quantity, Integer::sum);
            customer.addOrder(product, quantity);
            return true;
        }
        return false;
    }

    // Возврат товара
    public boolean returnProduct(Product product, int quantity, String exchangeProduct, int exchangeQuantity, Customer customer) {
        if (customer.hasPurchased(product, quantity)) {
            if (!revenue.containsKey(exchangeProduct) || revenue.get(exchangeProduct) < exchangeQuantity) {
                System.out.println("Недостаточно обменника в пункте вызова для возврата");
                return false;
            }
            products.merge(product, quantity, Integer::sum);
            revenue.put(exchangeProduct, revenue.get(exchangeProduct) - exchangeQuantity);
            if (revenue.get(exchangeProduct) == 0) {
                revenue.remove(exchangeProduct);
            }
            customer.removePurchase(product, quantity);
            return true;
        }
        System.out.println("Товар не был куплен этим покупателем");
        return false;
    }

    // Добавление сотрудника
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.assignTo(this);
    }

    // Удаление сотрудника
    public boolean removeEmployee(int employeeId) {
        Employee employee = employees.stream().filter(e -> e.getId() == employeeId).findFirst().orElse(null);
        if (employee == null) {
            System.out.println("Сотрудник с ID " + employeeId + " не найден");
            return false;
        }
        employees.remove(employee);
        employee.assignTo(null);
        return true;
    }

    @Override
    public void printInfo() {
        System.out.println("Пункт продаж ID: " + id);
        System.out.println("Название " + name);
        System.out.println("Товары:");
        if (products.isEmpty()) {
            System.out.println("  Нет товаров");
        } else {
            products.forEach((product, quantity) ->
                    System.out.println("  " + product.getName() + " (ID: " + product.getId() + "), Количество: " + quantity));
        }
        System.out.println("Выручка: " + revenue);
        System.out.println("Сотрудники:");
        if (employees.isEmpty()) {
            System.out.println("  Нет сотрудников");
        } else {
            employees.forEach(Employee::printInfo);
        }
    }

    // обновление счетчика
    public static void updateNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }


}
