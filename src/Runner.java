import data.Curator;
import data.Group;
import data.Student;
import db.IDBExecutor;
import db.MySqlExecutor;
import tables.AbsTable;
import tables.CuratorTable;
import tables.GroupTable;
import tables.StudentTable;

import java.util.ArrayList;
import java.util.List;

public class Runner {

    public static void main(String[] args) {
        IDBExecutor dbExecutor = new MySqlExecutor();
        try {
            AbsTable studentTable = new StudentTable(dbExecutor);
            AbsTable groupTable = new GroupTable(dbExecutor);
            AbsTable curatorTable = new CuratorTable(dbExecutor);


            List<String> columnsStudentTable = new ArrayList<>();
            columnsStudentTable.add("id int primary key");
            columnsStudentTable.add("fio varchar(50)");
            columnsStudentTable.add("sex varchar(1)");
            columnsStudentTable.add("id_group int");

            studentTable.createTable(columnsStudentTable);

            List<String> columnsGroupTable = new ArrayList<>();
            columnsGroupTable.add("id int primary key");
            columnsGroupTable.add("name varchar(10)");
            columnsGroupTable.add("id_curator int");

            groupTable.createTable(columnsGroupTable);

            List<String> columnsCuratorTable = new ArrayList<>();
            columnsCuratorTable.add("id int primary key");
            columnsCuratorTable.add("fio varchar(20)");

            curatorTable.createTable(columnsCuratorTable);

            List<Student> students = new ArrayList<>();
            students.add(new Student(1, "Sasha", 'm', 1));
            students.add(new Student(2, "Vanya", 'm', 1));
            students.add(new Student(3, "Pasha", 'm', 1));
            students.add(new Student(4, "Alexey", 'm', 1));
            students.add(new Student(5, "Taras", 'm', 1));
            students.add(new Student(6, "Andrey", 'm', 2));
            students.add(new Student(7, "Kolya", 'm', 2));
            students.add(new Student(8, "Vasya", 'm', 2));
            students.add(new Student(9, "Masha", 'f', 2));
            students.add(new Student(10, "Katya", 'f', 2));
            students.add(new Student(11, "Ira", 'f', 3));
            students.add(new Student(12, "Galya", 'f', 3));
            students.add(new Student(13, "Petya", 'm', 3));
            students.add(new Student(14, "Misha", 'm', 3));
            students.add(new Student(15, "Gosha", 'm', 3));

            List<Group> groups = new ArrayList<>();
            groups.add(new Group(1, "KlassA", 1));
            groups.add(new Group(2, "KlassB", 2));
            groups.add(new Group(3, "KlassC", 3));

            List<Curator> curators = new ArrayList<>();
            curators.add(new Curator(1, "Kurator1"));
            curators.add(new Curator(2, "Kurator2"));
            curators.add(new Curator(3, "Kurator3"));
            curators.add(new Curator(4, "Kurator4"));

            for (Student student : students) {
                studentTable.insert(student);
            }

            for (Group group : groups) {
                groupTable.insert(group);
            }

            for (Curator curator : curators) {
                curatorTable.insert(curator);
            }

            List<String> studentsInformation = studentTable.doubleJoin("Student.id, Student.fio, sex, StudentGroup.name, Curator.fio",
                    "StudentGroup", "Student.id_group=StudentGroup.id", "Curator", "StudentGroup.id_curator=Curator.id");
            System.out.println("Информация о всех студентах включая название группы и имя куратора:");
            System.out.println(studentsInformation);

            System.out.println("Количество студентов: " + String.join(",", studentTable.select("COUNT(*)")));

            System.out.println("Студентки: " + String.join(",", studentTable.where("fio", "sex='f'")));

            groupTable.updateTable("id_curator=2", "id_curator=3");

            List<String> groupsWhithCurators = groupTable.join("StudentGroup.id, StudentGroup.name, Curator.fio", "Curator", "StudentGroup.id_curator=Curator.id");
            System.out.println("Список групп с их кураторами: ");
            System.out.println(groupsWhithCurators);

            List<String> studentsInGroup = studentTable.whereIn("fio", "Student.id_group", "StudentGroup.id", "StudentGroup", "StudentGroup.name",
                    "'KlassA'");
            System.out.println("Студенты из первой группы: ");
            System.out.println(studentsInGroup);

        } finally {
            dbExecutor.close();
        }

    }
}
