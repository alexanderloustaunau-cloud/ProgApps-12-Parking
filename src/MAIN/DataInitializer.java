package MAIN;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import Clases.Parking;
import Clases.Planta;
import Clases.Plaza;
import Clases.Coche;
import Clases.Reserva;
import Clases.Color;   // tu enum de colores

public class DataInitializer {

    private static final Random rnd = new Random();

    private DataInitializer() {
        // Clase de utilidades, no se instancia
    }

    /**
     * Inicializa datos de ejemplo en los parkings:
     * - Crea 5 coches por parking
     * - Crea 100 reservas repartidas por todas las plazas de todos los parkings
     *   (mezcla de reservas pasadas "Finalizada" y futuras "Pendiente").
     */
    public static void inicializarDatos(List<Parking> parkings) {
        if (parkings == null || parkings.isEmpty()) return;

        // 1) Crear 5 coches por cada parking
        for (Parking p : parkings) {
            crearCochesBaseEnParking(p);
        }

        // 2) Crear hasta 100 reservas repartidas entre todos los parkings / plantas / plazas
        int reservasObjetivo = 100;
        int reservasCreadas  = 0;

        for (Parking p : parkings) {
            for (Planta planta : p.getPlantas()) {
                for (Plaza plaza : planta.getPlazas()) {

                    if (reservasCreadas >= reservasObjetivo) {
                        return; // hemos llegado a 100
                    }

                    // Escogemos un coche aleatorio de este parking
                    if (p.getListaCoches().isEmpty()) continue;
                    Coche coche = p.getListaCoches().get(rnd.nextInt(p.getListaCoches().size()));

                    // Decidimos si esta reserva será pasada (Finalizada) o futura (Pendiente)
                    boolean seraPendiente = (reservasCreadas % 5 == 0); 
                    // aprox 1 de cada 5 será pendiente

                    LocalDateTime inicio;
                    LocalDateTime fin;

                    if (seraPendiente) {
                        // Reserva futura: empieza entre 1 y 24 horas desde ahora
                        inicio = LocalDateTime.now().plusHours(1 + rnd.nextInt(24));
                    } else {
                        // Reserva pasada: empezó entre 1 y 48 horas antes
                        inicio = LocalDateTime.now().minusHours(1 + rnd.nextInt(48));
                    }

                    // Duración entre 1 y 6 horas
                    fin = inicio.plusHours(1 + rnd.nextInt(6));

                    Reserva reserva = new Reserva(coche, plaza, inicio, fin);
                    coche.addReserva(reserva);

                    // Si la reserva es futura o está en curso, podemos marcar la plaza como ocupada
                    if (fin.isAfter(LocalDateTime.now())) {
                        plaza.ocupar(coche);
                    }

                    reservasCreadas++;
                    if (reservasCreadas >= reservasObjetivo) return;
                }
            }
        }
    }

    // ---------------------------------------------------------------------
    // Crear 5 coches "base" en cada parking
    // ---------------------------------------------------------------------
    private static void crearCochesBaseEnParking(Parking parking) {
        // Podrías hacerlos aleatorios, pero con 5 fijos te es más fácil probar en el historial
        Coche c1 = new Coche("1234ABC", "Hyundai", "i30",   Color.AZUL);
        Coche c2 = new Coche("5678DEF", "Toyota",  "Corolla", Color.ROJO);
        Coche c3 = new Coche("9012GHI", "BMW",     "Serie 1", Color.NEGRO);
        Coche c4 = new Coche("3456JKL", "Audi",    "A3",      Color.GRIS);
        Coche c5 = new Coche("7890MNO", "Seat",    "Ibiza",   Color.BLANCO);

        parking.addCoche(c1);
        parking.addCoche(c2);
        parking.addCoche(c3);
        parking.addCoche(c4);
        parking.addCoche(c5);
    }
}
