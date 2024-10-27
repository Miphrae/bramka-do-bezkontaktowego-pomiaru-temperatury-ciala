package bramka_do_temperatury.SpringApplication1;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pomiar {
    private int nr_pomiaru;
    private double temperatura_ciala;
    private double temperatura_otoczenia;
    private String imie;
    private String nazwisko;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime data_pomiaru;
    private double blad_pomiaru;
    private boolean czy_dodane_recznie;

    public Pomiar() {
    }

    public Pomiar(int nr_pomiaru, double temperatura_ciala, double temperatura_otoczenia, String imie, String nazwisko, LocalDateTime data_pomiaru, double blad_pomiaru, boolean czy_dodane_recznie) {
        this.nr_pomiaru = nr_pomiaru;
        this.temperatura_ciala = temperatura_ciala;
        this.temperatura_otoczenia = temperatura_otoczenia;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.data_pomiaru = data_pomiaru;
        this.blad_pomiaru = blad_pomiaru;
        this.czy_dodane_recznie = czy_dodane_recznie;
    }

    public boolean isCzy_dodane_recznie() {
        return czy_dodane_recznie;
    }

    public void setCzy_dodane_recznie(boolean czy_dodane_recznie) {
        this.czy_dodane_recznie = czy_dodane_recznie;
    }

    public int getNr_pomiaru() {
        return nr_pomiaru;
    }

    public double getTemperatura_ciala() {
        return temperatura_ciala;
    }

    public double getTemperatura_otoczenia() {
        return temperatura_otoczenia;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public LocalDateTime getData_pomiaru() {
        return data_pomiaru;
    }

    public double getBlad_pomiaru() {
        return blad_pomiaru;
    }

    public void setNr_pomiaru(int nr_pomiaru) {
        this.nr_pomiaru = nr_pomiaru;
    }

    public void setTemperatura_ciala(double temperatura_ciala) {
        this.temperatura_ciala = temperatura_ciala;
    }

    public void setTemperatura_otoczenia(double temperatura_otoczenia) {
        this.temperatura_otoczenia = temperatura_otoczenia;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setData_pomiaru(LocalDateTime data_pomiaru) {
        this.data_pomiaru = data_pomiaru;
    }

    public void setBlad_pomiaru(double blad_pomiaru) {
        this.blad_pomiaru = blad_pomiaru;
    }

    @Override
    public String toString() {
        return "Pomiar{" +
                "nrPpomiaru=" + nr_pomiaru +
                ", tempCiala=" + temperatura_ciala +
                ", tempOtoczenia=" + temperatura_otoczenia +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", data=" + data_pomiaru +
                ", bladPomiaru=" + blad_pomiaru +
                ", czy_dodane_recznie=" + czy_dodane_recznie +
                '}';
    }
}

