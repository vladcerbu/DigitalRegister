package Utilities.Calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Semester {
    private int semNr;
    private LocalDate startSemester;
    private LocalDate startHolyday;
    private LocalDate endHolyday;
    private LocalDate endSemester;

    public Semester(int semNr) { this.semNr = semNr; }

    public int getCurrentWeek(LocalDate date) {
        if(date.compareTo(startHolyday) >= 0 && date.compareTo(endHolyday) <= 0)
            date = startHolyday.minusDays(1);
        LocalDate week = startSemester;
        int countWeeks = 0;
        while(date.compareTo(week) >= 0 && countWeeks < 14) {
            week = week.plusDays(7);
            if(date.compareTo(startHolyday) >= 0 && date.compareTo(endHolyday) <= 0)
                --countWeeks;
            ++countWeeks;
        }
        return countWeeks;
    }

    public LocalDate getCurrentDate(int week) {
        LocalDate rez = startSemester;
        DayOfWeek dow = LocalDate.now().getDayOfWeek();
        switch(dow) {
            case TUESDAY: rez = rez.plusDays(1); break;
            case WEDNESDAY: rez = rez.plusDays(2); break;
            case THURSDAY: rez = rez.plusDays(3); break;
            case FRIDAY: rez = rez.plusDays(4); break;
            case SATURDAY: rez = rez.plusDays(5); break;
            case SUNDAY: rez = rez.plusDays(6); break;
            default: break;
        }
        for(int i = 1; i < week; ++i) {
            rez = rez.plusDays(7);
            while(rez.compareTo(startHolyday) >= 0 && rez.compareTo(endHolyday) <= 0)
                rez = rez.plusDays(7);
        }
        return rez;
    }

    public int getSemNr() {
        return semNr;
    }

    public void setSemNr(int semNr) {
        this.semNr = semNr;
    }

    public LocalDate getStartSemester() {
        return startSemester;
    }

    public void setStartSemester(LocalDate startSemester) {
        this.startSemester = startSemester;
    }

    public LocalDate getStartHolyday() {
        return startHolyday;
    }

    public void setStartHolyday(LocalDate startHolyday) {
        this.startHolyday = startHolyday;
    }

    public LocalDate getEndHolyday() {
        return endHolyday;
    }

    public void setEndHolyday(LocalDate endHolyday) {
        this.endHolyday = endHolyday;
    }

    public LocalDate getEndSemester() {
        return endSemester;
    }

    public void setEndSemester(LocalDate endSemester) {
        this.endSemester = endSemester;
    }
}
