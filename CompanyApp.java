import java.util.Scanner;

public class CompanyApp {
    private Company company;
    private Scanner scan;


    public CompanyApp() {
        this.company = new Company();
        this.scan = new Scanner(System.in);
    }


    public void run() {
        while (true) {
            System.out.println("\n Управление товарооборотом компании ");
            System.out.println("Добавить товар - 1");
            System.out.println("Открыть склад - 2");
            System.out.println("Открыть пункт продаж - 3");
            System.out.println("Нанять сотрудника - 4");
            System.out.println("Зарегистрировать клиента - 5");
            System.out.println("Добавить ячейку на склад - 6");
            System.out.println("Закупить товар на склад - 7");
            System.out.println("Продать товар (обмен) - 8");
            System.out.println("Вернуть товар - 9");
            System.out.println("Переместить товар между ячейками - 10");
            System.out.println("Назначить ответственное лицо - 11");
            System.out.println("Уволить сотрудника - 12");
            System.out.println("Закрыть склад - 13");
            System.out.println("Закрыть пункт продаж - 14");
            System.out.println("Информация о складе - 15");
            System.out.println("Информация о пункте продаж - 16");
            System.out.println("Информация о товарах на складе - 17");
            System.out.println("Информация о товарах в пункте продаж - 18");
            System.out.println("Информация о доступных товарах - 19");
            System.out.println("Информация о доходности - 20");
            System.out.println("Полная информация о компании - 21");
            System.out.println("Выход - 0");
            System.out.print("Выберите действие: ");

            try {
                int choice = Integer.parseInt(scan.nextLine());
                switch (choice) {
                    case 1:
                        addProduct();
                        break;
                    case 2:
                        addStorage();
                        break;
                    case 3:
                        addSalesPoint();
                        break;
                    case 4:
                        hireEmployee();
                        break;
                    case 5:
                        registerCustomer();
                        break;
                    case 6:
                        addStorageCell();
                        break;
                    case 7:
                        purchaseProduct();
                        break;
                    case 8:
                        sellProduct();
                        break;
                    case 9:
                        returnProduct();
                        break;
                    case 10:
                        moveProduct();
                        break;
                    case 11:
                        assignEmployee();
                        break;
                    case 12:
                        fireEmployee();
                        break;
                    case 13:
                        closeStorage();
                        break;
                    case 14:
                        closeSalesPoint();
                        break;
                    case 15:
                        showStorageInfo();
                        break;
                    case 16:
                        showSalesPointInfo();
                        break;
                    case 17:
                        showStorageProducts();
                        break;
                    case 18:
                        showSalesPointProducts();
                        break;
                    case 19:
                        showAvailableProducts();
                        break;
                    case 20:
                        showRevenue();
                        break;
                    case 21:
                        company.printInfo();
                        break;
                    case 0:
                        System.out.println("Выход...");
                        return;
                    default:
                        System.out.println("Неверный выбор, попробуйте снова");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число");
            }
        }
    }


    private void addProduct() {
        System.out.print("Введите название товара: ");
        String name = scan.nextLine();
        System.out.print("Введите цену товара: ");
        String price = scan.nextLine();
        company.addProduct(new Product(name, price));
        company.saveToFile();
        System.out.println("Товар добавлен");
    }

    private void addStorage() {
        System.out.print("Введите название склада: ");
        String name = scan.nextLine();
        Storage storage = new Storage(name);
        company.addStorage(storage);
        company.saveToFile();
        System.out.println("Склад открыт с ID: " + storage.getId());
    }

    private void addSalesPoint() {
        System.out.print("Введите название пункта продаж: ");
        String name = scan.nextLine();
        PointOfSale pos = new PointOfSale(name);
        company.addSalesPoint(pos);
        company.saveToFile();
        System.out.println("Пункт продаж открыт с ID: " + pos.getId());
    }

    private void hireEmployee() {
        System.out.print("Введите возраст сотрудника: ");
        int age = Integer.parseInt(scan.nextLine());
        System.out.print("Введите имя сотрудника: ");
        String name = scan.nextLine();
        System.out.print("Введите пол сотрудника(1 - муж/0 - жен): ");
        boolean sex = Boolean.parseBoolean(scan.nextLine());
        System.out.print("Введите должность сотрудника: ");
        String position = scan.nextLine();
        System.out.print("Сотрудник ответстренный?: ");
        boolean isGood = Boolean.parseBoolean(scan.nextLine());
        company.addEmployee(new Employee(age,  name,  sex, position, isGood));
        company.saveToFile();
        System.out.println("Сотрудник нанят");
    }

    private void registerCustomer() {
        System.out.print("Введите возраст клиента: ");
        int age = Integer.parseInt(scan.nextLine());
        System.out.print("Введите имя клиента: ");
        String name = scan.nextLine();
        System.out.print("Введите пол клиента(1 - муж/0 - жен): ");
        boolean sex = Boolean.parseBoolean(scan.nextLine());
        System.out.print("Введите номер заказа: ");
        int  orderNumber = scan.nextInt();
        company.addCustomer(new Customer(age,  name, sex,  orderNumber));
        company.saveToFile();
        System.out.println("Клиент зарегистрирован");
    }

    private void addStorageCell() {
        System.out.print("Введите ID склада: ");
        int storageId = Integer.parseInt(scan.nextLine());
        Storage storage = company.getStorageById(storageId);
        if (storage == null) {
            System.out.println("Склад не найден");
            return;
        }
        System.out.print("Введите вместимость ячейки: ");
        int capacity = Integer.parseInt(scan.nextLine());
        storage.addCell(capacity);
        company.saveToFile();
        System.out.println("Ячейка добавлена");
    }

    private void purchaseProduct() {
        System.out.print("Введите ID склада: ");
        int storageId = Integer.parseInt(scan.nextLine());
        Storage storage = company.getStorageById(storageId);
        if (storage == null) {
            System.out.println("Склад не найден");
            return;
        }
        System.out.print("Введите ID ячейки: ");
        int cellId = Integer.parseInt(scan.nextLine());
        StorageCell cell = storage.getCells().stream()
                .filter(c -> c.getId() == cellId)
                .findFirst()
                .orElse(null);
        if (cell == null) {
            System.out.println("Ячейка не найдена");
            return;
        }
        System.out.print("Введите ID товара: ");
        int productId = Integer.parseInt(scan.nextLine());
        Product product = company.getProductById(productId);
        if (product == null) {
            System.out.println("Товар не найден");
            return;
        }
        System.out.print("Введите количество: ");
        int quantity = Integer.parseInt(scan.nextLine());
        if (cell.addProduct(product, quantity)) {
            company.saveToFile();
            System.out.println("Товар закуплен и помещен на склад");
        }
    }

    private void sellProduct() {
        System.out.print("Введите ID пункта продаж: ");
        int posId = Integer.parseInt(scan.nextLine());
        PointOfSale pos = company.getSalesPointById(posId);
        if (pos == null) {
            System.out.println("Пункт продаж не найден");
            return;
        }
        System.out.print("Введите ID товара: ");
        int productId = Integer.parseInt(scan.nextLine());
        Product product = company.getProductById(productId);
        if (product == null) {
            System.out.println("Товар не найден");
            return;
        }
        System.out.print("Введите количество: ");
        int quantity = Integer.parseInt(scan.nextLine());
        System.out.print("Введите название обменной вещи: ");
        String exchangeItem = scan.nextLine();
        System.out.print("Введите количество обменной вещи: ");
        int exchangeQuantity = Integer.parseInt(scan.nextLine());
        System.out.print("Введите ID клиента: ");
        int customerId = Integer.parseInt(scan.nextLine());
        Customer customer = company.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Клиент не найден");
            return;
        }
        if (pos.sellProduct(product, quantity, exchangeItem, exchangeQuantity, customer)) {
            company.addRevenue(exchangeItem, exchangeQuantity);
            company.saveToFile();
            System.out.println("Товар продан");

        }
    }

