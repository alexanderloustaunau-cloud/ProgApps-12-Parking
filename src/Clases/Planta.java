package Clases;

import java.util.ArrayList;
import java.util.List;

public class Planta {
	private int numeroPlanta;
	private List<Plaza> plazas;
	public Planta(int numeroPlanta, List<Plaza> plazas) {
		super();
		this.numeroPlanta = numeroPlanta;
		this.plazas = plazas;
	}
	public Planta(int numeroPlanta, int numPlazas) {
        this.numeroPlanta = numeroPlanta;
        this.plazas = new ArrayList<>();
        for (int i = 1; i <= numPlazas; i++) {
            plazas.add(new Plaza(i, false, null));
        }
    }
	public int getNumeroPlanta() {
		return numeroPlanta;
	}
	public void setNumeroPlanta(int numeroPlanta) {
		this.numeroPlanta = numeroPlanta;
	}
	public List<Plaza> getPlazas() {
		return plazas;
	}
	public void setPlazas(List<Plaza> plazas) {
		this.plazas = plazas;
	}
	public List<Plaza> getPlazasLibres(){
		List<Plaza> libres = new ArrayList<>();
        for (Plaza p : plazas) {
            if (!p.isOcupada()) libres.add(p);
        }
        return libres;
	}
	@Override
	public String toString() {
		return "Planta [numeroPlanta=" + numeroPlanta + ", plazas=" + plazas + "]";
	}

}
