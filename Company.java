import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.FileInputStream;


public class Company implements Printable {
    private List<Product> products;
    private List<Storage> storages;
    private List<PointOfSale> salesPoints;
    private List<Employee> employees;
    private List<Customer> customers;
    private Map<String, Integer> totalRevenue;
    private static final String DATA_FILE = "company_data.xlsx";

    public Company() {
        this.products = new ArrayList<>();
        this.storages = new ArrayList<>();
        this.salesPoints = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.totalRevenue = new HashMap<>();
        this.customers = new ArrayList<>();
        loadFromFile();
    }

    // добавления
    public void addProduct(Product product) {
        products.add(product);
    }

    public void addStorage(Storage storage) {
        storages.add(storage);
    }

    public void addSalesPoint(PointOfSale pos) {
        salesPoints.add(pos);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    // Геттеры для списков
    public List<Product> getProducts() {
        return products;
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public List<PointOfSale> getSalesPoints() {
        return salesPoints;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Map<String, Integer> getTotalRevenue() {
        return totalRevenue;
    }

    // поиск по id
    public Product getProductById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public Storage getStorageById(int id) {
        return storages.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public PointOfSale getSalesPointById(int id) {
        return salesPoints.stream().filter(sp -> sp.getId() == id).findFirst().orElse(null);
    }

    public Employee getEmployeeById(int id) {
        return employees.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public Customer getCustomerById(int id) {
        return customers.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    // - склад
    public boolean removeStorage(int storageId) {
        Storage storage = getStorageById(storageId);
        if (storage == null) {
            System.out.println("Склад не найден");
            return false;
        }
        if (storage.getCells().stream().anyMatch(cell -> !cell.getStock().isEmpty())) {
            System.out.println("Склад не пуст");
            return false;
        }
        storages.remove(storage);
        saveToFile();
        return true;
    }

    // - пункт продаж
    public boolean removeSalesPoint(int posId) {
        PointOfSale pos = getSalesPointById(posId);
        if (pos == null) {
            System.out.println("Пункт продаж не найден");
            return false;
        }
        if (!pos.getProducts().isEmpty()) {
            System.out.println("Пункт продаж не пуст");
            return false;
        }
        salesPoints.remove(pos);
        saveToFile();
        return true;
    }

    // Увольнение сотрудника
    public boolean fireEmployee(int employeeId) {
        Employee employee = getEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Сотрудник не найден");
            return false;
        }
        if (employee.getAssignedTo() != null) {
            if (employee.getAssignedTo() instanceof Storage) {
                ((Storage) employee.getAssignedTo()).setResponsibleEmployee(null);
            } else {
                ((PointOfSale) employee.getAssignedTo()).removeEmployee(employeeId);
            }
        }
        employees.remove(employee);
        saveToFile();
        return true;
    }

    // Перемещение товара
    public boolean moveProduct(int productId, int quantity, int fromStorageId, int fromCellId, int toStorageId, int toCellId) {
        Storage fromStorage = getStorageById(fromStorageId);
        Storage toStorage = getStorageById(toStorageId);
        Product product = getProductById(productId);
        if (fromStorage == null || toStorage == null || product == null) {
            System.out.println("Неверные данные для перемещения");
            return false;
        }
        StorageCell fromCell = fromStorage.getCells().stream().filter(c -> c.getId() == fromCellId).findFirst().orElse(null);
        StorageCell toCell = toStorage.getCells().stream().filter(c -> c.getId() == toCellId).findFirst().orElse(null);
        if (fromCell == null || toCell == null) {
            System.out.println("Ячейки не найдены");
            return false;
        }
        if (fromCell.removeProduct(product, quantity) && toCell.addProduct(product, quantity)) {
            saveToFile();
            return true;
        }
        return false;
    }

    // + выручка в общую выручку компании
    public void addRevenue(String item, int quantity) {
        totalRevenue.put(item, totalRevenue.getOrDefault(item, 0) + quantity);
        saveToFile();
    }

    // - выручка при возврате
    public void removeRevenue(String item, int quantity) {
        if (totalRevenue.containsKey(item)) {
            int newQuantity = totalRevenue.get(item) - quantity;
            if (newQuantity <= 0) {
                totalRevenue.remove(item);
            } else {
                totalRevenue.put(item, newQuantity);
            }
            saveToFile();
        }
    }


    // Вывод информации о компании
    @Override
    public void printInfo() {
        System.out.println("Компания:");
        System.out.println("Товары:");
        if (products.isEmpty()) {
            System.out.println("  Нет товаров");
        } else {
            products.forEach(Product::printInfo);
        }
        System.out.println("Склады:");
        if (storages.isEmpty()) {
            System.out.println("  Нет складов");
        } else {
            storages.forEach(Storage::printInfo);
        }
        System.out.println("Пункты продаж:");
        if (salesPoints.isEmpty()) {
            System.out.println("  Нет пунктов продаж");
        } else {
            salesPoints.forEach(PointOfSale::printInfo);
        }
        System.out.println("Сотрудники:");
        if (employees.isEmpty()) {
            System.out.println("  Нет сотрудников");
        } else {
            employees.forEach(Employee::printInfo);
        }
        System.out.println("Клиенты:");
        if (customers.isEmpty()) {
            System.out.println("  Нет клиентов");
        } else {
            customers.forEach(Customer::printInfo);
        }
        System.out.println("Общая выручка (полученные вещи):");
        if (totalRevenue.isEmpty()) {
            System.out.println("  Нет выручки");
        } else {
            totalRevenue.forEach((item, quantity) ->
                    System.out.println("  " + item + ", Количество: " + quantity));
        }
    }

    // охранение данных в файл
    public void saveToFile() {
        try (Workbook workbook = new XSSFWorkbook()) {
            // лист продуктов
            Sheet productsSheet = workbook.createSheet("Products");
            Row header = productsSheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Price");
            int rowNum = 1;
            for (Product p : products) {
                Row row = productsSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getPrice());
            }

            // лист складов
            Sheet storagesSheet = workbook.createSheet("Storages");
            header = storagesSheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("ResponsibleEmployeeID");
            rowNum = 1;
            for (Storage s : storages) {
                Row row = storagesSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getName());
                Employee emp = s.getResponsibleEmployee();
                row.createCell(2).setCellValue(emp != null ? emp.getId() : -1);
            }

            // лист ячеек склада
            Sheet cellsSheet = workbook.createSheet("StorageCells");
            header = cellsSheet.createRow(0);
            header.createCell(0).setCellValue("StorageID");
            header.createCell(1).setCellValue("CellID");
            header.createCell(2).setCellValue("Capacity");
            rowNum = 1;
            for (Storage s : storages) {
                for (StorageCell c : s.getCells()) {
                    Row row = cellsSheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(s.getId());
                    row.createCell(1).setCellValue(c.getId());
                    row.createCell(2).setCellValue(c.getCapacity());
                }
            }

            // лист запасов склада
            Sheet stockSheet = workbook.createSheet("StorageStock");
            header = stockSheet.createRow(0);
            header.createCell(0).setCellValue("StorageID");
            header.createCell(1).setCellValue("CellID");
            header.createCell(2).setCellValue("ProductID");
            header.createCell(3).setCellValue("Quantity");
            rowNum = 1;
            for (Storage s : storages) {
                for (StorageCell c : s.getCells()) {
                    for (Map.Entry<Product, Integer> entry : c.getStock().entrySet()) {
                        Row row = stockSheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(s.getId());
                        row.createCell(1).setCellValue(c.getId());
                        row.createCell(2).setCellValue(entry.getKey().getId());
                        row.createCell(3).setCellValue(entry.getValue());
                    }
                }
            }

            // лист пунктов продаж
            Sheet salesPointsSheet = workbook.createSheet("SalesPoints");
            header = salesPointsSheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            rowNum = 1;
            for (PointOfSale pos : salesPoints) {
                Row row = salesPointsSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pos.getId());
                row.createCell(1).setCellValue(pos.getName());
            }

            // лист товаров в пунктах продаж
            Sheet posProductsSheet = workbook.createSheet("SalesPointProducts");
            header = posProductsSheet.createRow(0);
            header.createCell(0).setCellValue("SalesPointID");
            header.createCell(1).setCellValue("ProductID");
            header.createCell(2).setCellValue("Quantity");
            rowNum = 1;
            for (PointOfSale pos : salesPoints) {
                for (Map.Entry<Product, Integer> entry : pos.getProducts().entrySet()) {
                    Row row = posProductsSheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(pos.getId());
                    row.createCell(1).setCellValue(entry.getKey().getId());
                    row.createCell(2).setCellValue(entry.getValue());
                }
            }

            // лист выручки пунктов продаж
            Sheet revenueSheet = workbook.createSheet("Revenue");
            header = revenueSheet.createRow(0);
            header.createCell(0).setCellValue("SalesPointID");
            header.createCell(1).setCellValue("ExchangeProduct");
            header.createCell(2).setCellValue("Quantity");
            rowNum = 1;
            for (PointOfSale pos : salesPoints) {
                for (Map.Entry<String, Integer> entry : pos.getRevenue().entrySet()) {
                    Row row = revenueSheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(pos.getId());
                    row.createCell(1).setCellValue(entry.getKey());
                    row.createCell(2).setCellValue(entry.getValue());
                }
            }

            // лист сотрудников
            Sheet employeesSheet = workbook.createSheet("Employees");
            header = employeesSheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Age");
            header.createCell(2).setCellValue("Name");
            header.createCell(3).setCellValue("Sex");
            header.createCell(4).setCellValue("Position");
            header.createCell(5).setCellValue("IsGood");
            header.createCell(6).setCellValue("AssignedType");
            header.createCell(7).setCellValue("AssignedID");
            rowNum = 1;
            for (Employee e : employees) {
                Row row = employeesSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(e.getId());
                row.createCell(1).setCellValue(e.getAge());
                row.createCell(2).setCellValue(e.getName());
                row.createCell(3).setCellValue(e.getSex() ? "M" : "F");
                row.createCell(4).setCellValue(e.getPosition());
                row.createCell(5).setCellValue(e.getIsGood());
                Object assignedTo = e.getAssignedTo();
                if (assignedTo instanceof Storage) {
                    row.createCell(6).setCellValue("Storage");
                    row.createCell(7).setCellValue(((Storage) assignedTo).getId());
                } else if (assignedTo instanceof PointOfSale) {
                    row.createCell(6).setCellValue("SalesPoint");
                    row.createCell(7).setCellValue(((PointOfSale) assignedTo).getId());
                } else {
                    row.createCell(6).setCellValue("None");
                    row.createCell(7).setCellValue(-1);
                }
            }

            // лист клиентов
            Sheet customersSheet = workbook.createSheet("Customers");
            header = customersSheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Age");
            header.createCell(2).setCellValue("Name");
            header.createCell(3).setCellValue("Sex");
            header.createCell(4).setCellValue("OrderNumber");
            rowNum = 1;
            for (Customer c : customers) {
                Row row = customersSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(c.getId());
                row.createCell(1).setCellValue(c.getAge());
                row.createCell(2).setCellValue(c.getName());
                row.createCell(3).setCellValue(c.getSex() ? "M" : "F");
                row.createCell(4).setCellValue(c.getOrderNumber());
            }

            // лист заказов клиентов
            Sheet ordersSheet = workbook.createSheet("CustomerOrders");
            header = ordersSheet.createRow(0);
            header.createCell(0).setCellValue("CustomerID");
            header.createCell(1).setCellValue("ProductID");
            header.createCell(2).setCellValue("Quantity");
            rowNum = 1;
            for (Customer c : customers) {
                for (Map.Entry<Product, Integer> entry : c.getOrder().entrySet()) {
                    Row row = ordersSheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(c.getId());
                    row.createCell(1).setCellValue(entry.getKey().getId());
                    row.createCell(2).setCellValue(entry.getValue());
                }
            }

            // Запись в файл
            try (FileOutputStream fileOut = new FileOutputStream(DATA_FILE)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    // метод загрузки данных из файла
    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return; // Если файла нет, оставляем списки пустыми
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // загрузка продуктов
            Sheet productsSheet = workbook.getSheet("Products");
            if (productsSheet != null) {
                for (int i = 1; i <= productsSheet.getLastRowNum(); i++) {
                    Row row = productsSheet.getRow(i);
                    int id = (int) row.getCell(0).getNumericCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    String price = row.getCell(2).getStringCellValue();
                    Product p = new Product(name, price);
                    p.setId(id);
                    products.add(p);
                    Product.updateNextId(id);
                }
            }

            // загрузка складов
            Sheet storagesSheet = workbook.getSheet("Storages");
            if (storagesSheet != null) {
                for (int i = 1; i <= storagesSheet.getLastRowNum(); i++) {
                    Row row = storagesSheet.getRow(i);
                    int id = (int) row.getCell(0).getNumericCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    Storage s = new Storage(name);
                    s.setId(id);
                    storages.add(s);
                    Storage.updateNextId(id);
                }
            }

            // загрузка ячеек склада
            Sheet cellsSheet = workbook.getSheet("StorageCells");
            if (cellsSheet != null) {
                for (int i = 1; i <= cellsSheet.getLastRowNum(); i++) {
                    Row row = cellsSheet.getRow(i);
                    int storageId = (int) row.getCell(0).getNumericCellValue();
                    int cellId = (int) row.getCell(1).getNumericCellValue();
                    int capacity = (int) row.getCell(2).getNumericCellValue();
                    Storage s = getStorageById(storageId);
                    if (s != null) {
                        StorageCell c = new StorageCell(capacity);
                        c.setId(cellId);
                        s.getCells().add(c);
                        StorageCell.updateNextId(cellId);
                    }
                }
            }

            // загрузка запасов склада
            Sheet stockSheet = workbook.getSheet("StorageStock");
            if (stockSheet != null) {
                for (int i = 1; i <= stockSheet.getLastRowNum(); i++) {
                    Row row = stockSheet.getRow(i);
                    int storageId = (int) row.getCell(0).getNumericCellValue();
                    int cellId = (int) row.getCell(1).getNumericCellValue();
                    int productId = (int) row.getCell(2).getNumericCellValue();
                    int quantity = (int) row.getCell(3).getNumericCellValue();
                    Storage s = getStorageById(storageId);
                    if (s != null) {
                        StorageCell c = s.getCells().stream().filter(cell -> cell.getId() == cellId).findFirst().orElse(null);
                        if (c != null) {
                            Product p = getProductById(productId);
                            if (p != null) {
                                c.getStock().put(p, quantity);
                            }
                        }
                    }
                }
            }

            // загрузка пунктов продаж
            Sheet salesPointsSheet = workbook.getSheet("SalesPoints");
            if (salesPointsSheet != null) {
                for (int i = 1; i <= salesPointsSheet.getLastRowNum(); i++) {
                    Row row = salesPointsSheet.getRow(i);
                    int id = (int) row.getCell(0).getNumericCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    PointOfSale pos = new PointOfSale(name);
                    pos.setId(id);
                    salesPoints.add(pos);
                    PointOfSale.updateNextId(id);
                }
            }

            // загрузка товаров в пунктах продаж
            Sheet posProductsSheet = workbook.getSheet("SalesPointProducts");
            if (posProductsSheet != null) {
                for (int i = 1; i <= posProductsSheet.getLastRowNum(); i++) {
                    Row row = posProductsSheet.getRow(i);
                    int posId = (int) row.getCell(0).getNumericCellValue();
                    int productId = (int) row.getCell(1).getNumericCellValue();
                    int quantity = (int) row.getCell(2).getNumericCellValue();
                    PointOfSale pos = getSalesPointById(posId);
                    if (pos != null) {
                        Product p = getProductById(productId);
                        if (p != null) {
                            pos.getProducts().put(p, quantity);
                        }
                    }
                }
            }

            // загрузка выручки пунктов продаж
            Sheet revenueSheet = workbook.getSheet("Revenue");
            if (revenueSheet != null) {
                for (int i = 1; i <= revenueSheet.getLastRowNum(); i++) {
                    Row row = revenueSheet.getRow(i);
                    int posId = (int) row.getCell(0).getNumericCellValue();
                    String exchangeProduct = row.getCell(1).getStringCellValue();
                    int quantity = (int) row.getCell(2).getNumericCellValue();
                    PointOfSale pos = getSalesPointById(posId);
                    if (pos != null) {
                        pos.getRevenue().put(exchangeProduct, quantity);
                    }
                }
            }

            // загрузка сотрудников
            Sheet employeesSheet = workbook.getSheet("Employees");
            if (employeesSheet != null) {
                for (int i = 1; i <= employeesSheet.getLastRowNum(); i++) {
                    Row row = employeesSheet.getRow(i);
                    int id = (int) row.getCell(0).getNumericCellValue();
                    int age = (int) row.getCell(1).getNumericCellValue();
                    String name = row.getCell(2).getStringCellValue();
                    boolean sex = row.getCell(3).getStringCellValue().equals("M");
                    String position = row.getCell(4).getStringCellValue();
                    boolean isGood = row.getCell(5).getBooleanCellValue();
                    Employee e = new Employee(age, name, sex, position, isGood);
                    e.setId(id);
                    employees.add(e);
                    Human.updateNextId(id);
                }
            }

            // загрузка клиентов
            Sheet customersSheet = workbook.getSheet("Customers");
            if (customersSheet != null) {
                for (int i = 1; i <= customersSheet.getLastRowNum(); i++) {
                    Row row = customersSheet.getRow(i);
                    int id = (int) row.getCell(0).getNumericCellValue();
                    int age = (int) row.getCell(1).getNumericCellValue();
                    String name = row.getCell(2).getStringCellValue();
                    boolean sex = row.getCell(3).getStringCellValue().equals("M");
                    int orderNumber = (int) row.getCell(4).getNumericCellValue();
                    Customer c = new Customer(age, name, sex, orderNumber);
                    c.setId(id);
                    customers.add(c);
                    Human.updateNextId(id);
                }
            }

            // загрузка заказов клиентов
            Sheet ordersSheet = workbook.getSheet("CustomerOrders");
            if (ordersSheet != null) {
                for (int i = 1; i <= ordersSheet.getLastRowNum(); i++) {
                    Row row = ordersSheet.getRow(i);
                    int customerId = (int) row.getCell(0).getNumericCellValue();
                    int productId = (int) row.getCell(1).getNumericCellValue();
                    int quantity = (int) row.getCell(2).getNumericCellValue();
                    Customer c = getCustomerById(customerId);
                    if (c != null) {
                        Product p = getProductById(productId);
                        if (p != null) {
                            c.getOrder().put(p, quantity);
                        }
                    }
                }
            }

            // восстановление связей для сотрудников и складов
            for (int i = 1; i <= employeesSheet.getLastRowNum(); i++) {
                Row row = employeesSheet.getRow(i);
                int id = (int) row.getCell(0).getNumericCellValue();
                String assignedType = row.getCell(6).getStringCellValue();
                int assignedId = (int) row.getCell(7).getNumericCellValue();
                Employee e = getEmployeeById(id);
                if (e != null) {
                    if (assignedType.equals("Storage") && assignedId != -1) {
                        Storage s = getStorageById(assignedId);
                        if (s != null) {
                            s.setResponsibleEmployee(e);
                        }
                    } else if (assignedType.equals("SalesPoint") && assignedId != -1) {
                        PointOfSale pos = getSalesPointById(assignedId);
                        if (pos != null) {
                            pos.addEmployee(e);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

}
