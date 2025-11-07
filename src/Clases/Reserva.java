package Clases;


import java.time.LocalDateTime;

public class Reserva {

    private Coche coche;
    private Plaza plaza;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public Reserva(Coche coche, Plaza plaza, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.coche = coche;
        this.plaza = plaza;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y setters
    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

    public Plaza getPlaza() {
        return plaza;
    }

    public void setPlaza(Plaza plaza) {
        this.plaza = plaza;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "coche=" + coche.getMatricula() +
                ", plaza=" + plaza.getNumero() +
                ", inicio=" + fechaInicio +
                ", fin=" + fechaFin +
                '}';
    }
}
