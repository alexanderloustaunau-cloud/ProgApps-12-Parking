package MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Clases.Coche;
import Clases.Parking;
import Clases.Planta;
import Clases.Plaza;
import Clases.Reserva;

public class Parkingviewpanel extends JPanel {

    private static final long serialVersionUID = 1L;

    
    private final Color COLOR_LIBRE   = new Color(76, 175, 80);
    private final Color COLOR_OCUPADO = new Color(244, 67, 54);
    private final Color COLOR_FUTURA  = new Color(255, 165, 0);
    private final Color COLOR_SIM     = new Color(244, 67, 54);

    private JFrame parentFrame;
    private Parking parking;
    private int planta;

    // Mapa número plaza → botón
    private Map<Integer, JButton> botonesPlaza = new HashMap<>();

    public Parkingviewpanel(JFrame owner, Parking parking, int planta) {
        this.parentFrame = owner;
        this.parking = parking;
        this.planta = planta;

        this.setBackground(Color.DARK_GRAY);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        construirMapa();
        refreshEstadoPlazas();
    }

    
    private void construirMapa() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        boolean esPlanta1 = (planta == 1);

        construirLaterales();
        construirIsletas3();
        construirColumnas3();
        construirRampasJuntas(esPlanta1);

        if (esPlanta1) construirEntradaSalida();

