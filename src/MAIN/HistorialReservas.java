package MAIN;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HistorialReservas extends JPanel {
    private static final long serialVersionUID = 1L;

    // Por ahora, solo mostramos un texto para probar
    public HistorialReservas(Object parkingSeleccionado) {
        setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Historial de Reservas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        JLabel placeholder = new JLabel(
            "Aquí irá la tabla de reservas del parking seleccionado.",
            SwingConstants.CENTER
        );
        add(placeholder, BorderLayout.CENTER);
    }
}
