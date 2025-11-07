package MAIN;

import javax.swing.*;

import Clases.Coche;
import Clases.Parking;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HistorialReservas extends JFrame {

    private JTextField campo;
    private JPanel c;
    private List<Coche> listacoches;
    
    public HistorialReservas(Parking parking) {
        this.listacoches = parking.getListaCoches();
        }
    
    public HistorialReservas() {
        setTitle("Historial de Reservas");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
       
        
        c = new JPanel(new BorderLayout());
        c.setBackground(new Color(255, 230, 230));
        c.add(cPantalla(), BorderLayout.CENTER);

        setContentPane(c);
        setVisible(true);
    }
    
        private JPanel cPantalla() {
        	 JPanel p = new JPanel();
             p.setLayout(new GridLayout(3, 1));
             
             JLabel t = new JLabel("Introduce la matrícula del coche:", SwingConstants.CENTER);
             t.setFont(new Font("Arial", Font.BOLD, 16));
             
       

        campo = new JTextField();
        campo.setFont(new Font("Arial", Font.BOLD, 14));
        
        
        JButton boton = new JButton("Aceptar");
        
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        
        boton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String matricula = campo.getText().trim().toUpperCase();
                String[][] datos = obtenerDatos(matricula);
                String[] columnas = {"ID", "Parking", "Plaza", "Inicio", "Fin", "Estado"};

                JTable tabla = new JTable(datos, columnas);
                JScrollPane scroll = new JScrollPane(tabla);

                JOptionPane.showMessageDialog(
                        null,
                        scroll,
                        "Historial de reservas de " + matricula,
                        JOptionPane.INFORMATION_MESSAGE );
               
            }
            });
        
        p.add(campo);
        p.add(t);
        p.add(boton);
       
        
        return p;
    }

       


        private String[][] obtenerDatos(String matriculaIntroducida) {

            if (listacoches != null) {
                for (Coche c : listacoches) {
                    if (matriculaIntroducida.equalsIgnoreCase(c.getMatricula())) {
                        return new String[][]{
                            {"1", "Parking Central", "B07", "05/11/2025", "06/11/2025", "Activa"}
                        };
                    }
                }
            }

            return new String[][]{
                {"", "", "", "", "", "Sin reservas para este coche"}
            };
        }

    public static void main(String[] args) {
        new HistorialReservas();
    }
}
