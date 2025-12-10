package MAIN;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import Clases.Parking;
import Clases.Coche;
import Clases.Reserva;
import Clases.Planta;
import Clases.Plaza;

public class HistorialReservas extends JPanel {

    private static final long serialVersionUID = 1L;
    private final JPanel cabecera = new JPanel();

    // 👉 Lista de parkings reales del proyecto
    private final List<Parking> parkings;

    private final JLabel lblTitulo    = new JLabel("INTRODUZCA SU MATRÍCULA:", SwingConstants.CENTER);
    private final JLabel lblSubtitulo = new JLabel("(Ej: 1234ABC)", SwingConstants.CENTER);
    public  final JTextField txtMatricula = new JTextField(28);
    public  final JButton btnAceptar  = new JButton("Aceptar");
    private final JButton btnVolver   = new JButton("Volver");

    private final JPanel centro = new JPanel();

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ------------------------------------------------------------------------
    // CONSTRUCTORES
    // ------------------------------------------------------------------------

  

    // Si usas varios parkings (Deusto, Leioa, San Mamés, ...)
    public HistorialReservas(List<Parking> parkings) {
        this.parkings = parkings;

        setLayout(new BorderLayout());

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
     // menos margen arriba y algo de margen lateral para centrar respecto al campo
     lblTitulo.setBorder(BorderFactory.createEmptyBorder(30, 200, 5, 200));

     lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 16));
     lblSubtitulo.setBorder(BorderFactory.createEmptyBorder(0, 200, 15, 200));

     
     cabecera.setLayout(new GridLayout(2, 1));
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
            lblSubtitulo.setVisible(false);           // puedes incluso quitarlo de la cabecera:
            // cabecera.remove(lblSubtitulo);
            cabecera.revalidate();
            cabecera.repaint();

            mostrarHistorial(m);
        });
        
        btnVolver.addActionListener(e -> {
            lblTitulo.setText("INTRODUZCA SU MATRÍCULA:");
            lblSubtitulo.setVisible(true);
            txtMatricula.setText("");

            construirFormulario();     // volvemos a poner el formulario en 'centro'
            txtMatricula.requestFocus();

            cabecera.revalidate();
            cabecera.repaint();
        });
    }

    // ------------------------------------------------------------------------
    // FORMULARIO INICIAL
    // ------------------------------------------------------------------------
    private void construirFormulario() {
        centro.removeAll();

        // Panel vertical: campo arriba, botón más abajo
        centro.setLayout(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(10, 200, 40, 200));

        // Panel del campo de matrícula (arriba)
        JPanel filaCampo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        txtMatricula.setPreferredSize(new Dimension(500, 50));
        txtMatricula.setFont(new Font("Arial", Font.PLAIN, 20));
        filaCampo.add(txtMatricula);
        centro.add(filaCampo, BorderLayout.NORTH);

        // Panel del botón (debajo)
        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        btnAceptar.setPreferredSize(new Dimension(220, 60));
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 22));
        btnAceptar.setBackground(new Color(120, 230, 140));
        filaBoton.add(btnAceptar);
        centro.add(filaBoton, BorderLayout.CENTER);

        centro.revalidate();
        centro.repaint();
    }


 // ------------------------------------------------------------------------
 // MOSTRAR HISTORIAL
 // ------------------------------------------------------------------------
 private void mostrarHistorial(String matricula) {
     centro.removeAll();
     // Menos margen para que la tabla ocupe más
     centro.setLayout(new BorderLayout());
     centro.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

     List<FilaHistorial> filas = buscarReservasPorMatricula(matricula);

     if (filas.isEmpty()) {
         JLabel msg = new JLabel("No hay ninguna reserva registrada para esa matrícula.", SwingConstants.CENTER);
         msg.setFont(new Font("Arial", Font.PLAIN, 18));
         centro.add(msg, BorderLayout.CENTER);
     } else {
         JTable tabla = construirTabla(filas);
         JScrollPane scroll = new JScrollPane(tabla);
         // BorderLayout.CENTER hace que el scroll (y la tabla) ocupen casi todo el espacio
         centro.add(scroll, BorderLayout.CENTER);
     }

     // Pie con el botón Volver
     JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER));
     btnVolver.setPreferredSize(new Dimension(180, 50));
     btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
     btnVolver.setBackground(new Color(255, 105, 97));
     pie.add(btnVolver);
     centro.add(pie, BorderLayout.SOUTH);

     centro.revalidate();
     centro.repaint();
 }


    // ------------------------------------------------------------------------
    // ESTRUCTURA INTERNA PARA UNA FILA
    // ------------------------------------------------------------------------
    private static class FilaHistorial {
        String matricula;
        String parking;
        String planta;
        String plaza;
        LocalDateTime inicio;
        LocalDateTime fin;
    }

    // ------------------------------------------------------------------------
    // BUSCAR TODAS LAS RESERVAS DE ESA MATRÍCULA EN TODOS LOS PARKINGS
    // ------------------------------------------------------------------------
    private List<FilaHistorial> buscarReservasPorMatricula(String matricula) {
        List<FilaHistorial> resultado = new ArrayList<>();

        for (Parking p : parkings) {
            for (Coche c : p.getListaCoches()) {
                if (!c.getMatricula().equalsIgnoreCase(matricula)) continue;

                for (Reserva r : c.getReservas()) {
                    FilaHistorial fila = new FilaHistorial();
                    fila.matricula = c.getMatricula();
                    fila.parking   = p.getNombre();

                    Plaza plaza = r.getPlaza();
                    String plantaStr = "-";
                    String plazaStr  = "-";

                    if (plaza != null) {
                        for (Planta pl : p.getPlantas()) {
                            List<Plaza> plazas = pl.getPlazas();
                            int idx = plazas.indexOf(plaza);
                            if (idx != -1) {
                                plantaStr = String.valueOf(pl.getNumeroPlanta());
                                plazaStr  = String.valueOf(plaza.getNumero());
                                break;
                            }
                        }
                    }

                    fila.planta = plantaStr;
                    fila.plaza  = plazaStr;
                    fila.inicio = r.getFechaInicio();
                    fila.fin    = r.getFechaFin();

                    resultado.add(fila);
                }
            }
        }

        return resultado;
    }

 // ------------------------------------------------------------------------
 // CONSTRUIR JTable: matricula, parking, planta, plaza, inicio, fin, estado
 // ------------------------------------------------------------------------
    private JTable construirTabla(List<FilaHistorial> filas) {
        filas.sort(Comparator.comparing(f -> f.inicio));

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Matrícula", "Parking", "Planta", "Plaza", "Inicio", "Fin", "Estado"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        LocalDateTime ahora = LocalDateTime.now();
        for (FilaHistorial f : filas) {
            String ini = f.inicio.format(fmt);
            String fin = f.fin.format(fmt);

            String estado;
            if (f.fin.isBefore(ahora)) {
                estado = "Finalizada";
            } else if ((f.inicio.isBefore(ahora) || f.inicio.isEqual(ahora)) && f.fin.isAfter(ahora)) {
                estado = "En curso";
            } else {
                estado = "Pendiente";
            }

            modelo.addRow(new Object[]{
                f.matricula, f.parking, f.planta, f.plaza, ini, fin, estado
            });
        }

        JTable tabla = new JTable(modelo) {
            @Override
            public java.awt.Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer,
                    int row, int column) {

                java.awt.Component c = super.prepareRenderer(renderer, row, column);

                if (isRowSelected(row)) {
                    c.setBackground(getSelectionBackground());
                    c.setForeground(getSelectionForeground());
                    return c;
                }

                int modelRow = convertRowIndexToModel(row);
                String estado = (String) getModel().getValueAt(modelRow, 6);

                if ("Finalizada".equalsIgnoreCase(estado)) {
                    c.setBackground(new Color(255, 200, 200));
                } else if ("En curso".equalsIgnoreCase(estado)) {
                    c.setBackground(new Color(200, 255, 200));
                } else if ("Pendiente".equalsIgnoreCase(estado)) {
                    c.setBackground(new Color(200, 220, 255));
                } else {
                    c.setBackground(Color.WHITE);
                }
                c.setForeground(Color.BLACK);

                return c;
            }
        };

        // 🔠 Fuente más grande para el contenido
        tabla.setFont(new Font("Arial", Font.PLAIN, 16));
        tabla.setRowHeight(28);

        // 🔠 Header en negrita y más grande
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        tabla.setAutoCreateRowSorter(true);
        return tabla;
    }



}
