package MAIN;  
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Clases.Coche;
import Clases.Color;
import Clases.Plaza;
import Clases.Reserva;

public class GestorDB {

    private static final String SQLITE_FILE = "resources/db/parking.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + SQLITE_FILE;

   
    private static final DateTimeFormatter FMT_DB = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public GestorDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            java.io.File dbFile = new java.io.File("resources/db/parking.db");
            System.out.println("Ruta absoluta BD: " + dbFile.getAbsolutePath());
        } catch (ClassNotFoundException e) {
            System.err.format("* Error al cargar el driver de la BBDD: %s\n", e.getMessage());
        }
    }

    
    public void initDatabase() {
        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             Statement st = con.createStatement()) {

            st.execute("PRAGMA foreign_keys = ON;");

            st.execute("""
                CREATE TABLE IF NOT EXISTS PARKING (
                    id        INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre    TEXT NOT NULL UNIQUE,
                    direccion TEXT
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS PLANTA (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    parking_id INTEGER NOT NULL,
                    numero     INTEGER NOT NULL,
                    UNIQUE (parking_id, numero),
                    FOREIGN KEY (parking_id) REFERENCES PARKING(id)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS PLAZA (
                    id        INTEGER PRIMARY KEY AUTOINCREMENT,
                    planta_id INTEGER NOT NULL,
                    numero    INTEGER NOT NULL,
                    tipo      TEXT,
                    UNIQUE (planta_id, numero),
                    FOREIGN KEY (planta_id) REFERENCES PLANTA(id)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS COCHE (
                    matricula TEXT PRIMARY KEY,
                    marca     TEXT NOT NULL,
                    modelo    TEXT NOT NULL,
                    color     TEXT
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS RESERVA (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    matricula    TEXT NOT NULL,
                    plaza_id     INTEGER NOT NULL,
                    fecha_inicio TEXT NOT NULL,
                    fecha_fin    TEXT NOT NULL,
                    estado       TEXT NOT NULL CHECK (
                                   estado IN ('PENDIENTE','EN_CURSO','FINALIZADA')
                                 ),
                    FOREIGN KEY (matricula) REFERENCES COCHE(matricula)
                        ON UPDATE CASCADE
                        ON DELETE RESTRICT,
                    FOREIGN KEY (plaza_id)  REFERENCES PLAZA(id)
                        ON UPDATE CASCADE
                        ON DELETE RESTRICT
                );
            """);

            System.out.println("Base de datos de parking inicializada correctamente.");

        } catch (SQLException e) {
            System.err.println("* Error creando tablas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    // COCHES
    
    public boolean guardarCoche(Coche c) {
        String sql = """
            INSERT INTO COCHE (matricula, marca, modelo, color)
            VALUES (?, ?, ?, ?)
            ON CONFLICT(matricula) DO UPDATE SET
              marca = excluded.marca,
              modelo = excluded.modelo,
              color = excluded.color;
        """;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getMatricula());
            ps.setString(2, c.getMarca());
            ps.setString(3, c.getModelo());
            ps.setString(4, c.getColor() != null ? c.getColor().name() : null);

            return ps.executeUpdate() >= 1;

        } catch (SQLException e) {
            System.err.format("* Error guardando coche '%s': %s\n", c.getMatricula(), e.getMessage());
            return false;
        }
    }

   
    public Coche getCoche(String matricula) {
        String sql = "SELECT matricula, marca, modelo, color FROM COCHE WHERE matricula = ?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String mat   = rs.getString("matricula");
                String marca = rs.getString("marca");
                String modelo= rs.getString("modelo");
                String col   = rs.getString("color");

                Color color = null;
                if (col != null) {
                    try { color = Color.valueOf(col); } catch (IllegalArgumentException ignored) {}
                }
                return new Coche(mat, marca, modelo, color);
            }

        } catch (SQLException e) {
            System.err.format("* Error obteniendo coche '%s': %s\n", matricula, e.getMessage());
        }
        return null;
    }

    
    public List<Coche> loadCoches() {
        List<Coche> lista = new ArrayList<>();
        String sql = "SELECT matricula, marca, modelo, color FROM COCHE";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String mat   = rs.getString("matricula");
                String marca = rs.getString("marca");
                String modelo= rs.getString("modelo");
                String col   = rs.getString("color");

                Color color = null;
                if (col != null) {
                    try { color = Color.valueOf(col); } catch (IllegalArgumentException ignored) {}
                }
                lista.add(new Coche(mat, marca, modelo, color));
            }

        } catch (SQLException e) {
            System.err.format("* Error cargando coches: %s\n", e.getMessage());
        }
        return lista;
    }

    
    // RESERVAS
 

   
    private String toDB(LocalDateTime dt) {
        return dt.format(FMT_DB);
    }

 
    private LocalDateTime fromDB(String txt) {
        return LocalDateTime.parse(txt, FMT_DB);
    }

   
    public boolean insertarReserva(Reserva r, int plazaId, String estado) {
        String sql = """
            INSERT INTO RESERVA (matricula, plaza_id, fecha_inicio, fecha_fin, estado)
            VALUES (?, ?, ?, ?, ?);
        """;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getCoche().getMatricula());
            ps.setInt(2, plazaId);
            ps.setString(3, toDB(r.getFechaInicio()));
            ps.setString(4, toDB(r.getFechaFin()));
            ps.setString(5, estado);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.format("* Error insertando reserva de '%s': %s\n",
                    r.getCoche().getMatricula(), e.getMessage());
            return false;
        }
    }

    
    public List<Reserva> loadReservasPorMatricula(String matricula, PlazaResolver plazaResolver) {
        List<Reserva> lista = new ArrayList<>();

        String sql = """
            SELECT id, plaza_id, fecha_inicio, fecha_fin, estado
            FROM RESERVA
            WHERE matricula = ?
            ORDER BY fecha_inicio;
        """;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();

            // Obtenemos el coche una sola vez
            Coche coche = getCoche(matricula);
            if (coche == null) {
                return lista;
            }

            while (rs.next()) {
                int plazaId        = rs.getInt("plaza_id");
                String inicioTxt   = rs.getString("fecha_inicio");
                String finTxt      = rs.getString("fecha_fin");
                String estado      = rs.getString("estado");

                LocalDateTime inicio = fromDB(inicioTxt);
                LocalDateTime fin    = fromDB(finTxt);

                Plaza plaza = plazaResolver.obtenerPlazaPorId(plazaId);

                Reserva r = new Reserva(coche, plaza, inicio, fin);
                

                lista.add(r);
            }

        } catch (SQLException e) {
            System.err.format("* Error cargando reservas de '%s': %s\n", matricula, e.getMessage());
        }
        return lista;
    }

    
    public boolean borrarReserva(long idReserva) {
        String sql = "DELETE FROM RESERVA WHERE id = ?";

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idReserva);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.format("* Error borrando reserva %d: %s\n", idReserva, e.getMessage());
            return false;
        }
    }

   
   
    public interface PlazaResolver {
        Plaza obtenerPlazaPorId(int idPlaza);
    }
}
