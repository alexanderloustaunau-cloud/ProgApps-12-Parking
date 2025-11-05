
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

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JTextField;

import java.awt.Color;



public class ReservationDialog extends JDialog {

 private static final long serialVersionUID = 1L;



 private JTextField fieldPlaza;

 private JTextField fieldPatente;

 private JTextField fieldHoraEntrada;

 private boolean estaOcupada = false;



 public ReservationDialog(JFrame owner, String estadoPlaza) {

 super(owner, "Nueva Reserva de Parking", true); 

 

 if (estadoPlaza.equalsIgnoreCase("OCUPADA")) {

 this.setTitle("AVISO: Plaza " + this.getTitle() + " - OCUPADA");

 this.estaOcupada = true;

 }



 this.setSize(400, 250);

 this.setLocationRelativeTo(owner);

 this.setLayout(new BorderLayout(10, 10));



 JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

 

 fieldPlaza = new JTextField();

 fieldPatente = new JTextField(10);

 fieldHoraEntrada = new JTextField(10);



 formPanel.add(new JLabel(" Plaza Seleccionada:"));

 formPanel.add(fieldPlaza);

 formPanel.add(new JLabel(" Patente/Matrícula:"));

 formPanel.add(fieldPatente);

 formPanel.add(new JLabel(" Hora de Entrada (Ej. 14:30):"));

 formPanel.add(fieldHoraEntrada);

 

 if (estaOcupada) {

 fieldPatente.setEnabled(false);

 fieldHoraEntrada.setEnabled(false);

 fieldPatente.setText("PLAZA OCUPADA");

 }



 JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

 JButton btnReservar = new JButton("Confirmar Reserva");

 JButton btnCancelar = new JButton("Cancelar");

 

 if (estaOcupada) {

 btnReservar.setText("Plaza Ocupada (No Reservar)");

 btnReservar.setEnabled(false);

 btnReservar.setBackground(Color.LIGHT_GRAY);

 }



 buttonPanel.add(btnCancelar);

 buttonPanel.add(btnReservar);

 

 

 btnReservar.addActionListener(new ActionListener() {

 @Override

 public void actionPerformed(ActionEvent e) {

 if (estaOcupada) return; 



 String patente = fieldPatente.getText().trim();

 String hora = fieldHoraEntrada.getText().trim();

 

 if (patente.isEmpty() || hora.isEmpty()) {

 JOptionPane.showMessageDialog(

 ReservationDialog.this, 

 "Debe introducir la Matrícula y la Hora de Entrada para confirmar la reserva.",

 "Error de Validación",

 JOptionPane.ERROR_MESSAGE

 );

 return;

 }



 JOptionPane.showMessageDialog(

 ReservationDialog.this, 

 "¡Reserva confirmada con éxito!\nPlaza: " + fieldPlaza.getText() + ", Matrícula: " + patente,

 "Reserva Exitosa",

 JOptionPane.INFORMATION_MESSAGE

 );



 System.out.println("--- Reserva Confirmada ---");

 System.out.println("Patente: " + patente);

 System.out.println("Plaza: " + fieldPlaza.getText());

 System.out.println("Hora: " + hora);

 

 dispose(); 

 }

 });

 

 btnCancelar.addActionListener(new ActionListener() {

 @Override

 public void actionPerformed(ActionEvent e) {

 String mensaje = estaOcupada ? "Detalles de plaza cerrada." : "Reserva cancelada.";

 JOptionPane.showMessageDialog(

 ReservationDialog.this,

 mensaje,

 "Cerrado",

 JOptionPane.WARNING_MESSAGE

 );

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

