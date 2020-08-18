package Validation;

import Domain.Homework;
import Exceptions.ValidationException;

public class HomeworkValidator implements Validator<Homework> {
    private String errorMsg;

    /**
     * Validates a Homework Object
     * @param hw - the homework to be validated
     * @throws ValidationException - if the Homework is not valid
     */
    @Override
    public void validate(Homework hw) throws ValidationException {
        errorMsg = "";
        if(hw.getDescription().equals(""))
            errorMsg += "Descrierea nu poate fi nula! ";
        if(hw.getStartWeek() < 1 || hw.getStartWeek() > 14 || hw.getDeadlineWeek() < 1 || hw.getDeadlineWeek() > 14)
            errorMsg += "Saptamana de start si de deadline a temei trebuie sa fie cuprinsa intre 1 si 14! ";
        if(hw.getStartWeek() >= hw.getDeadlineWeek())
            errorMsg += "Saptamana de deadline trebuie sa fie dupa saptamana de start a temei!";
        if(!errorMsg.isEmpty()) throw new ValidationException("\n" + errorMsg);
    }
}
