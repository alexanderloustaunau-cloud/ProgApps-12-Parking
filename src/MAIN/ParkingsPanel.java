package MAIN;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Clases.Parking;

public class ParkingsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final List<Parking> parkings;
    private final JComboBox<String> comboParkings;
    private final JComboBox<Integer> comboPlantas;   // [NUEVO]
    private final JLabel lblDetalle;
    private final JButton btnContinuar;

    public ParkingsPanel(List<Parking> parkings) {
        this.parkings = parkings;

        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(new Color(240, 240, 240));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Selecciona parking y planta", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setOpaque(false);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BorderLayout(10, 10));

        comboParkings = new JComboBox<String>();
        comboParkings.setPreferredSize(new Dimension(300, 40));
        comboParkings.setFont(new Font("Arial", Font.PLAIN, 16));

        // Cargar parkings
        if (parkings != null) {
            int i = 0;
            while (i < parkings.size()) {
                Parking p = parkings.get(i);
                comboParkings.addItem(p.getNombre());
                i++;
            }
        }

        // [NUEVO] Combo de plantas
        comboPlantas = new JComboBox<Integer>();
        comboPlantas.setPreferredSize(new Dimension(300, 40));
        comboPlantas.setFont(new Font("Arial", Font.PLAIN, 16));

        // [NUEVO] Forzar selección válida antes de recargar plantas
        if (comboParkings.getItemCount() > 0) {     // [NUEVO]
            comboParkings.setSelectedIndex(0);      // [NUEVO]
            recargarPlantas();                      // [NUEVO]
        }

        lblDetalle = new JLabel(detalleSeleccion(), SwingConstants.CENTER);
        lblDetalle.setFont(new Font("Arial", Font.PLAIN, 14));

        // Listeners (sin lambdas)
        comboParkings.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                recargarPlantas();                  // si cambia parking, repuebla plantas
                lblDetalle.setText(detalleSeleccion());
            }
        });

        comboPlantas.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                lblDetalle.setText(detalleSeleccion());
            }
        });

        btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        btnContinuar.setPreferredSize(new Dimension(160, 40));

        // Montaje vertical simple
        JPanel arriba = new JPanel();
        arriba.setOpaque(false);
        arriba.setLayout(new BorderLayout(10, 10));
        arriba.add(comboParkings, BorderLayout.NORTH);
        arriba.add(comboPlantas, BorderLayout.SOUTH);

        centro.add(arriba, BorderLayout.NORTH);
        centro.add(lblDetalle, BorderLayout.CENTER);
        centro.add(btnContinuar, BorderLayout.SOUTH);

        this.add(titulo, BorderLayout.NORTH);
        this.add(centro, BorderLayout.CENTER);
    }

    // [NUEVO] Devuelve el parking seleccionado (o null)
    public Parking getParkingSeleccionado() {
        int idx = comboParkings.getSelectedIndex();
        if (parkings == null || parkings.isEmpty() || idx < 0 || idx >= parkings.size()) {
            return null;
        }
        return parkings.get(idx);
    }

    // [NUEVO] Devuelve la planta seleccionada (1..N). Si nada seleccionado, devuelve -1.
    public int getPlantaSeleccionada() {
        Integer val = (Integer) comboPlantas.getSelectedItem();
        return val == null ? -1 : val.intValue();
    }

    // [NUEVO] Exponemos el botón para que la GUI añada su ActionListener
    public JButton getBtnContinuar() {
        return btnContinuar;
    }

    // [NUEVO] Rellena el combo de plantas según el parking seleccionado
    private void recargarPlantas() {
        comboPlantas.removeAllItems();
        Parking sel = getParkingSeleccionado();
        if (sel == null || sel.getPlantas() == null) {
            return;
        }
        int n = sel.getPlantas().size();
        int i = 1;
        while (i <= n) {
            comboPlantas.addItem(Integer.valueOf(i));
            i++;
        }
        if (comboPlantas.getItemCount() > 0) {
            comboPlantas.setSelectedIndex(0);
        }
    }

    private String detalleSeleccion() {
        Parking sel = getParkingSeleccionado();
        if (sel == null) {
            return "No hay parkings configurados.";
        }
        int numPlantas = sel.getPlantas() != null ? sel.getPlantas().size() : 0;
        String plantaTxt = getPlantaSeleccionada() > 0 ? (" | Planta: " + getPlantaSeleccionada()) : "";
        return "Seleccionado: " + sel.getNombre() + "  |  Nº de plantas: " + numPlantas + plantaTxt;
    }
}