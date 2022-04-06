package domain;

import java.util.Objects;

public class Nota implements HasID<Pair<String, String>> {
    Pair<String, String> idNota;
    private double nota;
    private int saptamanaPredare;
    private String feedback;

    public Nota(Pair<String, String> idNota, double nota, int saptamanaPredare, String feedback) {
        this.idNota = idNota;
        this.nota = nota;
        this.saptamanaPredare = saptamanaPredare;
        this.feedback = feedback;
    }

    @Override
    public Pair<String, String> getID() {
        return idNota;
    }

    @Override
    public void setID(Pair<String, String> idNota) {
        this.idNota = idNota;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getSaptamanaPredare() {
        return saptamanaPredare;
    }

    public void setSaptamanaPredare(int saptamanaPredare) {
        this.saptamanaPredare = saptamanaPredare;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id_student = " + idNota.getObject1() +
                ", id_tema = " + idNota.getObject2() +
                ", nota = " + nota +
                ", saptamanaPredare = " + saptamanaPredare +
                ", feedback = '" + feedback + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota1 = (Nota) o;
        return Double.compare(nota1.nota, nota) == 0 && saptamanaPredare == nota1.saptamanaPredare && Objects.equals(idNota, nota1.idNota) && Objects.equals(feedback, nota1.feedback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNota, nota, saptamanaPredare, feedback);
    }
}
