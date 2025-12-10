package MAIN;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // =========================================================
    // MÉTODO PRINCIPAL
    // =========================================================
    public static void inicializarDatos(List<Parking> parkings) {
        if (parkings == null || parkings.isEmpty())
            return;

        // 1) Crear coches aleatorios en cada parking
        for (Parking p : parkings) {
            crearCochesBaseEnParking(p);
        }

        // 1.5) Construir un pool global de matrículas existentes
        List<String> poolMatriculas = new ArrayList<>();
        for (Parking p : parkings) {
            for (Coche c : p.getListaCoches()) {
                poolMatriculas.add(c.getMatricula());
            }
        }
        if (poolMatriculas.isEmpty()) return; // por seguridad

        // 2) Crear hasta X reservas repartidas aleatoriamente
        int reservasObjetivo = 2500;
        int reservasCreadas  = 0;

        while (reservasCreadas < reservasObjetivo) {

            // --- Parking aleatorio (Deusto / Leioa / San Mamés) ---
            Parking p = parkings.get(rnd.nextInt(parkings.size()));
            if (p.getPlantas() == null || p.getPlantas().isEmpty()) continue;

            // --- Planta aleatoria ---
            Planta planta = p.getPlantas().get(rnd.nextInt(p.getPlantas().size()));
            if (planta.getPlazas() == null || planta.getPlazas().isEmpty()) continue;

            // --- Plaza aleatoria ---
            Plaza plaza = planta.getPlazas().get(rnd.nextInt(planta.getPlazas().size()));

            // --- Matrícula aleatoria del pool global ---
            String matriculaElegida = poolMatriculas.get(rnd.nextInt(poolMatriculas.size()));

            // --- Buscar o crear coche con esa matrícula EN ESTE parking ---
            Coche coche = buscarOCrearCocheEnParking(p, matriculaElegida);

            // Decidimos si la reserva será futura o pasada
            boolean seraPendiente = (reservasCreadas % 5 == 0); // aprox 1 de cada 5 futura

            LocalDateTime inicio;
            LocalDateTime fin;

            if (seraPendiente) {
                // Reserva futura: empieza entre 1 y 24 horas desde ahora
                inicio = LocalDateTime.now().plusHours(1 + rnd.nextInt(24));
            } else {
                // Reserva pasada: empezó entre 1 y 48 horas antes
                inicio = LocalDateTime.now().minusHours(1 + rnd.nextInt(48));
            }

            // ⏱ Duración entre 1 y 24 horas (horas fijas)
            int durHoras = 1 + rnd.nextInt(24);
            fin = inicio.plusHours(durHoras);

            // ❗ No crear la reserva si se solapa con otra de ESTA MATRÍCULA
            // en CUALQUIER parking
            if (tieneSolapeGlobal(parkings, matriculaElegida, inicio, fin)) {
                continue; // probamos otra combinación
            }

            // Crear la reserva y asociarla al coche
            Reserva reserva = new Reserva(coche, plaza, inicio, fin);
            coche.addReserva(reserva);

            // Si la reserva es futura o está en curso, marcamos la plaza como ocupada
            if (fin.isAfter(LocalDateTime.now())) {
                plaza.ocupar(coche);
            }

            reservasCreadas++;
        }

        // 3) Reservas fijas para 1234ABC en Deusto 1, plazas 33–35
        crearReservasFijas123ABC(parkings);
    }

    // =========================================================
    // COCHES ALEATORIOS
    // =========================================================
    private static String generarMatriculaAleatoria() {
        StringBuilder sb = new StringBuilder();
        // 4 dígitos
        for (int i = 0; i < 4; i++) {
            sb.append(rnd.nextInt(10));
        }
        // 3 letras
        for (int i = 0; i < 3; i++) {
            char letra = (char) ('A' + rnd.nextInt(26));
            sb.append(letra);
        }
        return sb.toString();
    }

    private static void crearCochesBaseEnParking(Parking parking) {
        // Marcas y modelos de ejemplo
        String[] marcas  = { "Hyundai", "Toyota", "BMW", "Audi", "Kia", "Seat", "Ford", "Renault", "Peugeot" };
        String[] modelos = { "i30", "Corolla", "Serie 1", "A3", "Ceed", "Ibiza", "Focus", "Clio", "308" };

        // Creamos 100 coches aleatorios
        for (int i = 0; i < 100; i++) {

            String matricula = generarMatriculaAleatoria();
            String marca     = marcas[rnd.nextInt(marcas.length)];
            String modelo    = modelos[rnd.nextInt(modelos.length)];

            // Color aleatorio del enum
            Color[] colores = Color.values();
            Color color     = colores[rnd.nextInt(colores.length)];

            Coche coche = new Coche(matricula, marca, modelo, color);
            parking.addCoche(coche);
        }
    }

    // Buscar coche por matrícula dentro de UN parking; si no existe, se crea
    private static Coche buscarOCrearCocheEnParking(Parking parking, String matricula) {
        for (Coche c : parking.getListaCoches()) {
            if (c.getMatricula().equalsIgnoreCase(matricula)) {
                return c;
            }
        }
        // Si no existe en este parking, creamos uno "nuevo" con esa matrícula
        // y datos genéricos
        Coche nuevo = new Coche(matricula, "MarcaGen", "ModeloGen", Color.OTROS);
        parking.addCoche(nuevo);
        return nuevo;
    }

    // =========================================================
    // COMPROBACIÓN GLOBAL DE SOLAPES POR MATRÍCULA
    // =========================================================
    private static boolean tieneSolapeGlobal(List<Parking> parkings,
                                             String matricula,
                                             LocalDateTime inicio,
                                             LocalDateTime fin) {
        for (Parking p : parkings) {
            for (Coche c : p.getListaCoches()) {
                if (!c.getMatricula().equalsIgnoreCase(matricula)) continue;

                for (Reserva r : c.getReservas()) {
                    if (rangosSolapan(r.getFechaInicio(), r.getFechaFin(), inicio, fin)) {
                        return true; // ya hay una reserva de esa matrícula en ese intervalo
                    }
                }
            }
        }
        return false;
    }

    private static boolean rangosSolapan(LocalDateTime aIni, LocalDateTime aFin,
                                         LocalDateTime bIni, LocalDateTime bFin) {
        // Solapan si cada uno empieza antes de que termine el otro
        return aIni.isBefore(bFin) && bIni.isBefore(aFin);
    }

    // =========================================================
    // RESERVAS FIJAS PARA 1234ABC
    // =========================================================
    private static void crearReservasFijas123ABC(List<Parking> parkings) {

        // Buscar el parking Deusto
        Parking deusto = null;
        for (Parking p : parkings) {
            if (p.getNombre().equalsIgnoreCase("Deusto")) {
                deusto = p;
                break;
            }
        }
        if (deusto == null) return;

        // Planta 1 (índice 0)
        Planta planta1 = deusto.getPlantas().get(0);

        // Plazas fijas
        Plaza plaza33 = planta1.getPlazas().get(33 - 1);
        Plaza plaza34 = planta1.getPlazas().get(34 - 1);
        Plaza plaza35 = planta1.getPlazas().get(35 - 1);
        Plaza plaza36 = planta1.getPlazas().get(36 - 1);

        // Crear / recuperar coche 1234ABC
        Coche coche = null;
        for (Coche c : deusto.getListaCoches()) {
            if (c.getMatricula().equalsIgnoreCase("1234ABC")) {
                coche = c;
                break;
            }
        }
        if (coche == null) {
            coche = new Coche("1234ABC", "Hyundai", "i30", Color.AZUL);
            deusto.addCoche(coche);
        }

        LocalDateTime ahora = LocalDateTime.now();

        // 1️⃣ Reserva FINALIZADA en plaza 33
        LocalDateTime inicio1 = ahora.minusHours(4);
        LocalDateTime fin1    = inicio1.plusHours(2);
        Reserva r1 = new Reserva(coche, plaza33, inicio1, fin1);
        coche.addReserva(r1);

        // 2️⃣ Reserva FINALIZADA en plaza 34
        LocalDateTime inicio2 = ahora.minusHours(8);
        LocalDateTime fin2    = inicio2.plusHours(3);
        Reserva r2 = new Reserva(coche, plaza34, inicio2, fin2);
        coche.addReserva(r2);
        
        LocalDateTime inicio3 = ahora.minusHours(1);
        LocalDateTime fin3   = inicio3.plusHours(2);
        Reserva r3 = new Reserva(coche, plaza35, inicio3, fin3);
        coche.addReserva(r3);

        // 3️⃣ Reserva PENDIENTE en plaza 35
        LocalDateTime inicio4 = ahora.plusHours(3);
        LocalDateTime fin4    = inicio4.plusHours(2);
        Reserva r4 = new Reserva(coche, plaza36, inicio4, fin4);
        coche.addReserva(r4);
        plaza35.ocupar(coche);
    }
}
