package MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private JFrame parentFrame;

    private Parking parking;
    private int planta;

    // ------------------------------------------------------------------------
    // CONSTRUCTOR
    // ------------------------------------------------------------------------
    public Parkingviewpanel(JFrame owner, Parking parking, int planta) {
        this.parentFrame = owner;
        this.parking = parking;
        this.planta = planta;

        this.setBackground(Color.DARK_GRAY);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        construirMapa();
    }

    // ------------------------------------------------------------------------
    // MAPA COMPLETO (usa GridBagLayout)
    // ------------------------------------------------------------------------
    private void construirMapa() {

        this.removeAll();
        this.setLayout(new GridBagLayout());

        boolean esPlanta1 = (planta == 1);

        construirLaterales();
        construirIsletas3();
        construirColumnas3();
        construirRampasJuntas(esPlanta1);

        if (esPlanta1) {
            construirEntradaSalida();
        }

        revalidate();
        repaint();
    }

    // ------------------------------------------------------------------------
    // AÑADIR PLAZA EN COORDENADA
    // ------------------------------------------------------------------------
    private void addPlaza(int num, int row, int col) {
        JButton btn = crearBotonPlaza(num);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.insets = new Insets(3, 3, 3, 3);

        this.add(btn, gbc);
    }

    // ------------------------------------------------------------------------
    // AÑADIR BLOQUE (columna física)
    // ------------------------------------------------------------------------
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

    // ------------------------------------------------------------------------
    // ENTRADA / SALIDA (solo en planta 1)
    // ------------------------------------------------------------------------
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

        // ENTRADA
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 3);
        this.add(entrada, gbc);

        // SALIDA
        gbc.gridx = 17;
        gbc.gridy = 0;
        this.add(salida, gbc);
    }

    // ------------------------------------------------------------------------
    // RAMPAS JUNTAS (2 celdas)
    // ------------------------------------------------------------------------
    private void construirRampasJuntas(boolean esPlanta1) {

        JLabel rmpDown = new JLabel("RAMPA ↓", SwingConstants.CENTER);
        rmpDown.setOpaque(true);
        rmpDown.setBackground(new Color(200, 120, 0));
        rmpDown.setForeground(Color.WHITE);

        JLabel rmpUp = new JLabel(esPlanta1 ? "SUBIDA ↑" : "RAMPA ↑", SwingConstants.CENTER);
        rmpUp.setOpaque(true);
        rmpUp.setBackground(new Color(200, 120, 0));
        rmpUp.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();

        // ↓ BAJADA
        gbc.gridx = 16;
        gbc.gridy = 15;
        gbc.insets = new Insets(3, 3, 3, 3);
        this.add(rmpDown, gbc);

        // ↑ SUBIDA
        gbc.gridx = 17;
        gbc.gridy = 15;
        this.add(rmpUp, gbc);
    }

    // ------------------------------------------------------------------------
    // LATERALES DEL PARKING (1–15, 16–30, 61–75, 76–90)
    // ------------------------------------------------------------------------
    private void construirLaterales() {

        // Superior 1–15
        for (int i = 0; i < 15; i++) {
            addPlaza(1 + i, 0, 1 + i);
        }

        // Derecha 16–30
        for (int i = 0; i < 15; i++) {
            addPlaza(16 + i, 1 + i, 17);
        }

        // Izquierda 61–75
        for (int i = 0; i < 15; i++) {
            addPlaza(61 + i, 1 + i, 0);
        }

        // Inferior 76–90
        for (int i = 0; i < 15; i++) {
            addPlaza(76 + i, 15, 1 + i);
        }
    }

    // ------------------------------------------------------------------------
    // TRES ISLETAS INTERIORES (31–40 / 41–50 / 51–60)
    // ------------------------------------------------------------------------
    private void construirIsletas3() {

        // Isleta 1 (izquierda) 31–40
        for (int i = 0; i < 10; i++) {
            addPlaza(31 + i, 3 + i, 4);
        }

        // Isleta 2 (centro) 41–50
        for (int i = 0; i < 10; i++) {
            addPlaza(41 + i, 3 + i, 9);
        }

        // Isleta 3 (derecha) 51–60
        for (int i = 0; i < 10; i++) {
            addPlaza(51 + i, 3 + i, 14);
        }
    }

    // ------------------------------------------------------------------------
    // COLUMNAS FÍSICAS
    // ------------------------------------------------------------------------
    private void construirColumnas3() {

        // Columna izquierda inferior
        addBloque(12, 4);

        // Columna derecha inferior
        addBloque(12, 14);
    }

    // ------------------------------------------------------------------------
    // BOTÓN DE PLAZA (pinta estado y gestiona reserva)
    // ------------------------------------------------------------------------
    private JButton crearBotonPlaza(int num) {

        Planta plantaObj = parking.getPlantas().get(planta - 1);
        Plaza plazaReal = plantaObj.getPlazas().get(num - 1);

        JButton btn = new JButton("P" + num);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(45, 35));

        if (plazaReal.isOcupada()) {
            btn.setBackground(COLOR_OCUPADO);
            btn.setToolTipText("Plaza ocupada");
        } else {
            btn.setBackground(COLOR_LIBRE);
            btn.setToolTipText("Plaza libre");
        }
        btn.addActionListener(e -> {
            if (plazaReal.isOcupada()) {
                // Obtenemos el coche que ocupa la plaza (si lo hay)
                Coche cocheOcupante = plazaReal.getCoche();
                String matriculaOcupante = (cocheOcupante != null)
                        ? cocheOcupante.getMatricula()
                        : "desconocida";

                JOptionPane.showMessageDialog(
                    parentFrame,
                    "La plaza ya está ocupada.\n(Matrícula: " + matriculaOcupante + ")",
                    "Plaza ocupada",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            ReservationDialog dialog = new ReservationDialog(parentFrame, "LIBRE");
            dialog.setPlaza("P" + num);
            dialog.setVisible(true);

            if (dialog.isReservaConfirmada()) {

                String matricula     = dialog.getMatriculaSeleccionada();
                String marca         = dialog.getMarcaSeleccionada();
                String modelo        = dialog.getModeloSeleccionado();
                Clases.Color color   = dialog.getColorSeleccionado();
                LocalDateTime inicio = dialog.getFechaInicioSeleccionada();
                LocalDateTime fin    = dialog.getFechaFinSeleccionada();

                Coche coche = buscarOCrearCoche(matricula, marca, modelo, color);

                Reserva reserva = new Reserva(coche, plazaReal, inicio, fin);
                coche.addReserva(reserva);

                plazaReal.ocupar(coche);

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                btn.setBackground(COLOR_OCUPADO);
                btn.setToolTipText(
                    "<html>Plaza ocupada<br>" +
                    "Coche: " + marca + " " + modelo + "<br>" +
                    inicio.format(fmt) + " - " + fin.format(fmt) +
                    "</html>"
                );
            }
        });


        return btn;
    }

    // ------------------------------------------------------------------------
    // Buscar o crear coche en el parking
    // ------------------------------------------------------------------------
    private Coche buscarOCrearCoche(String matricula, String marca, String modelo, Clases.Color color) {
        for (Coche c : parking.getListaCoches()) {
            if (c.getMatricula().equalsIgnoreCase(matricula)) {

                // Opcional: actualizar datos si estaban genéricos
                if (c.getMarca() == null || c.getMarca().isBlank()) c.setMarca(marca);
                if (c.getModelo() == null || c.getModelo().isBlank()) c.setModelo(modelo);
                if (c.getColor() == null && color != null) c.setColor(color);

                return c;
            }
        }

        // Si no existe, lo creamos con los datos proporcionados
        Coche nuevo = new Coche(matricula, marca, modelo, color);
        parking.addCoche(nuevo);
        return nuevo;
    }

}
