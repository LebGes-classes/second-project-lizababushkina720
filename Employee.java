public class Employee extends Human implements Printable{
    private String position;
    private boolean isGood;// хорошо или плохо работает?
    private Object assignedTo; // связь со складом или пунктом продаж

    Employee(int age, String name, boolean sex,String position,boolean isGood){
        super(age,name,sex);
        this.position = position;
        this.assignedTo=null;
        setIsGood(isGood);
    }

    // геттеры и сеттеры
    public String getPosition(){
        return position;
    }

    public void setIsGood(boolean isGood){
        this.isGood = isGood;
    }

    public boolean getIsGood(){
        return isGood;
    }
    public Object getAssignedTo() {
        return assignedTo;
    }

    // назначение сотрудника на склад или пункт продаж
    public void assignTo(Object location) {
        this.assignedTo = location;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        String assigned = assignedTo == null ? "Не назначен" :
                (assignedTo instanceof Storage ? "Склад ID: " + ((Storage) assignedTo).getId() :
                        "Пункт продаж ID: " + ((PointOfSale) assignedTo).getId());
        System.out.println("Должность: " + position + "\n"+"Хороший сотрудник " + isGood+"\n"+  "Назначен: " + assigned);


    }


}
