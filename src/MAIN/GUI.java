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

import Clases.Coche;
import Clases.Parking;
import Clases.Plaza;
import Clases.Reserva;
import MAIN.GestorDB; 

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel();   

    private final JFrame parentFrame = this; 
    
    private ParkingsPanel parkingsPanel;     
    private Parkingviewpanel parkingPanelMapa; 
    
    private GestorDB gestorDB;

    
    // Lista TODOS los parkings
    private List<Parking> parkings;

    public GUI() {

        this.setTitle("Parking App - Menú Principal");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 700); 
        this.setLocationRelativeTo(null); 
        this.setLayout(new BorderLayout()); 

        contentPanel.setLayout(cardLayout);
        
        gestorDB = new GestorDB();
        gestorDB.initDatabase();

     // 1) Obtenemos los parkings del proyecto 
     parkings = ParkingDataProvider.getParkings();

     // 2) Inicializamos datos de ejemplo sobre ESOS parkings
     IniciarDatos.inicializarDatos(parkings,gestorDB);

     
     parkingsPanel = new ParkingsPanel(parkings);
     HistorialReservas reservasPanel = new HistorialReservas(parkings);


        
        JPanel helpPanel    = createGenericPanel("Ayuda");

        contentPanel.add(parkingsPanel, "UBICACIONES");
        contentPanel.add(reservasPanel, "RESERVAS");
        
        contentPanel.add(helpPanel, "AYUDA");

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1, 10, 10));
        menuPanel.setPreferredSize(new Dimension(150, this.getHeight()));
        menuPanel.setBackground(new Color(40, 40, 40));

        String[] botones = {"Ubicaciones", "Reservas", "Ayuda"};
        String[] tarjetas = {"UBICACIONES", "RESERVAS", "AYUDA"};

        JButton btnReservar = new JButton("<html>RESERVAR<br>PLAZA</html>");
        btnReservar.setBackground(new Color(0, 150, 0));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFont(btnReservar.getFont().deriveFont(Font.BOLD, 14f));
        menuPanel.add(btnReservar);
        
        btnReservar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Parking parkingSel = parkingsPanel.getParkingSeleccionado();
                if (parkingSel == null) {
                    JOptionPane.showMessageDialog(parentFrame, "Selecciona un parking primero.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int respuesta = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "¿Desea iniciar una reserva sin seleccionar una plaza en el mapa?\n" +
                    "Se asignará una plaza LIBRE aleatoria del parking seleccionado.",
                    "Reserva automática",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (respuesta != JOptionPane.YES_OPTION) return;

                
                PlazaAsignada asignada = elegirPlazaLibreAleatoria(parkingSel);
                if (asignada == null) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "No hay plazas disponibles (libres) en este parking ahora mismo.",
                            "Sin disponibilidad",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

              
                ReservationDialog dialog = new ReservationDialog(parentFrame, "LIBRE");
                dialog.setPlaza("Parking: " + parkingSel.getNombre() +
                        " | Planta: " + asignada.numPlanta +
                        " | Plaza: P" + asignada.plaza.getNumero());
                dialog.setVisible(true);

                if (!dialog.isReservaConfirmada()) return;

                
                Coche coche = null;
                for (Coche c : parkingSel.getListaCoches()) {
                    if (c.getMatricula().equalsIgnoreCase(dialog.getMatriculaSeleccionada())) {
                        coche = c;
                        break;
                    }
                }
                if (coche == null) {
                    coche = new Coche(
                        dialog.getMatriculaSeleccionada(),
                        dialog.getMarcaSeleccionada(),
                        dialog.getModeloSeleccionado(),
                        dialog.getColorSeleccionado()
                    );
                    parkingSel.addCoche(coche);
                }

               
                Reserva reserva = new Reserva(
                    coche,
                    asignada.plaza,
                    dialog.getFechaInicioSeleccionada(),
                    dialog.getFechaFinSeleccionada()
                );
                coche.addReserva(reserva);

              
                if (!reserva.getFechaInicio().isAfter(java.time.LocalDateTime.now())) {
                    asignada.plaza.ocupar(coche);
                }

                JOptionPane.showMessageDialog(parentFrame,
                        "Reserva guardada en el historial:\n" +
                        parkingSel.getNombre() + " / Planta " + asignada.numPlanta +
                        " / Plaza P" + asignada.plaza.getNumero(),
                        "OK",
                        JOptionPane.INFORMATION_MESSAGE);
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

        // Continuar
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

               
                parkingPanelMapa = new Parkingviewpanel(parentFrame, parkingSel, plantaSel);

        
             ParkingUpdaterThread updater = new ParkingUpdaterThread(
                     parkingSel,
                     plantaSel,
                     parkingPanelMapa
             );
             updater.start();

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
    
    private static class PlazaAsignada {
        Plaza plaza;
        int numPlanta;
        PlazaAsignada(Plaza plaza, int numPlanta) {
            this.plaza = plaza;
            this.numPlanta = numPlanta;
        }
    }

    private PlazaAsignada elegirPlazaLibreAleatoria(Parking parking) {
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();

        java.util.List<PlazaAsignada> candidatas = new java.util.ArrayList<>();

        for (Clases.Planta pl : parking.getPlantas()) {
            for (Clases.Plaza plaza : pl.getPlazas()) {
                if (esPlazaDisponible(parking, plaza, ahora)) {
                    candidatas.add(new PlazaAsignada(plaza, pl.getNumeroPlanta()));
                }
            }
        }

        if (candidatas.isEmpty()) return null;

        int indice = new java.util.Random().nextInt(candidatas.size());
        return candidatas.get(indice);
    }

    private boolean esPlazaDisponible(Parking parking, Plaza plaza, java.time.LocalDateTime ahora) {
        if (plaza.isOcupada()) return false;

       
        for (Coche c : parking.getListaCoches()) {
            for (Reserva r : c.getReservas()) {
                if (r.getPlaza() == plaza && r.getFechaFin().isAfter(ahora)) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new GUI());
    }
}

