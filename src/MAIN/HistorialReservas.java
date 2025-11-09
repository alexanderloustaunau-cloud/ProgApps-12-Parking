package MAIN;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class HistorialReservas extends JPanel {

   
    private final Map<String, List<Reserva>> reservasDB = new HashMap<>();

   
    private final JLabel lblTitulo = new JLabel("INTRODUZCA SU MATRÍCULA:", SwingConstants.CENTER);
    private final JLabel lblSubtitulo = new JLabel("(Ej ; 1234ABC)", SwingConstants.CENTER);
    public final JTextField txtMatricula = new JTextField(28);
    public final JButton btnAceptar = new JButton("Aceptar");
    private final JButton btnVolver = new JButton("Volver");

   
    private final JPanel centro = new JPanel();

    public HistorialReservas() {
        setLayout(new BorderLayout());
        cargaEjemplo(); 

        
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(100, 0, 10, 0));
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 16));

        
        JPanel cabecera = new JPanel(new GridLayout(2, 1));
        cabecera.add(lblTitulo);
        cabecera.add(lblSubtitulo);
        add(cabecera, BorderLayout.NORTH);

       
        construirFormulario();
        add(centro, BorderLayout.CENTER);

       
        btnAceptar.addActionListener(e -> {
            String m = txtMatricula.getText().trim().toUpperCase();
            if (m.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, introduzca una matrícula.",
                        "Campo vacío", JOptionPane.WARNING_MESSAGE);
                txtMatricula.requestFocus();
                return;
            }
            lblTitulo.setText("MATRÍCULA INTRODUCIDA: " + m);
            lblSubtitulo.setVisible(false);
            mostrarHistorial(m);
        });

       
        btnVolver.addActionListener(e -> {
            lblTitulo.setText("INTRODUZCA SU MATRÍCULA:");
            lblSubtitulo.setVisible(true); 
            txtMatricula.setText("");
            construirFormulario();
            txtMatricula.requestFocus();
        });
    }

  

    private void construirFormulario() {
        centro.removeAll();
        centro.setLayout(new GridLayout(2, 1, 10, 40));
        centro.setBorder(BorderFactory.createEmptyBorder(50, 60, 60, 60));

        JPanel filaCampo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        txtMatricula.setPreferredSize(new Dimension(220, 50));
        txtMatricula.setFont(new Font("Arial", Font.PLAIN, 20));
        filaCampo.add(txtMatricula);
        centro.add(filaCampo);

        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAceptar.setPreferredSize(new Dimension(200, 60));
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 22));
        btnAceptar.setBackground(new Color(120, 230, 140));
        filaBoton.add(btnAceptar);
        centro.add(filaBoton);

        centro.revalidate();
        centro.repaint();
    }

    private void mostrarHistorial(String matricula) {
        centro.removeAll();
        centro.setLayout(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));

     
        JLabel subtitulo = new JLabel("Historial de reservas para: " + matricula, SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.BOLD, 22));
        subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        centro.add(subtitulo, BorderLayout.NORTH);

       
        List<Reserva> reservas = reservasDB.getOrDefault(matricula, Collections.emptyList());
        if (reservas.isEmpty()) {
            JLabel msg = new JLabel("No hay ninguna reserva registrada.", SwingConstants.CENTER);
            msg.setFont(new Font("Arial", Font.PLAIN, 18));
            centro.add(msg, BorderLayout.CENTER);
        } else {
            JTable tabla = construirTabla(reservas);
            centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        }

     
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnVolver.setPreferredSize(new Dimension(180, 50));
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setBackground(new Color(255, 105, 97));
        pie.add(btnVolver);
        centro.add(pie, BorderLayout.SOUTH);

        centro.revalidate();
        centro.repaint();
    }

    private JTable construirTabla(List<Reserva> reservas) {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Plaza", "Inicio", "Fin", "Estado"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Reserva r : reservas) {
            modelo.addRow(new Object[]{ r.id, r.plaza, r.inicio, r.fin, r.estado });
        }
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(24);
        return tabla;
    }

    

    private void cargaEjemplo() {
        
        String mat = "1234ABC";

        List<Reserva> lista = new ArrayList<>();
        lista.add(new Reserva("R-1001", "P-12", "2025-10-02 09:00", "2025-10-02 11:30", "Finalizada"));
        lista.add(new Reserva("R-1015", "P-07", "2025-11-08 16:15", "2025-11-08 18:00", "Finalizada"));

        reservasDB.put(mat, lista);
    }

  
    private static class Reserva {
        String id, plaza, inicio, fin, estado;
        Reserva(String id, String plaza, String inicio, String fin, String estado) {
            this.id = id; this.plaza = plaza; this.inicio = inicio; this.fin = fin; this.estado = estado;
        }
    }
}
