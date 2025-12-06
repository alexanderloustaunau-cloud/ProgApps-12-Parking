package MAIN;

import java.awt.BorderLayout;
import java.awt.CardLayout; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane; 
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Clases.Parking;

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel();   

    private final JFrame parentFrame = this; 
    
    private ParkingsPanel parkingsPanel;     
    private Parkingviewpanel parkingPanelMapa; 

    
    // 👉 Lista con TODOS los parkings del proyecto
    private List<Parking> parkings;

    public GUI() {

        this.setTitle("Parking App - Menú Principal");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 700); 
        this.setLocationRelativeTo(null); 
        this.setLayout(new BorderLayout()); 

        contentPanel.setLayout(cardLayout);

     // 1) Obtenemos los parkings del proyecto (como tú querías)
     parkings = ParkingDataProvider.getParkings();

     // 2) Inicializamos datos de ejemplo sobre ESOS parkings
     DataInitializer.inicializarDatos(parkings);

     // 3) Usamos esa misma lista en los paneles
     parkingsPanel = new ParkingsPanel(parkings);
     HistorialReservas reservasPanel = new HistorialReservas(parkings);


        JPanel profilePanel = createGenericPanel("Mi Perfil");
        JPanel helpPanel    = createGenericPanel("Ayuda");

        contentPanel.add(parkingsPanel, "UBICACIONES");
        contentPanel.add(reservasPanel, "RESERVAS");
        contentPanel.add(profilePanel, "MI_PERFIL");
        contentPanel.add(helpPanel, "AYUDA");

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1, 10, 10));
        menuPanel.setPreferredSize(new Dimension(150, this.getHeight()));
        menuPanel.setBackground(new Color(40, 40, 40));

        String[] botones = {"Ubicaciones", "Reservas", "Mi Perfil", "Ayuda"};
        String[] tarjetas = {"UBICACIONES", "RESERVAS", "MI_PERFIL", "AYUDA"};

        JButton btnReservar = new JButton("RESERVAR PLAZA");
        btnReservar.setBackground(new Color(0, 150, 0));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFont(btnReservar.getFont().deriveFont(Font.BOLD, 14f));
        menuPanel.add(btnReservar);

        btnReservar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int respuesta = JOptionPane.showConfirmDialog(
                    parentFrame, 
                    "¿Desea iniciar una reserva sin seleccionar una plaza en el mapa? Esto asignará una plaza genérica.",
                    "Reservar Plaza Genérica",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    ReservationDialog dialog = new ReservationDialog(parentFrame, "LIBRE");
                    dialog.setPlaza("No Asignada (Genérica)"); 
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(
                        parentFrame,
                        "Seleccione una plaza libre directamente en el mapa para una reserva específica.",
                        "Reserva Cancelada",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        for (int i = 0; i < botones.length; i++) {
            JButton boton = new JButton(botones[i]);
            boton.setBackground(new Color(60, 60, 60)); 
            boton.setForeground(Color.WHITE);
            menuPanel.add(boton);

            final String nombreTarjeta = tarjetas[i]; 

            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, nombreTarjeta);
                }
            });
        }

        this.add(menuPanel, BorderLayout.WEST);
        this.add(contentPanel, BorderLayout.CENTER); 

        // Cuando el usuario le da a "Continuar" en ParkingsPanel:
        parkingsPanel.getBtnContinuar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Parking parkingSel = parkingsPanel.getParkingSeleccionado();
                int plantaSel = parkingsPanel.getPlantaSeleccionada();

                if (parkingSel == null || plantaSel < 1) {
                    JOptionPane.showMessageDialog(
                        parentFrame,
                        "Seleccione un parking y una planta válida.",
                        "Selección inválida",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Crear el mapa de esa planta en el parking seleccionado
                parkingPanelMapa = new Parkingviewpanel(parentFrame, parkingSel, plantaSel);

                contentPanel.add(parkingPanelMapa, "MAPA");
                contentPanel.revalidate();
                contentPanel.repaint();
                cardLayout.show(contentPanel, "MAPA");
            }
        });

        this.setVisible(true); 
    }

    private JPanel createGenericPanel(String titulo) {

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Vista de " + titulo, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);
        panel.setBackground(new Color(240, 240, 240));
        return panel;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new GUI());
    }
}

