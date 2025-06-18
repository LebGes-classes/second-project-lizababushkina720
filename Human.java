public class Human implements Printable{
    private int age;
    private String name;
    private boolean sex;
    private int id;
    private static int nextId = 1;// счетчик id
    Human(int age,  String name, boolean sex){
        this.name=name;
        this.age=age;
        this.sex=sex;
        this.id=nextId++;

    }

    public int getAge(){
        return age;
    }

    public void setId(int id) { this.id = id; }

    public String getName(){
        return name;
    }

    public boolean getSex(){
        return sex;
    }

    public int getId(){
        return id;
    }

    // генерация id
    public static void updateNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    @Override
    public void printInfo(){
            System.out.println(
                        "Id: " + id + "\n" +
                        "Имя: " + name + "\n" +
                        "Возраст: " + age + "\n" +
                        "Пол: " + (sex ? "М" : "Ж"));

    }
}
