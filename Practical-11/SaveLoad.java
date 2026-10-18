
import java.io.*;

class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    int id;
    String name;
    transient String password;

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println("ID       : " + id);
        System.out.println("Name     : " + name);
        System.out.println("Password : " + password);
    }
}

public class SaveLoad {

    public static void main(String[] args) {

        String fileName = "students.dat";

        Student[] students = {
            new Student(101, "Rahul", "rahul123"),
            new Student(102, "Priya", "priya456")
        };

        System.out.println("Before Saving:");

        for (Student s : students) {
            s.display();
            System.out.println();
        }

        try (ObjectOutputStream out =
                 new ObjectOutputStream(
                     new FileOutputStream(fileName))) {

            out.writeObject(students);

            System.out.println("Objects saved successfully.\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(
                     new FileInputStream(fileName))) {

            Student[] loadedStudents =
                (Student[]) in.readObject();

            System.out.println("After Loading:");

            for (Student s : loadedStudents) {
                s.display();
                System.out.println();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
