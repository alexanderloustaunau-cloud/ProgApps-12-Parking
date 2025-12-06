package Clases;

import java.util.ArrayList;
import java.util.List;

public class Parking {
	private String nombre;
	private List<Planta> plantas;
	private List<Coche> listaCoches  = new ArrayList<>();
	
	public Parking(String nombre, List<Planta> plantas) {
		super();
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
	public List<Coche> getListaCoches() {
		return listaCoches;
	}
	public void setPlantas(List<Planta> plantas) {
		this.plantas = plantas;
	}
	
	public void addCoche(Coche c) {                       
	    if (c != null && !listaCoches.contains(c)) {
	        listaCoches.add(c);
	    }
	}
	
	public List<Plaza> getPlazasLibresTotales() {
        List<Plaza> libres = new ArrayList<>();
        for (Planta planta : plantas) {
            libres.addAll(planta.getPlazasLibres());
        }
        return libres;
    }
	@Override
	public String toString() {
		return "Parking [nombre=" + nombre + ", plantas=" + plantas + "]";
	}
	
}