    private void returnProduct() {
        System.out.print("Введите ID пункта продаж: ");
        int posId = Integer.parseInt(scan.nextLine());
        PointOfSale pos = company.getSalesPointById(posId);
        if (pos == null) {
            System.out.println("Пункт продаж не найден");
            return;
        }
        System.out.print("Введите ID товара: ");
        int productId = Integer.parseInt(scan.nextLine());
        Product product = company.getProductById(productId);
        if (product == null) {
            System.out.println("Товар не найден");
            return;
        }
        System.out.print("Введите количество: ");
        int quantity = Integer.parseInt(scan.nextLine());
        System.out.print("Введите название обменной вещи: ");
        String exchangeItem = scan.nextLine();
        System.out.print("Введите количество обменной вещи: ");
        int exchangeQuantity = Integer.parseInt(scan.nextLine());
        System.out.print("Введите ID клиента: ");
        int customerId = Integer.parseInt(scan.nextLine());
        Customer customer = company.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Клиент не найден");
            return;
        }
        if (pos.returnProduct(product, quantity, exchangeItem, exchangeQuantity, customer)) {
            company.removeRevenue(exchangeItem, exchangeQuantity);
            company.saveToFile();
            System.out.println("Товар возвращен");
        }
    }

    private void moveProduct() {
        System.out.print("Введите ID товара: ");
        int productId = Integer.parseInt(scan.nextLine());
        System.out.print("Введите количество: ");
        int quantity = Integer.parseInt(scan.nextLine());
        System.out.print("Введите ID склада-источника: ");
        int fromStorageId = Integer.parseInt(scan.nextLine());
        System.out.print("Введите ID ячейки-источника: ");
        int fromCellId = Integer.parseInt(scan.nextLine());
        System.out.print("Введите ID склада-назначения: ");
        int toStorageId = Integer.parseInt(scan.nextLine());
        System.out.print("Введите ID ячейки-назначения: ");
        int toCellId = Integer.parseInt(scan.nextLine());
        if (company.moveProduct(productId, quantity, fromStorageId, fromCellId, toStorageId, toCellId)) {
            company.saveToFile();
            System.out.println("Товар перемещен");
        }
    }

    private void assignEmployee() {
        System.out.print("Введите ID сотрудника: ");
        int employeeId = Integer.parseInt(scan.nextLine());
        Employee employee = company.getEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Сотрудник не найден");
            return;
        }
        System.out.print("Введите тип назначения (1 - склад, 2 - пункт продаж): ");
        int type = Integer.parseInt(scan.nextLine());
        if (type == 1) {
            System.out.print("Введите ID склада: ");
            int storageId = Integer.parseInt(scan.nextLine());
            Storage storage = company.getStorageById(storageId);
            if (storage == null) {
                System.out.println("Склад не найден");
                return;
            }
            storage.setResponsibleEmployee(employee);
            company.saveToFile();
            System.out.println("Сотрудник назначен на склад");
        } else if (type == 2) {
            System.out.print("Введите ID пункта продаж: ");
            int posId = Integer.parseInt(scan.nextLine());
            PointOfSale pos = company.getSalesPointById(posId);
            if (pos == null) {
                System.out.println("Пункт продаж не найден");
                return;
            }
            pos.addEmployee(employee);
            company.saveToFile();
            System.out.println("Сотрудник назначен на пункт продаж");
        } else {
            System.out.println("Неверный тип назначения");
        }
    }

    private void fireEmployee() {
        System.out.print("Введите ID сотрудника: ");
        int employeeId = Integer.parseInt(scan.nextLine());
        if (company.fireEmployee(employeeId)) {
            company.saveToFile();
            System.out.println("Сотрудник уволен");
        }
    }

    private void closeStorage() {
        System.out.print("Введите ID склада: ");
        int storageId = Integer.parseInt(scan.nextLine());
        if (company.removeStorage(storageId)) {
            company.saveToFile();
            System.out.println("Склад закрыт");
        }
    }

    private void closeSalesPoint() {
        System.out.print("Введите ID пункта продаж: ");
        int posId = Integer.parseInt(scan.nextLine());
        if (company.removeSalesPoint(posId)) {
            company.saveToFile();
            System.out.println("Пункт продаж закрыт");
        }
    }

    private void showStorageInfo() {
        System.out.print("Введите ID склада: ");
        int storageId = Integer.parseInt(scan.nextLine());
        Storage storage = company.getStorageById(storageId);
        if (storage == null) {
            System.out.println("Склад не найден");
            return;
        }
        storage.printInfo();
    }

    private void showSalesPointInfo() {
        System.out.print("Введите ID пункта продаж: ");
        int posId = Integer.parseInt(scan.nextLine());
        PointOfSale pos = company.getSalesPointById(posId);
        if (pos == null) {
            System.out.println("Пункт продаж не найден");
            return;
        }
        pos.printInfo();
    }

    private void showStorageProducts() {
        System.out.print("Введите ID склада: ");
        int storageId = Integer.parseInt(scan.nextLine());
        Storage storage = company.getStorageById(storageId);
        if (storage == null) {
            System.out.println("Склад не найден");
            return;
        }
        System.out.println("Товары на складе ID: " + storageId);
        storage.getCells().forEach(cell -> {
            System.out.println("Ячейка ID: " + cell.getId());
            cell.getStock().forEach((product, quantity) -> {
                product.printInfo(); // Используем printInfo для товара
                System.out.println("  Количество: " + quantity);
            });
        });}

    private void showSalesPointProducts() {
        System.out.print("Введите ID пункта продаж: ");
        int posId = Integer.parseInt(scan.nextLine());
        PointOfSale pos = company.getSalesPointById(posId);
        if (pos == null) {
            System.out.println("Пункт продаж не найден");
            return;
        }
        System.out.println("Товары в пункте продаж ID: " + posId);
        pos.getProducts().forEach((product, quantity) -> {
            product.printInfo(); // Используем printInfo для товара
            System.out.println("  Количество: " + quantity);
        });}

    private void showAvailableProducts() {
        System.out.println("Доступные товары:");
        company.getProducts().forEach(Product::printInfo);
    }

    private void showRevenue() {
        System.out.print("Введите ID пункта продаж (-1 для общей выручки): ");
        int posId = Integer.parseInt(scan.nextLine());
        if (posId == -1) {
            System.out.println("Общая выручка компании:");
            company.getTotalRevenue().forEach((item, quantity) ->
                    System.out.println("  " + item + ", Количество: " + quantity));
        } else {
            PointOfSale pos = company.getSalesPointById(posId);
            if (pos == null) {
                System.out.println("Пункт продаж не найден");
                return;
            }
            System.out.println("Выручка пункта продаж ID: " + posId);
            pos.getRevenue().forEach((item, quantity) ->
                    System.out.println("  " + item + ", Количество: " + quantity));
        }
    }


}