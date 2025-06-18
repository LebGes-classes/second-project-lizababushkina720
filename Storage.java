import java.util.ArrayList;
import java.util.List;

public class Storage implements Printable{
    private static int nextId = 1; // счетчик для Id
    private int id;
    private String name;
    private List<StorageCell> cells;
    private Employee responsibleEmployee;

    public Storage(String name) {
        this.id = nextId++;
        this.cells = new ArrayList<>();
        this.responsibleEmployee = null;
        this.name=name;
    }

    // геттеры
    public int getId() {
        return id;
    }

    public String getName(){ return name; }


    public void setId(int id) { this.id = id; }

    public List<StorageCell> getCells() {
        return cells;
    }

    public Employee getResponsibleEmployee() {
        return responsibleEmployee;
    }

    // назначение ответственного
    public void setResponsibleEmployee(Employee employee) {
        this.responsibleEmployee = employee;
        if (employee != null) {
            employee.assignTo(this);
        }
    }

    // + ячейка
    public void addCell(int capacity) {
        cells.add(new StorageCell(capacity));
    }

    // - ячейка
    public boolean removeCell(int cellId) {
        StorageCell cell = cells.stream().filter(c -> c.getId() == cellId).findFirst().orElse(null);
        if (cell == null) {
            System.out.println("Ячейка с ID " + cellId + " не найдена");
            return false;
        }
        if (!cell.getStock().isEmpty()) {
            System.out.println("Ячейка " + cellId + " не пуста");
            return false;
        }
        cells.remove(cell);
        return true;
    }

    @Override
    public void printInfo() {
        System.out.println("Склад ID: " + id);
        System.out.println("Название: " +name );
        System.out.println("Ответственный: " + (responsibleEmployee != null ? responsibleEmployee.getName() : "Не назначен"));
        System.out.println("Ячейки: ");
        if (cells.isEmpty()) {
            System.out.println("Нет ячеек");
        } else {
            cells.forEach(StorageCell::printInfo);
        }
    }

    // обновление счетчика
    public static void updateNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

}
