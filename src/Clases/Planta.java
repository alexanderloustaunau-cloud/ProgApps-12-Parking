package Clases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Planta {

    private int numeroPlanta;
    private List<Plaza> plazas;

    public Planta(int numeroPlanta, int numPlazas) {
        this.numeroPlanta = numeroPlanta;
        this.plazas = new ArrayList<>();

        for (int i = 1; i <= numPlazas; i++) {
            plazas.add(new Plaza(i, false, null));
        }
    }

    public int getNumeroPlanta() { return numeroPlanta; }
    public List<Plaza> getPlazas() { return plazas; }

	public Collection<? extends Plaza> getPlazasLibres() {
		// TODO Auto-generated method stub
		List<Plaza> libres = new ArrayList<>();
		for (Plaza plaza : plazas) {
			if (!plaza.isOcupada()) {
				libres.add(plaza);
			}
		}
		return libres;
	}
}
