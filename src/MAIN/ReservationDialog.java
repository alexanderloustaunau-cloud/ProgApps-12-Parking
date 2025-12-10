package MAIN;

import java.awt.BorderLayout;
import javax.swing.AbstractAction;

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
import javax.swing.KeyStroke;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import java.awt.Color;  // para fondos de botones

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class ReservationDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    //  plaza
    private JTextField fieldPlaza;

    // datos del coche
    private JTextField fieldPatente;
    private JTextField fieldMarca;
    private JTextField fieldModelo;
    private JComboBox<String> comboColor; 

    // fechas
    private JTextField fieldFechaInicio;   // dd/MM/yyyy HH:mm
    private JTextField fieldFechaFin;      

    
    private java.awt.CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton btnAtras;
    private JButton btnSiguiente;
    private JButton btnCancelar;
    private int pasoActual = 1; // 1 = coche, 2 = fechas

    private boolean estaOcupada = false;
    private boolean reservaConfirmada = false;

    // Datos seleccionados que devolveremos
    private String matriculaSeleccionada;
    private String marcaSeleccionada;
    private String modeloSeleccionado;
    private Clases.Color colorSeleccionado;
    private LocalDateTime fechaInicioSeleccionada;
    private LocalDateTime fechaFinSeleccionada;

    public ReservationDialog(JFrame owner, String estadoPlaza) {
        super(owner, "Nueva Reserva de Parking", true);

        if (estadoPlaza != null && estadoPlaza.equalsIgnoreCase("OCUPADA")) {
            this.setTitle("AVISO: Plaza ocupada");
            this.estaOcupada = true;
        }

        this.setSize(520, 280);
        this.setLocationRelativeTo(owner);
        this.setLayout(new BorderLayout(10, 10));
        String actionKey = "cerrarConESC";

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), actionKey);

        getRootPane().getActionMap().put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });
        
        fieldPlaza = new JTextField();
        fieldPlaza.setEnabled(false);

        
        JPanel panelCoche = new JPanel(new GridLayout(5, 2, 10, 10));

        fieldPatente = new JTextField(10);
        fieldMarca   = new JTextField(10);
        fieldModelo  = new JTextField(10);

        
        String[] opcionesColor = {
                "Seleccione color",
                "BLANCO", "NEGRO", "AZUL", "ROJO",
                "VERDE", "AMARILLO", "GRIS", "MARRON", "OTROS"
        };
        comboColor = new JComboBox<>(opcionesColor);

        panelCoche.add(new JLabel(" Plaza seleccionada:"));
        panelCoche.add(fieldPlaza);

        panelCoche.add(new JLabel(" Matrícula:"));
        panelCoche.add(fieldPatente);

        panelCoche.add(new JLabel(" Marca:"));
        panelCoche.add(fieldMarca);

        panelCoche.add(new JLabel(" Modelo:"));
        panelCoche.add(fieldModelo);

        panelCoche.add(new JLabel(" Color:"));
        panelCoche.add(comboColor);

        // ---------- PANTALLA 2: FECHAS ----------
        JPanel panelFechas = new JPanel(new GridLayout(2, 2, 10, 10));
        fieldFechaInicio = new JTextField(16);
        fieldFechaFin    = new JTextField(16);

        // Prefill: inicio = ahora + 10 minutos, fin = inicio + 1 día
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioSugerido = ahora.plusMinutes(10);
        fieldFechaInicio.setText(inicioSugerido.format(FORMATO_FECHA));
        fieldFechaFin.setText(inicioSugerido.plusDays(1).format(FORMATO_FECHA));

        panelFechas.add(new JLabel(" Fecha/hora inicio (dd/mm/yyyy xx:xx):"));
        panelFechas.add(fieldFechaInicio);

        panelFechas.add(new JLabel(" Fecha/hora fin (dd/mm/yyyy xx:xx):"));
        panelFechas.add(fieldFechaFin);

        
        cardLayout = new java.awt.CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(panelCoche, "COCHE");
        cardPanel.add(panelFechas, "FECHAS");

     
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAtras     = new JButton("Atrás");
        btnSiguiente = new JButton("Siguiente");
        btnCancelar  = new JButton("Cancelar");

        btnAtras.setEnabled(false); 

        if (estaOcupada) {
            btnSiguiente.setText("Plaza ocupada (No reservar)");
            btnSiguiente.setEnabled(false);
            btnSiguiente.setBackground(Color.LIGHT_GRAY);

            fieldPatente.setEnabled(false);
            fieldMarca.setEnabled(false);
            fieldModelo.setEnabled(false);
            comboColor.setEnabled(false);
            fieldFechaInicio.setEnabled(false);
            fieldFechaFin.setEnabled(false);
        }

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnAtras);
        buttonPanel.add(btnSiguiente);

        

        // Cancelar
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

        // Atrás
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pasoActual == 2) {
                    pasoActual = 1;
                    cardLayout.show(cardPanel, "COCHE");
                    btnAtras.setEnabled(false);
                    btnSiguiente.setText("Siguiente");
                }
            }
        });

        // Siguiente / Confirmar
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (estaOcupada) return;

                if (pasoActual == 1) {
                    // Validar datos del coche
                    if (!validarPasoCoche()) return;

                    // Pasar al paso 2
                    pasoActual = 2;
                    cardLayout.show(cardPanel, "FECHAS");
                    btnAtras.setEnabled(true);
                    btnSiguiente.setText("Confirmar reserva");
                } else {
                    // Validar fechas y finalizar
                    if (!validarPasoFechas()) return;

                    reservaConfirmada = true;

                    JOptionPane.showMessageDialog(
                            ReservationDialog.this,
                            "¡Reserva confirmada!\n" +
                                    "Plaza: " + fieldPlaza.getText() + "\n" +
                                    "Matrícula: " + matriculaSeleccionada + "\n" +
                                    "Marca: " + marcaSeleccionada + "\n" +
                                    "Modelo: " + modeloSeleccionado + "\n" +
                                    "Color: " + colorSeleccionado + "\n" +
                                    "Inicio: " + fechaInicioSeleccionada.format(FORMATO_FECHA) + "\n" +
                                    "Fin: " + fechaFinSeleccionada.format(FORMATO_FECHA),
                            "Reserva exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                }
            }
        });

        this.add(cardPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }


    private boolean validarPasoCoche() {
        String matricula = fieldPatente.getText().trim();
        String marca     = fieldMarca.getText().trim();
        String modelo    = fieldModelo.getText().trim();
        String colorTxt  = (String) comboColor.getSelectedItem();

        if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe introducir Matrícula, Marca y Modelo.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if (comboColor.getSelectedIndex() == 0) { 
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar el color de su coche.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

       
        matriculaSeleccionada = matricula;
        marcaSeleccionada     = marca;
        modeloSeleccionado    = modelo;

       
        try {
            colorSeleccionado = Clases.Color.valueOf(colorTxt.toUpperCase());
        } catch (IllegalArgumentException ex) {
            
            try {
                colorSeleccionado = Clases.Color.valueOf("OTROS");
            } catch (Exception ex2) {
                colorSeleccionado = null;
            }
        }

        return true;
    }

  
    private boolean validarPasoFechas() {
        String txtInicio = fieldFechaInicio.getText().trim();
        String txtFin    = fieldFechaFin.getText().trim();

        if (txtInicio.isEmpty() || txtFin.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe introducir fecha/hora de inicio y fecha/hora de fin.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        LocalDateTime fechaInicio;
        LocalDateTime fechaFin;

        try {
            fechaInicio = LocalDateTime.parse(txtInicio, FORMATO_FECHA);
            fechaFin    = LocalDateTime.parse(txtFin, FORMATO_FECHA);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Formato incorrecto.\nUse: dd/mm/yyyy xx:xx (ej: 01/01/2025 01:01).",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteInicioMax = ahora.plusDays(3);   

        
        if (fechaInicio.isBefore(ahora)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha/hora de inicio no puede ser anterior al momento actual.",
                    "Fecha de inicio inválida",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

       
        if (fechaInicio.isAfter(limiteInicioMax)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo puede reservar con hasta 3 días de antelación.\n" +
                            "Inicio máximo permitido: " + limiteInicioMax.format(FORMATO_FECHA),
                    "Fecha de inicio demasiado lejana",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        
        if (!fechaFin.isAfter(fechaInicio)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha/hora de fin debe ser posterior a la de inicio.",
                    "Rango de fechas inválido",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        //  MAX 24 horas
        LocalDateTime limiteFinMax = fechaInicio.plusHours(24);
        if (fechaFin.isAfter(limiteFinMax)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La reserva no puede durar más de 24 horas.\n" +
                            "Fin máximo permitido para ese inicio: " + limiteFinMax.format(FORMATO_FECHA),
                    "Duración excesiva",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        fechaInicioSeleccionada = fechaInicio;
        fechaFinSeleccionada    = fechaFin;

        return true;
    }

    

    public boolean isReservaConfirmada() {
        return reservaConfirmada;
    }

    public String getMatriculaSeleccionada() {
        return matriculaSeleccionada;
    }

    public String getMarcaSeleccionada() {
        return marcaSeleccionada;
    }

    public String getModeloSeleccionado() {
        return modeloSeleccionado;
    }

    public Clases.Color getColorSeleccionado() {
        return colorSeleccionado;
    }

    public LocalDateTime getFechaInicioSeleccionada() {
        return fechaInicioSeleccionada;
    }

    public LocalDateTime getFechaFinSeleccionada() {
        return fechaFinSeleccionada;
    }

    public void setPlaza(String numeroPlaza) {
        fieldPlaza.setText(numeroPlaza);
    }
}

