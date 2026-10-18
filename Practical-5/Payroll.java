abstract class Employee{
    String name;
    int id;

    public Employee(String name,int id) {
        this.name = name;
        this.id = id;
    }
    abstract double monthlySalary();
}

class FullTime extends Employee{
    double fixedSalary;

    FullTime(String name, int id, double fixedSalary) {
        super(name, id);
        this.fixedSalary = fixedSalary;
    }

    @Override
    double monthlySalary(){
            return fixedSalary;
    }
}

class PartTime extends Employee{
    int Hour;
    int rate;
    public PartTime(String name,int id,int Hour, int rate) {
        super(name, id);
        this.Hour = Hour;
        this.rate = rate;
    }
    double monthlySalary(){
        return Hour * rate;
    }
    
}

class Intern extends Employee{
    double stiped;
    public Intern(String name, int id,double stiped){
        super(name,id);
        this.stiped = stiped;
    }

    double monthlySalary(){
        return stiped;
    }
}

class Payroll{
    public static void main(String args[]){
        Employee[] employees = {
         new FullTime("Piyush", 101, 57000),
            new PartTime("Ujjaval", 102, 80, 400),
            new Intern("Krish", 103, 25000),
            new FullTime("Aaryan", 104, 40000),
            new Intern("Manav", 105, 12000)
        };
        double total = 0;
          for (Employee e : employees) {

            double salary = e.monthlySalary();

            System.out.println(
                "Name: " + e.name +
                ", ID: " + e.id +
                ", Salary: " + salary
            );

            if (e instanceof Intern) {
                System.out.println("Note: This employee is an Intern.");
            }

            total += salary;
        }

        System.out.println("----------------------");
        System.out.println("Total Payroll = " + total);
    }
}
