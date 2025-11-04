package MAIN;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ReservationDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField fieldPlaza;
    private JTextField fieldPatente;
    private JTextField fieldHoraEntrada;

    public ReservationDialog(JFrame owner) {
        super(owner, "Nueva Reserva de Parking", true); 
        this.setSize(400, 250);
        this.setLocationRelativeTo(owner);
        this.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        fieldPlaza = new JTextField();
        fieldPatente = new JTextField(10);
        fieldHoraEntrada = new JTextField(10);

        formPanel.add(new JLabel("  Plaza Seleccionada:"));
        formPanel.add(fieldPlaza);
        formPanel.add(new JLabel("  Patente/Matrícula:"));
        formPanel.add(fieldPatente);
        formPanel.add(new JLabel("  Hora de Entrada (Ej. 14:30):"));
        formPanel.add(fieldHoraEntrada);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnReservar = new JButton("Confirmar Reserva");
        JButton btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnReservar);
        
        
        btnReservar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("--- Reserva Confirmada ---");
                System.out.println("Patente: " + fieldPatente.getText());
                System.out.println("Plaza: " + fieldPlaza.getText());
                System.out.println("Hora: " + fieldHoraEntrada.getText());
                
                dispose(); 
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void setPlaza(String numeroPlaza) {
        fieldPlaza.setText(numeroPlaza);
        fieldPlaza.setEnabled(false); 
    }
}