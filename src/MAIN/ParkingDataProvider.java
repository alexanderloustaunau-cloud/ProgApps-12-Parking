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
        Parking p1 = new Parking("Deusto", 3, 100);
        Parking p2 = new Parking("Leioa", 2, 100);
        Parking p3 = new Parking("San Mamés", 5, 100);
        p1.setImagen1("/Deusto.jpg");
        p1.setImagen2("UDeusto mapa.png");
        p2.setImagen1("/Leioa.jpg");
        p2.setImagen2("/LeioaMap.png");
        p3.setImagen1("/San Mames.jpg");
        p3.setImagen2("/San Mames Map.png");;
        
        
        lista.add(p1);
        lista.add(p2);
        lista.add(p3);
        return lista;
    }
}