package Clases;

import java.util.ArrayList;
import java.util.List;

public class Parking {

    private String nombre;
    private List<Planta> plantas;
    private List<Coche> listaCoches = new ArrayList<>();
    private String Imagen1;
    private String Imagen2;

    public Parking(String nombre, List<Planta> plantas) {
        this.nombre = nombre;
        this.plantas = plantas;
    }

    public Parking(String nombre, int numPlantas, int plazasPorPlanta) {
        this.nombre = nombre;
        this.plantas = new ArrayList<>();

        for (int i = 1; i <= numPlantas; i++) {
            plantas.add(new Planta(i, plazasPorPlanta));
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Planta> getPlantas() {
        return plantas;
    }

    public void setPlantas(List<Planta> plantas) {
        this.plantas = plantas;
    }

    public List<Coche> getListaCoches() {
        return listaCoches;
    }

    public void addCoche(Coche c) {
        if (c != null && !listaCoches.contains(c)) {
            listaCoches.add(c);
        }
    }

    public String getImagen1() {
        return Imagen1;
    }

    public void setImagen1(String imagen1) {
        this.Imagen1 = imagen1;
    }

    public String getImagen2() {
        return Imagen2;
    }

    public void setImagen2(String imagen2) {
        this.Imagen2 = imagen2;
    }

    public List<Plaza> getPlazasLibresTotales() {
        List<Plaza> libres = new ArrayList<>();
        for (Planta p : plantas) {
            libres.addAll(p.getPlazasLibres());
        }
        return libres;
    }

    @Override
    public String toString() {
        return "Parking [nombre=" + nombre + ", plantas=" + plantas + "]";
    }
}
