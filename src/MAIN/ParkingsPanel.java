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

public class ParkingsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private final List<Parking> parkings;
    private final JComboBox<String> comboParkings;
    private final JLabel lblDetalle;
    private final JButton btnContinuar;
    
    public ParkingsPanel(List<Parking> parkings) {
        this.parkings = parkings;

        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(new Color(240, 240, 240));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Selecciona un parking", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setOpaque(false);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BorderLayout(10, 10));

        comboParkings = new JComboBox<String>();
        comboParkings.setPreferredSize(new Dimension(300, 40));
        comboParkings.setFont(new Font("Arial", Font.PLAIN, 16));

        // Cargar opciones en el combo
        if (parkings != null) {
            int i = 0;
            while (i < parkings.size()) {
                Parking p = parkings.get(i);
                comboParkings.addItem(p.getNombre());
                i++;
            }
        }

        lblDetalle = new JLabel(detalleSeleccion(), SwingConstants.CENTER);
        lblDetalle.setFont(new Font("Arial", Font.PLAIN, 14));

        // Actualiza el detalle cuando cambia la selección (sin lambdas)
        comboParkings.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                lblDetalle.setText(detalleSeleccion());
            }
        });

        btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        btnContinuar.setPreferredSize(new Dimension(160, 40));

        // Montaje
        centro.add(comboParkings, BorderLayout.NORTH);
        centro.add(lblDetalle, BorderLayout.CENTER);
        centro.add(btnContinuar, BorderLayout.SOUTH);

        this.add(titulo, BorderLayout.NORTH);
        this.add(centro, BorderLayout.CENTER);
    }

    // [NUEVO] Devuelve el parking actualmente seleccionado (o null si no hay datos)
    public Parking getParkingSeleccionado() {
        int idx = comboParkings.getSelectedIndex();
        if (parkings == null || parkings.isEmpty() || idx < 0 || idx >= parkings.size()) {
            return null;
        }
        return parkings.get(idx);
    }

    // [NUEVO] Exponemos el botón para que la GUI pueda añadir su ActionListener
    public JButton getBtnContinuar() {
        return btnContinuar;
    }

    // [NUEVO] Texto informativo (nombre + nº plantas si hay datos)
    private String detalleSeleccion() {
        Parking sel = getParkingSeleccionado();
        if (sel == null) {
            return "No hay parkings configurados.";
        }
        int numPlantas = sel.getPlantas() != null ? sel.getPlantas().size() : 0;
        return "Seleccionado: " + sel.getNombre() + "  |  Nº de plantas: " + numPlantas;
    }
}
    
	
	
	

