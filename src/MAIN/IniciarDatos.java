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
import Clases.Color;  
import MAIN.GestorDB;

public class IniciarDatos {

    private static final Random rnd = new Random();


 
    public static void inicializarDatos(List<Parking> parkings,GestorDB gestorDB) {
        if (parkings == null || parkings.isEmpty())
            return;

        //  Crear coches aleatorios en cada parking
        for (Parking p : parkings) {
            crearCochesBaseEnParking(p,gestorDB);
        }

        
        List<String> poolMatriculas = new ArrayList<>();
        for (Parking p : parkings) {
            for (Coche c : p.getListaCoches()) {
                poolMatriculas.add(c.getMatricula());
            }
        }
        if (poolMatriculas.isEmpty()) return; 

        // 2) Crear hasta X reservas 
        
        int reservasObjetivo = 1000; //X
        int reservasCreadas  = 0;

        while (reservasCreadas < reservasObjetivo) {

           
            Parking p = parkings.get(rnd.nextInt(parkings.size()));
            if (p.getPlantas() == null || p.getPlantas().isEmpty()) continue;

            
            Planta planta = p.getPlantas().get(rnd.nextInt(p.getPlantas().size()));
            if (planta.getPlazas() == null || planta.getPlazas().isEmpty()) continue;

           
            Plaza plaza = planta.getPlazas().get(rnd.nextInt(planta.getPlazas().size()));

           
            String matriculaElegida = poolMatriculas.get(rnd.nextInt(poolMatriculas.size()));

           
            Coche coche = buscarOCrearCocheEnParking(p, matriculaElegida);

           
            boolean seraPendiente = (reservasCreadas % 5 == 0); 

            LocalDateTime inicio;
            LocalDateTime fin;

            if (seraPendiente) {
                // Reserva futura: empieza entre 1 y 24 horas desde ahora
                inicio = LocalDateTime.now().plusHours(1 + rnd.nextInt(24));
            } else {
                // Reserva pasada: empezó entre 1 y 48 horas antes
                inicio = LocalDateTime.now().minusHours(1 + rnd.nextInt(48));
            }

            //  Duración entre 1 y 24 horas (horas fijas)
            int durHoras = 1 + rnd.nextInt(24);
            fin = inicio.plusHours(durHoras);

        
            if (tieneSolapeGlobal(parkings, matriculaElegida, inicio, fin)) {
                continue; 
            }

            // Crear la reserva
            Reserva reserva = new Reserva(coche, plaza, inicio, fin);
            coche.addReserva(reserva);

            LocalDateTime ahora = LocalDateTime.now();
            if (fin.isAfter(ahora)) {
                plaza.ocupar(coche);
            }

            // Calcular estado para la BD
            String estado;
            if (fin.isBefore(ahora)) {
                estado = "FINALIZADA";
            } else if ((inicio.isBefore(ahora) || inicio.isEqual(ahora)) && fin.isAfter(ahora)) {
                estado = "EN_CURSO";
            } else {
                estado = "PENDIENTE";
            }

            
            int plazaId = plaza.getIdBD(); 

           
            gestorDB.insertarReserva(reserva, plazaId, estado);

            reservasCreadas++;
        }

        // 3) Reservas fijas para 1234ABC en Deusto 1, plazas 33–35
        crearReservasFijas123ABC(parkings,gestorDB);
    }

    
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

