package MAIN;

import java.time.LocalDateTime;

import javax.swing.SwingUtilities;

import Clases.Parking;
import Clases.Planta;
import Clases.Plaza;
import Clases.Coche;
import Clases.Reserva;

/**
 * Hilo que actualiza automáticamente el estado REAL de las plazas:
 * - Activa reservas que comienzan
 * - Libera plazas cuyas reservas ya terminaron
 * - Llama al método refreshEstadoPlazas() del panel visual
 */
public class ParkingUpdaterThread extends Thread {

    private Parking parking;
    private Parkingviewpanel panel;
    private int planta;
    private boolean running = true;

    private int intervaloMs = 3000; // cada 3 segundos

    public ParkingUpdaterThread(Parking parking, int planta, Parkingviewpanel panel) {
        this.parking = parking;
        this.panel = panel;
        this.planta = planta;
    }

    @Override
    public void run() {
        while (running) {

            try {
                actualizarEstadoFisicoPlazas();
                actualizarPanel();
                Thread.sleep(intervaloMs);

            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    /**
     * Revisa TODAS las reservas del parking y aplica:
     * - Ocupar plaza cuando la reserva entra en curso.
     * - Liberar plaza cuando la reserva ya ha terminado.
     */
    private void actualizarEstadoFisicoPlazas() {

        LocalDateTime ahora = LocalDateTime.now();

        for (Planta pl : parking.getPlantas()) {
            for (Plaza plaza : pl.getPlazas()) {

                Coche cocheActual = plaza.getCoche();
                boolean estabaOcupada = plaza.isOcupada();
                boolean debeOcuparse = false;
                boolean debeLiberarse = false;

                // Buscar reservas válidas sobre esta plaza
                for (Coche c : parking.getListaCoches()) {
                    for (Reserva r : c.getReservas()) {
                        if (r.getPlaza() != plaza) continue;

                        // Caso 1 → RESERVA ACTIVA (inicio ≤ ahora ≤ fin)
                        if (!r.getFechaInicio().isAfter(ahora) &&
                             r.getFechaFin().isAfter(ahora)) {
                            debeOcuparse = true;
                            cocheActual = c;
                        }

                        // Caso 2 → YA TERMINÓ
                        if (r.getFechaFin().isBefore(ahora)) {
                            debeLiberarse = true;
                        }
                    }
                }

                // Aplicar cambios reales de ocupación
                if (debeLiberarse) plaza.liberar();
                if (debeOcuparse) plaza.ocupar(cocheActual);
            }
        }
    }

    /**
     * Pide a Swing que refresque el panel visualmente
     */
    private void actualizarPanel() {
        SwingUtilities.invokeLater(() -> panel.refreshEstadoPlazas());
    }

    public void detener() {
        running = false;
        this.interrupt();
    }
}

