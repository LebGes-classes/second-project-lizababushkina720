import java.util.Objects;

public class Product implements Printable{
    private static int nextId = 1; // Счетчик для Id
    private String name;
    private String price;
    private int id;

    Product( String name, String price){
        this.id=nextId++;
        this.name=name;
        this.price = price;

    }

    // геттеры сеттеры
    public String getName(){
        return name;
    }

    public void setId(int id) { this.id = id; }

    public String getPrice(){
        return price;
    }

    public int getId(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public void printInfo() {
        System.out.println("ID: " + id + "\n"+"Название: " + name +"\n"+ "Цена: " + price);
    }

    // обновление счетчика
    public static void updateNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }



}
