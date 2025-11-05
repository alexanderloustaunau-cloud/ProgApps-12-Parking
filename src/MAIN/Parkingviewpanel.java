package MAIN;



import java.awt.Color;

import java.awt.Dimension;

import java.awt.Font;

import java.awt.GridLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener; 

import java.util.Random;

import javax.swing.JButton;

import javax.swing.JPanel;

import javax.swing.JFrame; 

import javax.swing.JOptionPane; 

import javax.swing.border.EmptyBorder; 



public class Parkingviewpanel extends JPanel {

 private static final long serialVersionUID = 1L;

 private final int FILAS = 10;

 private final int COLUMNAS = 10;

 private final Color COLOR_LIBRE = new Color(76, 175, 80); 

 private final Color COLOR_OCUPADO = new Color(244, 67, 54); 

 

 private JFrame parentFrame;



 public Parkingviewpanel(JFrame owner) {

 this.parentFrame = owner; 

 

 this.setLayout(new GridLayout(FILAS, COLUMNAS, 5, 5));

 this.setBackground(Color.DARK_GRAY);

 this.setBorder(new EmptyBorder(10, 10, 10, 10)); 



 Random random = new Random();



 for (int i = 1; i <= FILAS * COLUMNAS; i++) {

 final JButton plaza = new JButton(String.valueOf(i)); 

 plaza.setFont(new Font("Arial", Font.BOLD, 10));

 plaza.setForeground(Color.WHITE);

 plaza.setPreferredSize(new Dimension(50, 50)); 



 

 boolean isFree = random.nextDouble() < 0.8;

 if (isFree) {

 plaza.setBackground(COLOR_LIBRE);

 plaza.setToolTipText("Plaza Libre - Haz clic para reservar");

 } else {

 plaza.setBackground(COLOR_OCUPADO);

 plaza.setToolTipText("Plaza Ocupada");

 }



 

 plaza.addActionListener(new ActionListener() {

 @Override

 public void actionPerformed(ActionEvent e) {

 

 String estadoActual = plaza.getBackground().equals(COLOR_LIBRE) ? "LIBRE" : "OCUPADA";

 

 if (estadoActual.equals("LIBRE")) {

 ReservationDialog dialog = new ReservationDialog(parentFrame, estadoActual);

 dialog.setPlaza(plaza.getText());

 dialog.setVisible(true);

 

 } else {

 JOptionPane.showMessageDialog(

 parentFrame,

 "La plaza " + plaza.getText() + " está ocupada. No se puede iniciar una nueva reserva.",

 "Plaza No Disponible",

 JOptionPane.ERROR_MESSAGE

 );

 }

 }

 });



 this.add(plaza);

 }

 }

}