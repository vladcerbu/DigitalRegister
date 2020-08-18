package Domain;

public class HelpfulDTO {
    private int idSt;
    private String numeSt;
    private int grSt;
    private double medie;
    private int labNr;

    public HelpfulDTO(int idSt, String numeSt, int grSt, double medie) {
        this.idSt = idSt;
        this.numeSt = numeSt;
        this.grSt = grSt;
        this.medie = medie;
    }

    public HelpfulDTO(int labNr, double medie) {
        this.labNr = labNr;
        this.medie = medie;
    }

    public int getLabNr() { return labNr; }

    public void setLabNr(int labNr) { this.labNr = labNr; }

    public int getIdSt() {
        return idSt;
    }

    public void setIdSt(int idSt) {
        this.idSt = idSt;
    }

    public String getNumeSt() {
        return numeSt;
    }

    public void setNumeSt(String numeSt) {
        this.numeSt = numeSt;
    }

    public int getGrSt() {
        return grSt;
    }

    public void setGrSt(int grSt) {
        this.grSt = grSt;
    }

    public double getMedie() {
        return medie;
    }

    public void setMedie(double medie) {
        this.medie = medie;
    }
}
