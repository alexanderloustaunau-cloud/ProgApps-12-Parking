package MAIN;

import javax.swing.*;
import java.awt.*;

public class HistorialReservasPorCochePanel extends JPanel {

    private String coche;
    private String[] cabeceras;
    private String[][] datos;

    public HistorialReservasPorCochePanel(String coche, String[] cabeceras, String[][] datos) {
        this.coche = coche;
        this.cabeceras = cabeceras;
        this.datos = datos;

        setBackground(new Color(255, 230, 230));
        setLayout(new GridLayout(2, 1)); 

        JLabel titulo = new JLabel("Historial de Reservas — " + coche, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo);

        add(crearTabla(this.cabeceras, this.datos));
    }

    
    public void setDatos(String coche, String[][] nuevosDatos) {
        this.coche = coche;
        this.datos = nuevosDatos;

        removeAll();
        setLayout(new GridLayout(2, 1));

        JLabel titulo = new JLabel("Historial de Reservas — " + coche, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo);

        add(crearTabla(this.cabeceras, this.datos));

        revalidate();
        repaint();
    }

    private JPanel crearTabla(String[] cabeceras, String[][] datos) {
        int columnas = cabeceras.length;
        int filasDatos = (datos == null) ? 0 : datos.length;
        int filas = 1 + Math.max(filasDatos, 1); // 1 cabecera + al menos 1 fila

        JPanel tabla = new JPanel(new GridLayout(filas, columnas));
        tabla.setBackground(new Color(255, 230, 230));


        for (int j = 0; j < columnas; j++) {
            JLabel h = new JLabel(cabeceras[j], SwingConstants.CENTER);
            h.setFont(new Font("Arial", Font.BOLD, 14));
            tabla.add(h);
        }

        if (filasDatos > 0) {
        
            for (int i = 0; i < filasDatos; i++) {
                for (int j = 0; j < columnas; j++) {
                    JLabel celda = new JLabel(datos[i][j], SwingConstants.CENTER);
                    celda.setFont(new Font("Arial", Font.PLAIN, 13));
                    tabla.add(celda);
                }
            }
        } else {
           
            for (int j = 0; j < columnas; j++) {
                JLabel celda = new JLabel(j == 0 ? "Sin reservas para este coche" : "", SwingConstants.CENTER);
                celda.setFont(new Font("Arial", Font.PLAIN, 13));
                tabla.add(celda);
            }
        }
        return tabla;
    }
}

