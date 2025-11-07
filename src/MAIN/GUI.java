package MAIN;



import java.awt.BorderLayout;

import java.awt.CardLayout; 

import java.awt.Color;

import java.awt.Dimension;

import java.awt.Font;

import java.awt.GridLayout;

import java.awt.event.ActionEvent; 

import java.awt.event.ActionListener; 

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JOptionPane; 

import javax.swing.JPanel;

import javax.swing.SwingConstants;





public class GUI extends JFrame {

 private static final long serialVersionUID = 1L;

 

 private CardLayout cardLayout = new CardLayout();

 private JPanel contentPanel = new JPanel();   

 private final JFrame parentFrame = this; 

 private ParkingsPanel parkingsPanel;     
 
 private Parkingviewpanel parkingPanelMapa; 
 
 private String ultimoParkingSeleccionado = null;
 
 private int ultimaPlantaSeleccionada = -1; 

 public GUI() {

 this.setTitle("Parking App - Menú Principal");

 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 this.setSize(1000, 600); 

 this.setLocationRelativeTo(null); 

 this.setLayout(new BorderLayout()); 

 

 contentPanel.setLayout(cardLayout);


 parkingsPanel = new ParkingsPanel(ParkingDataProvider.getParkings());

 parkingPanelMapa = new Parkingviewpanel(parentFrame);

 JPanel reservationsPanel = createReservationsPanel();

 JPanel profilePanel = createGenericPanel("Mi Perfil");

 JPanel helpPanel = createGenericPanel("Ayuda");


 contentPanel.add(parkingsPanel, "UBICACIONES");
 
 contentPanel.add(parkingPanelMapa, "MAPA");

 contentPanel.add(reservationsPanel, "RESERVASk"); 

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

 parkingsPanel.getBtnContinuar().addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(ActionEvent e) {
         cardLayout.show(contentPanel, "MAPA");
     }
 });

 parkingsPanel.getBtnContinuar().addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(ActionEvent e) {
         if (parkingsPanel.getParkingSeleccionado() != null) {
             ultimoParkingSeleccionado = parkingsPanel.getParkingSeleccionado().getNombre(); 
         } else {
             ultimoParkingSeleccionado = null;
         }
         ultimaPlantaSeleccionada = parkingsPanel.getPlantaSeleccionada();

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

 

 private JPanel createReservationsPanel() {

 JPanel panel = new JPanel(new GridLayout(2, 1));

 panel.setBackground(new Color(255, 230, 230));

 JLabel titulo = new JLabel("Historial de Reservas", SwingConstants.CENTER);

 titulo.setFont(new Font("Arial", Font.BOLD, 20));

 panel.add(titulo);

 JLabel lista = new JLabel("Aquí irá la tabla de reservas pasadas y activas.", SwingConstants.CENTER);

 panel.add(lista);

 return panel;

 }

 

 public static void main(String[] args) {

 javax.swing.SwingUtilities.invokeLater(new Runnable() {

 @Override

 public void run() {

 new GUI();

 }

 });

 }

}



