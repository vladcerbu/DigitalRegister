package Utilities.Calendar;

import Exceptions.InvalidDateException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * UniversityYear - Singleton
 * The university year contains the 2 semesters and initializez them from the specified files
 */
public class UniversityYear {
    private String yearName;
    private String fileName1 = "E:\\FMI Sem.3\\MAP\\Projects\\ProiectMare\\src\\main\\resources\\Files\\Sem1.txt";
    private String fileName2 = "E:\\FMI Sem.3\\MAP\\Projects\\ProiectMare\\src\\main\\resources\\Files\\Sem2.txt";
    private Semester sem1;
    private Semester sem2;
    private static UniversityYear instance = new UniversityYear();

    private UniversityYear() {
        this.sem1 = new Semester(1);
        this.sem2 = new Semester(2);
        load();
    }

    public static UniversityYear getInstance() { return instance; }

    private void load() {
        try {
            String line1;
            String line2;
            String[] dateString1;
            String[] dateString2;
            BufferedReader br1 = new BufferedReader(new FileReader(fileName1));
            BufferedReader br2 = new BufferedReader(new FileReader(fileName2));
            this.yearName = br1.readLine();
            this.yearName = br2.readLine();
            for (int i = 1; i <= 4; ++i) {
                line1 = br1.readLine();
                line2 = br2.readLine();
                dateString1 = line1.split("=");
                dateString2 = line2.split("=");
                LocalDate date1 = LocalDate.parse(dateString1[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate date2 = LocalDate.parse(dateString2[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                switch (i) {
                    case 1: sem1.setStartSemester(date1); sem2.setStartSemester(date2); break;
                    case 2: sem1.setStartHolyday(date1); sem2.setStartHolyday(date2); break;
                    case 3: sem1.setEndHolyday(date1); sem2.setEndHolyday(date2); break;
                    case 4: sem1.setEndSemester(date1); sem2.setEndSemester(date2); break;
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
            System.out.println("\nEroare la citire!");
        }
    }

    public String getYearName() {
        return yearName;
    }

    public Semester getCurrentSemester(LocalDate date) {
        /*if(date.compareTo(sem1.getStartSemester()) < 0 || (date.compareTo(sem1.getEndSemester()) > 0 && date.compareTo(sem2.getStartSemester()) < 0) || date.compareTo(sem2.getEndSemester()) > 0)
            throw new InvalidDateException("\nNe aflam in perioada intersemestriala! ");*/
        if(date.compareTo(sem1.getStartSemester()) >= 0 && date.compareTo(sem1.getEndSemester()) <= 0)
            return sem1;
        else
            return sem2;
    }
}