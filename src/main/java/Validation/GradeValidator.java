package Validation;

import Domain.Grade;
import Exceptions.ValidationException;

public class GradeValidator implements Validator<Grade> {
    private String errorMsg;

    /**
     * Validates a Grade Object
     * @param gr - the grade to be validated
     * @throws ValidationException - if the Grade is not valid
     */
    @Override
    public void validate(Grade gr) throws ValidationException {
        errorMsg = "";
        if(gr.getProf().equals("") || gr.getProf().charAt(0) < 'A' || gr.getProf().charAt(0) > 'Z')
            errorMsg += "Numele profesorului trebuie sa inceapa cu litera mare! ";
        if(gr.getValue() < 1 || gr.getValue() > 10)
            errorMsg += "Nota trebuie sa fie o valoare cuprinsa intre 1 si 10!";
        if(!errorMsg.isEmpty()) throw new ValidationException("\n" + errorMsg);
    }
}
