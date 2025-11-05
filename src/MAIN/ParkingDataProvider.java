package MAIN;

import java.util.ArrayList;
import java.util.List;

import Clases.Parking;

public final class ParkingDataProvider {

    private ParkingDataProvider() {}

    // Devuelve exactamente los 3 parkings del proyecto
    public static List<Parking> getParkings() {
        List<Parking> lista = new ArrayList<Parking>();
        // Cada planta con 100 plazas (10x10) para encajar con tu panel actual
        lista.add(new Parking("Deusto", 3, 100));
        lista.add(new Parking("Leioa", 2, 100));
        lista.add(new Parking("San Mamés", 5, 100));
        return lista;
    }
}