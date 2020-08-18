package Validation;

import Domain.Student;
import Exceptions.ValidationException;

public class StudentValidator implements Validator<Student> {
    private String errorMsg;

    /**
     * Validates a Student Object
     * @param st - the homework to be validated
     * @throws ValidationException - if the Student is not valid
     */
    @Override
    public void validate(Student st) throws ValidationException {
        errorMsg = "";
        validateName(st.getFirstName(), st.getLastName());
        validateGroup(st.getGroup());
        validateEmail(st.getEmail());
        validateProf(st.getLabProf());
        if(!errorMsg.isEmpty()) throw new ValidationException("\n" + errorMsg);
    }

    private void validateName(String first, String last) {
        if(last.equals("") || last.charAt(0) < 'A' || last.charAt(0) > 'Z')
            errorMsg += "Numele trebuie sa inceapa cu litera mare! ";
        if(first.equals("") || first.charAt(0) < 'A' || first.charAt(0) > 'Z')
            errorMsg += "Prenumele trebuie sa inceapa cu litera mare! ";
    }

    private void validateGroup(Integer gr) {
        if(gr < 100 || gr > 999)
            errorMsg += "Grupa trebuie sa fie cuprinsa intre 100 si 999! ";
    }

    private void validateEmail(String email) {
        if(!email.matches("^[\\w-\\.]+\\@[\\w-\\.]+\\.[a-zA-Z]{2,4}$"))
            errorMsg += "Emailul trebuie sa fie de tipul 'a...a@a...a.aaaa'. Se pot folosi litere, cifre, ., _ si -. ";
    }

    private void validateProf(String name) {
        if(name.equals("") || name.charAt(0) < 'A' || name.charAt(0) > 'Z')
            errorMsg += "Numele indrumatorului de laborator trebuie sa inceapa cu litera mare!";
    }
}