    private static void crearCochesBaseEnParking(Parking parking, GestorDB gestorDB) {
        // Marcas y modelos de ejemplo
        String[] marcas  = { "Hyundai", "Toyota", "BMW", "Audi", "Kia", "Seat", "Ford", "Renault", "Peugeot" };
        String[] modelos = { "i30", "Corolla", "Serie 1", "A3", "Ceed", "Ibiza", "Focus", "Clio", "308" };

        // Creamos 100 coches aleatorios
        for (int i = 0; i < 100; i++) {

            String matricula = generarMatriculaAleatoria();
            String marca     = marcas[rnd.nextInt(marcas.length)];
            String modelo    = modelos[rnd.nextInt(modelos.length)];

            
            Color[] colores = Color.values();
            Color color     = colores[rnd.nextInt(colores.length)];

            Coche coche = new Coche(matricula, marca, modelo, color);
            parking.addCoche(coche);
            
            gestorDB.guardarCoche(coche);
        }
    }

    
    private static Coche buscarOCrearCocheEnParking(Parking parking, String matricula) {
        for (Coche c : parking.getListaCoches()) {
            if (c.getMatricula().equalsIgnoreCase(matricula)) {
                return c;
            }
        }
        
        Coche nuevo = new Coche(matricula, "MarcaGen", "ModeloGen", Color.OTROS);
        parking.addCoche(nuevo);
        return nuevo;
    }

    
    private static boolean tieneSolapeGlobal(List<Parking> parkings,
                                             String matricula,
                                             LocalDateTime inicio,
                                             LocalDateTime fin) {
        for (Parking p : parkings) {
            for (Coche c : p.getListaCoches()) {
                if (!c.getMatricula().equalsIgnoreCase(matricula)) continue;

                for (Reserva r : c.getReservas()) {
                    if (rangosSolapan(r.getFechaInicio(), r.getFechaFin(), inicio, fin)) {
                        return true; 
                    }
                }
            }
        }
        return false;
    }

    private static boolean rangosSolapan(LocalDateTime aIni, LocalDateTime aFin,
                                         LocalDateTime bIni, LocalDateTime bFin) {
        
        return aIni.isBefore(bFin) && bIni.isBefore(aFin);
    }

   
    // RESERVAS FIJAS PARA 1234ABC
  
    private static void crearReservasFijas123ABC(List<Parking> parkings, GestorDB gestorDB) {

       
        Parking deusto = null;
        for (Parking p : parkings) {
            if (p.getNombre().equalsIgnoreCase("Deusto")) {
                deusto = p;
                break;
            }
        }
        if (deusto == null) return;

       
        Planta planta1 = deusto.getPlantas().get(0);

        
        Plaza plaza33 = planta1.getPlazas().get(33 - 1);
        Plaza plaza34 = planta1.getPlazas().get(34 - 1);
        Plaza plaza35 = planta1.getPlazas().get(35 - 1);
        Plaza plaza36 = planta1.getPlazas().get(36 - 1);

        
      
       
          Coche  coche = new Coche("1234ABC", "Hyundai", "i30", Color.AZUL);
            deusto.addCoche(coche);
        

        LocalDateTime ahora = LocalDateTime.now();

     
        LocalDateTime inicio1 = ahora.minusHours(4);
        LocalDateTime fin1    = inicio1.plusHours(2);
        Reserva r1 = new Reserva(coche, plaza33, inicio1, fin1);
        coche.addReserva(r1);

       
        LocalDateTime inicio2 = ahora.minusHours(8);
        LocalDateTime fin2    = inicio2.plusHours(3);
        Reserva r2 = new Reserva(coche, plaza34, inicio2, fin2);
        coche.addReserva(r2);
        
        LocalDateTime inicio3 = ahora.minusHours(1);
        LocalDateTime fin3   = inicio3.plusHours(2);
        Reserva r3 = new Reserva(coche, plaza35, inicio3, fin3);
        coche.addReserva(r3);

      
        LocalDateTime inicio4 = ahora.plusHours(3);
        LocalDateTime fin4    = inicio4.plusHours(2);
        Reserva r4 = new Reserva(coche, plaza36, inicio4, fin4);
        coche.addReserva(r4);
        plaza35.ocupar(coche);
        
        gestorDB.guardarCoche(coche); 
        
        int id33 = plaza33.getIdBD();
        int id34 = plaza34.getIdBD();
        int id35 = plaza35.getIdBD();
        int id36 = plaza36.getIdBD();
        
        gestorDB.insertarReserva(r1, id33, "FINALIZADA");
        gestorDB.insertarReserva(r2, id34, "FINALIZADA");
        gestorDB.insertarReserva(r3, id35, "EN_CURSO");
        gestorDB.insertarReserva(r4, id36, "PENDIENTE");
    }
}