        revalidate();
        repaint();
    }

   
    private void addPlaza(int num, int row, int col) {
        JButton btn = crearBotonPlaza(num);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.insets = new Insets(3, 3, 3, 3);

        botonesPlaza.put(num, btn);
        this.add(btn, gbc);
    }


    private void addBloque(int row, int col) {
        JPanel bloque = new JPanel();
        bloque.setBackground(new Color(80, 80, 80));
        bloque.setPreferredSize(new Dimension(35, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.insets = new Insets(3, 3, 3, 3);

        this.add(bloque, gbc);
    }

    
    private void construirEntradaSalida() {

        JLabel entrada = new JLabel("ENTRADA →", SwingConstants.CENTER);
        entrada.setOpaque(true);
        entrada.setBackground(new Color(0, 130, 0));
        entrada.setForeground(Color.WHITE);

        JLabel salida = new JLabel("← SALIDA", SwingConstants.CENTER);
        salida.setOpaque(true);
        salida.setBackground(new Color(180, 0, 0));
        salida.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 3);
        this.add(entrada, gbc);

        gbc.gridx = 17;
        gbc.gridy = 0;
        this.add(salida, gbc);
    }

    
    private void construirRampasJuntas(boolean esPlanta1) {

        JLabel rmpDown = new JLabel("RAMPA ↓", SwingConstants.CENTER);
        rmpDown.setOpaque(true);
        rmpDown.setBackground(new Color(120, 144, 156));
        rmpDown.setForeground(Color.WHITE);

        JLabel rmpUp = new JLabel(esPlanta1 ? "SUBIDA ↑" : "RAMPA ↑", SwingConstants.CENTER);
        rmpUp.setOpaque(true);
        rmpUp.setBackground(new Color(120, 144, 156));
        rmpUp.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 16;
        gbc.gridy = 15;
        gbc.insets = new Insets(3, 3, 3, 3);
        this.add(rmpDown, gbc);

        gbc.gridx = 17;
        gbc.gridy = 15;
        this.add(rmpUp, gbc);
    }

   
    private void construirLaterales() {

        for (int i = 0; i < 15; i++) {
            addPlaza(1 + i, 0, 1 + i);
        }
        for (int i = 0; i < 15; i++) {
            addPlaza(16 + i, 1 + i, 17);
        }
        for (int i = 0; i < 15; i++) {
            addPlaza(61 + i, 1 + i, 0);
        }
        for (int i = 0; i < 15; i++) {
            addPlaza(76 + i, 15, 1 + i);
        }
    }

    
    private void construirIsletas3() {

        for (int i = 0; i < 10; i++) {
            addPlaza(31 + i, 3 + i, 4);
        }

        for (int i = 0; i < 10; i++) {
            addPlaza(41 + i, 3 + i, 9);
        }

        for (int i = 0; i < 10; i++) {
            addPlaza(51 + i, 3 + i, 14);
        }
    }

  
    private void construirColumnas3() {
        addBloque(12, 4);
        addBloque(12, 14);
    }

    
    private JButton crearBotonPlaza(int num) {

        Planta plantaObj = parking.getPlantas().get(planta - 1);
        Plaza plazaReal = plantaObj.getPlazas().get(num - 1);

        JButton btn = new JButton("P" + num);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(45, 35));

        btn.addActionListener(e -> gestionarClick(plazaReal, btn));

        return btn;
    }

 
    private void gestionarClick(Plaza plazaReal, JButton btn) {

        EstadoPlaza estado = calcularEstado(plazaReal);

        switch (estado) {
            case OCUPADA_REAL:
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "La plaza está ocupada REALMENTE.\nCoche: " +
                    plazaReal.getCoche().getMatricula(),
                    "Plaza ocupada",
                    JOptionPane.ERROR_MESSAGE
                );
                return;

            case FUTURA:
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "La plaza tiene una RESERVA FUTURA y no puede ser usada.",
                    "Reserva futura",
                    JOptionPane.WARNING_MESSAGE
                );
                return;

            case OCUPADA_SIM:
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "La plaza está ocupada por el SIMULADOR.",
                    "Ocupada (Simulada)",
                    JOptionPane.ERROR_MESSAGE
                );
                return;

            case LIBRE:
                abrirDialogoReserva(plazaReal, btn);
                return;
        }
    }

   
    private void abrirDialogoReserva(Plaza plazaReal, JButton btn) {

        ReservationDialog dialog = new ReservationDialog(parentFrame, "LIBRE");
        dialog.setPlaza("P" + plazaReal.getNumero());
        dialog.setVisible(true);

        if (!dialog.isReservaConfirmada()) return;

        Coche coche = buscarOCrearCoche(
                dialog.getMatriculaSeleccionada(),
                dialog.getMarcaSeleccionada(),
                dialog.getModeloSeleccionado(),
                dialog.getColorSeleccionado()
        );

        Reserva reserva = new Reserva(
                coche,
                plazaReal,
                dialog.getFechaInicioSeleccionada(),
                dialog.getFechaFinSeleccionada()
        );

        coche.addReserva(reserva);

        // Si ya está en curso → ocupar plaza
        if (!reserva.getFechaInicio().isAfter(LocalDateTime.now())) {
            plazaReal.ocupar(coche);
        }

        refreshEstadoPlazas();
    }

    
    private Coche buscarOCrearCoche(String matricula, String marca, String modelo, Clases.Color color) {

        for (Coche c : parking.getListaCoches()) {
            if (c.getMatricula().equalsIgnoreCase(matricula)) return c;
        }

        Coche nuevo = new Coche(matricula, marca, modelo, color);
        parking.addCoche(nuevo);
        return nuevo;
    }

   
    private enum EstadoPlaza {
        LIBRE,
        OCUPADA_REAL,
        FUTURA,
        OCUPADA_SIM
    }

   
    private EstadoPlaza calcularEstado(Plaza p) {

        if (p.isOcupada()) return EstadoPlaza.OCUPADA_REAL;

        for (Coche c : parking.getListaCoches()) {
            for (Reserva r : c.getReservas()) {
                if (r.getPlaza() == p) {

                    LocalDateTime ahora = LocalDateTime.now();

                    if (r.getFechaInicio().isAfter(ahora)) {
                        return EstadoPlaza.FUTURA;
                    }
                    if (r.getFechaInicio().isBefore(ahora) &&
                        r.getFechaFin().isAfter(ahora)) {
                        return EstadoPlaza.OCUPADA_REAL;
                    }
                }
            }
        }

        return EstadoPlaza.LIBRE;
    }

 
    public void refreshEstadoPlazas() {

        Planta plantaObj = parking.getPlantas().get(planta - 1);

        for (Plaza p : plantaObj.getPlazas()) {

            JButton btn = botonesPlaza.get(p.getNumero());
            if (btn == null) continue;

            EstadoPlaza estado = calcularEstado(p);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            switch (estado) {

                case LIBRE:
                    btn.setBackground(COLOR_LIBRE);
                    btn.setToolTipText("Plaza libre");
                    break;

                case FUTURA:
                    btn.setBackground(COLOR_FUTURA);
                    Reserva futura = obtenerReservaDePlaza(p);
                    btn.setToolTipText("<html>Reserva futura<br>"
                            + futura.getFechaInicio().format(fmt)
                            + " - "
                            + futura.getFechaFin().format(fmt)
                            + "</html>");
                    break;

                case OCUPADA_REAL:
                    btn.setBackground(COLOR_OCUPADO);
                    Coche c = p.getCoche();
                    btn.setToolTipText("<html>Ocupada<br>"
                            + c.getMarca() + " " + c.getModelo()
                            + "<br>" + c.getMatricula() + "</html>");
                    break;

                case OCUPADA_SIM:
                    btn.setBackground(COLOR_SIM);
                    btn.setToolTipText("Ocupada (Simulada)");
                    break;
            }
        }

        repaint();
    }

    private Reserva obtenerReservaDePlaza(Plaza p) {
        for (Coche c : parking.getListaCoches()) {
            for (Reserva r : c.getReservas()) {
                if (r.getPlaza() == p) return r;
            }
        }
        return null;
    }
}
