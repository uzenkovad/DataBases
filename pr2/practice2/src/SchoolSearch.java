
import java.io.*;
import java.util.*;

class Student {
    String lastName;
    String firstName;
    int grade;
    int classroom;
    int bus;

    public Student(String lastName, String firstName, int grade, int classroom, int bus) {
        this.lastName = lastName.toUpperCase();
        this.firstName = firstName.toUpperCase();
        this.grade = grade;
        this.classroom = classroom;
        this.bus = bus;
    }

    public String getBusInfo() {
        return firstName + " " + lastName + " (Bus Route: " + bus + ")";
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (Classroom: " + classroom + ")";
    }
}

class Teacher {
    String lastName;
    String firstName;
    int classroom;

    public Teacher(String lastName, String firstName, int classroom) {
        this.lastName = lastName.toUpperCase();
        this.firstName = firstName.toUpperCase();
        this.classroom = classroom;
    }
}

public class SchoolSearch {
    public static Map<Integer, Teacher> readTeachersFromFile(String filename) throws IOException {
        Map<Integer, Teacher> teachers = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length != 3) {
                System.out.println("Incorrect data format in line: " + line);
                continue;
            }
            String lastName = data[0].trim();
            String firstName = data[1].trim();
            int classroom = Integer.parseInt(data[2].trim());
            teachers.put(classroom, new Teacher(lastName, firstName, classroom));
        }
        br.close();
        return teachers;
    }

    public static List<Student> readStudentsFromFile(String filename) throws IOException {
        List<Student> students = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length != 5) {
                System.out.println("Incorrect data format in line: " + line);
                continue;
            }
            String lastName = data[0].trim();
            String firstName = data[1].trim();
            int grade = Integer.parseInt(data[2].trim());
            int classroom = Integer.parseInt(data[3].trim());
            int bus = Integer.parseInt(data[4].trim());
            students.add(new Student(lastName, firstName, grade, classroom, bus));
        }
        br.close();
        return students;
    }

    public static void findByLastNameInteractive(List<Student> students, Map<Integer, Teacher> teachers, String lastName, Scanner scanner) {
        System.out.print("Would you like to find by bus route? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            System.out.print("Enter bus route number: ");
            try {
                int busRoute = Integer.parseInt(scanner.nextLine().trim());
                findByLastNameAndBus(students, teachers, lastName, busRoute);
            } catch (NumberFormatException e) {
                System.out.println("Invalid bus route number format.");
            }
        } else if (response.equals("no")) {
            findByLastName(students, teachers, lastName);
        } else {
            System.out.println("Invalid response. Please enter 'yes' or 'no'.");
        }
    }

    public static void findByLastName(List<Student> students, Map<Integer, Teacher> teachers, String lastName) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Student s : students) {
            if (s.lastName.equalsIgnoreCase(lastName) && uniqueStudents.add(s)) {
                Teacher teacher = teachers.get(s.classroom);
                if (teacher != null) {
                    System.out.println(s.firstName + " " + s.lastName + " (Classroom: " + s.classroom + ", Teacher: " + teacher.firstName + " " + teacher.lastName + ")");
                } else {
                    System.out.println(s.firstName + " " + s.lastName + " (Classroom: " + s.classroom + ", Teacher: Not found)");
                }
                found = true;
            }
        }
        if (!found) {
            System.out.println("Student with last name " + lastName + " not found.");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findByLastNameAndBus(List<Student> students, Map<Integer, Teacher> teachers, String lastName, int bus) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Student s : students) {
            if (s.lastName.equalsIgnoreCase(lastName) && s.bus == bus && uniqueStudents.add(s)) {
                Teacher teacher = teachers.get(s.classroom);
                if (teacher != null) {
                    System.out.println(s.getBusInfo() + " (Classroom: " + s.classroom + ", Teacher: " + teacher.firstName + " " + teacher.lastName + ")");
                } else {
                    System.out.println(s.getBusInfo() + " (Classroom: " + s.classroom + ", Teacher: Not found)");
                }
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students with last name " + lastName + " found on bus route " + bus + ".");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findByGrade(List<Student> students, int grade) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Student s : students) {
            if (s.grade == grade && uniqueStudents.add(s)) {
                System.out.println(s.firstName + " " + s.lastName + " (Bus Route: " + s.bus + ")");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students found in grade " + grade + ".");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findByTeacher(List<Student> students, Map<Integer, Teacher> teachers, String lastName) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Map.Entry<Integer, Teacher> entry : teachers.entrySet()) {
            Teacher teacher = entry.getValue();
            if (teacher.lastName.equalsIgnoreCase(lastName)) {
                for (Student s : students) {
                    if (s.classroom == teacher.classroom && uniqueStudents.add(s)) {
                        System.out.println(s.firstName + " " + s.lastName + " (Classroom: " + s.classroom + ")");
                    }
                }
                found = true;
            }
        }
        if (!found) {
            System.out.println("Teacher with last name " + lastName + " not found.");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findBusRoute(List<Student> students, String lastName, String firstName) {
        long startTime = System.nanoTime();
        for (Student s : students) {
            if (s.lastName.equalsIgnoreCase(lastName) && s.firstName.equalsIgnoreCase(firstName)) {
                System.out.println(s.firstName + " " + s.lastName + " (Bus Route: " + s.bus + ")");
                long endTime = System.nanoTime();
                printSearchTime(startTime, endTime);
                return;
            }
        }
        System.out.println("Student " + firstName + " " + lastName + " not found.");
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findClassroom(List<Student> students, String lastName, String firstName) {
        long startTime = System.nanoTime();
        for (Student s : students) {
            if (s.lastName.equalsIgnoreCase(lastName) && s.firstName.equalsIgnoreCase(firstName)) {
                System.out.println(s.firstName + " " + s.lastName + " (Classroom: " + s.classroom + ")");
                long endTime = System.nanoTime();
                printSearchTime(startTime, endTime);
                return;
            }
        }
        System.out.println("Student " + firstName + " " + lastName + " not found.");
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void printSearchTime(long startTime, long endTime) {
        long duration = endTime - startTime;
        double seconds = duration / 1_000_000_000.0;
        System.out.printf("\nSearch time: %.6f seconds%n", seconds);
    }

    // Метод для додаткового завдання
    public static void listStudentsByClassroom(List<Student> students, int classroom) {
        long startTime = System.nanoTime();
        System.out.println("Students in classroom " + classroom + ":");
        boolean found = false;
        for (Student s : students) {
            if (s.classroom == classroom) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students found in classroom " + classroom + ".");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    // Метод для додаткового завдання
    public static void findTeachersByClassroom(Map<Integer, Teacher> teachers, int classroom) {
        long startTime = System.nanoTime();
        boolean found = false;
        for (Teacher teacher : teachers.values()) {
            if (teacher.classroom == classroom) {
                System.out.println(teacher.firstName + " " + teacher.lastName + " (Classroom: " + classroom + ")");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No teachers found for classroom " + classroom + ".");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    // Метод для додаткового завдання
    public static void findTeachersByGrade(List<Student> students, Map<Integer, Teacher> teachers, int grade) {
        long startTime = System.nanoTime();
        Set<Integer> classroomsWithStudents = new HashSet<>();

        for (Student s : students) {
            if (s.grade == grade) {
                classroomsWithStudents.add(s.classroom);
            }
        }

        boolean found = false;
        for (Integer classroom : classroomsWithStudents) {
            for (Teacher teacher : teachers.values()) {
                if (teacher.classroom == classroom) {
                    System.out.println(teacher.firstName + " " + teacher.lastName + " (Classroom: " + classroom + ")");
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No teachers found for grade " + grade + ".");
        }

        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Map<Integer, Teacher> teachers = readTeachersFromFile("teachers.txt");
            List<Student> students = readStudentsFromFile("list.txt");

            while (true) {
                System.out.println("\nAvailable commands:");
                System.out.println("S (Student) <lastname> - Search for a student by last name.");
                System.out.println("T (Teacher) <lastname> - Search for students by teacher's last name.");
                System.out.println("B (Bus) <lastname> <firstname> - Search for bus route by student's name.");
                System.out.println("G (Grade) <number> - Search for students by grade.");
                System.out.println("C (Classroom) <lastname> <firstname> - Find the classroom of a student.");
                System.out.println("L (List) <classroom> - List all students in a classroom.");
                System.out.println("CT (Classroom Teacher) <classroom> - Find teachers by classroom.");
                System.out.println("GT (Grade Teacher) <number> - Find teachers by grade.");
                System.out.println("Q - Quit.");
                System.out.print("Enter command: ");

                String input = scanner.nextLine().trim();
                String[] parts = input.split(" ");

                if (parts.length == 0) {
                    System.out.println("Invalid command.");
                    continue;
                }

                String command = parts[0].toUpperCase();
                switch (command) {
                    case "S":
                        if (parts.length == 2) {
                            findByLastNameInteractive(students, teachers, parts[1], scanner);
                        } else {
                            System.out.println("Usage: S <lastname>");
                        }
                        break;
                    case "T":
                        if (parts.length == 2) {
                            findByTeacher(students, teachers, parts[1]);
                        } else {
                            System.out.println("Usage: T <lastname>");
                        }
                        break;
                    case "B":
                        if (parts.length == 3) {
                            findBusRoute(students, parts[1], parts[2]);
                        } else {
                            System.out.println("Usage: B <lastname> <firstname>");
                        }
                        break;
                    case "G":
                        if (parts.length == 2) {
                            try {
                                int grade = Integer.parseInt(parts[1]);
                                findByGrade(students, grade);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid grade format.");
                            }
                        } else {
                            System.out.println("Usage: G <number>");
                        }
                        break;
                    case "C":
                        if (parts.length == 3) {
                            findClassroom(students, parts[1], parts[2]);
                        } else {
                            System.out.println("Usage: C <lastname> <firstname>");
                        }
                        break;
                    case "L":
                        if (parts.length == 2) {
                            try {
                                int classroom = Integer.parseInt(parts[1]);
                                listStudentsByClassroom(students, classroom);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid classroom format.");
                            }
                        } else {
                            System.out.println("Usage: L <classroom>");
                        }
                        break;
                    case "CT": //  Пошук вчителів за номером класу
                        if (parts.length == 2) {
                            try {
                                int classroom = Integer.parseInt(parts[1]);
                                findTeachersByClassroom(teachers, classroom);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid classroom format.");
                            }
                        } else {
                            System.out.println("Usage: CT <classroom>");
                        }
                        break;
                    case "GT": // Пошук вчителів за рівнем класу
                        if (parts.length == 2) {
                            try {
                                int grade = Integer.parseInt(parts[1]);
                                findTeachersByGrade(students, teachers, grade);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid grade format.");
                            }
                        } else {
                            System.out.println("Usage: GT <number>");
                        }
                        break;
                    case "Q":
                        System.out.println("Good bye!");
                        return;
                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
