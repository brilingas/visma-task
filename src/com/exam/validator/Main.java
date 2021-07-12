package com.exam.validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static com.exam.validator.Main.cheaters;
import static com.exam.validator.Neighbour.findNeighbours;
import static com.exam.validator.Student.searchCheaters;
import static com.exam.validator.StudentUtils.*;

public class Main {
    public static Map<Student, Student> cheaters = new HashMap<>();
    private final Map<Integer, String> correctAnswers = new HashMap<>();

    {
        correctAnswers.put(1, "a");
        correctAnswers.put(2, "bd");
        correctAnswers.put(3, "abef");
        correctAnswers.put(4, "f");
        correctAnswers.put(5, "f");
        correctAnswers.put(6, "d");
        correctAnswers.put(7, "abe");
        correctAnswers.put(8, "abcde");
        correctAnswers.put(9, "abe");
        correctAnswers.put(10, "abd");
        correctAnswers.put(11, "b");
        correctAnswers.put(12, "af");
        correctAnswers.put(13, "ce");
        correctAnswers.put(14, "be");
        correctAnswers.put(15, "bdf");
        correctAnswers.put(16, "a");
    }

    public static void main(String[] args) {
        List<Student> students = CSVReader.parseCSV();

        searchCheaters(students);
        applyProbability(cheaters);
        printCheaters();
    }
}

class Student {
    private static final int ANSWERS_THRESHOLD = 5;
    private static final Map<Integer, String> similarAnswers = new HashMap<Integer, String>();
    private String name;
    private String sittingLocation;
    private Map<Integer, String> studentAnswers = new HashMap<Integer, String>();

    public static void searchCheaters(List<Student> students) {
        students.forEach(studentBeingChecked -> {
            List<Student> neighbours = findNeighbours(studentBeingChecked, students);
            neighbours.forEach(neighbour -> {
                compareAnswers(studentBeingChecked, neighbour);
                if (similarAnswers.size() >= ANSWERS_THRESHOLD) {
                    cheaters.put(studentBeingChecked, neighbour);
                }
                similarAnswers.clear();
            });
        });
    }

    public static void compareAnswers(Student studentBeingChecked, Student neighbour) {
        studentBeingChecked.getStudentAnswers().forEach((studentAnswerKey, studentAnswerValue) -> {
            neighbour.getStudentAnswers().forEach((neighbourAnswerKey, neighbourAnswerValue) -> {
                if (studentAnswerKey.equals(neighbourAnswerKey) && studentAnswerValue.equals(neighbourAnswerValue)) {
                    similarAnswers.put(studentAnswerKey, studentAnswerValue);
                }
            });
        });
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public String getSittingLocation() {
        return sittingLocation;
    }

    public Student setSittingLocation(String sittingLocation) {
        this.sittingLocation = sittingLocation;
        return this;
    }

    public Map<Integer, String> getStudentAnswers() {
        return studentAnswers;
    }

    public Student setStudentAnswers(Map<Integer, String> studentAnswers) {
        this.studentAnswers = studentAnswers;
        return this;
    }
}


class Neighbour {
    public static List<Student> findNeighbours(Student studentBeingChecked, List<Student> students) {
        List<Student> neighbours = new ArrayList<>();
        for (Student otherStudent : students) {
            if (areNeighbors(studentBeingChecked, otherStudent)) {
                neighbours.add(otherStudent);
            }
        }
        return neighbours;
    }

    public static boolean areNeighbors(Student studentBeingChecked, Student otherStudent) {
        double studentBeingCheckedLocation = Double.parseDouble(studentBeingChecked.getSittingLocation());
        double otherStudentLocation = Double.parseDouble(otherStudent.getSittingLocation());
        double[] offset = {-0.1, 0.1, -0.9, -1.0, -1.1};
        for (int i = 0; i < offset.length; i++) {
            if (round(studentBeingCheckedLocation - offset[i]) == otherStudentLocation) {
                return true;
            }
        }
        return false;
    }
}

class StudentUtils {
    private static final double CHEATING_PROBABILITY = 0.5;

    public static void applyProbability(Map<Student, Student> cheaters) {
        int newMapSize = (int) (cheaters.size() * CHEATING_PROBABILITY);//Math.round to add 0.5;
        Set randomNumbers = generateRandomNumbers(cheaters.size(), newMapSize);
        Iterator<Map.Entry<Student, Student>> iterator = cheaters.entrySet().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            if (randomNumbers.contains(count)) {
                iterator.remove();
            }
            count++;
        }
    }

    public static Set generateRandomNumbers(int maximumRange, int quantity) {
        if (maximumRange < quantity) {
            throw new IllegalArgumentException("Range shouldnt be less than quantity");
        }
        Random random = new Random();
        Set<Integer> randomNumbers = new HashSet<Integer>();
        while (randomNumbers.size() < quantity) {
            int randomNumber = random.nextInt(maximumRange+1);
            randomNumbers.add(randomNumber);
        }
        return randomNumbers;
    }

    public static double round(double value) {
        return (double) Math.round(value * 100) / 100;
    }

    public static void printCheaters() {
        cheaters.forEach((firstStudent, secondStudent) -> {
            System.out.println(firstStudent.getName() + " cheated from: " + secondStudent.getName());
        });
    }
}

class CSVReader {

    public static List<Student> parseCSV() {

        String csvFile = "results.csv";
        String line = "";
        String splitCSVBy = ",";

        List<Student> students = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {

            while ((line = bufferedReader.readLine()) != null) {

                String[] studentResult = line.split(splitCSVBy);
                Student student = new Student()
                        .setName(studentResult[0])
                        .setSittingLocation(studentResult[1])
                        .setStudentAnswers(parseAnswers(studentResult));

                students.add(student);
            }
            return students;

        } catch (IOException e) {
            throw new RuntimeException("Error while parsing", e);
        }

    }

    private static Map<Integer, String> parseAnswers(String[] studentResult) {
        Map<Integer, String> answers = new HashMap<>();

        for (int i = 2; i < studentResult.length; i++)
            answers.put(i - 1, studentResult[i]);

        return answers;
    }

}
