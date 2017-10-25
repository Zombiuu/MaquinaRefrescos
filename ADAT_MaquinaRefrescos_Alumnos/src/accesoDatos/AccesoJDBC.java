package accesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.sun.javafx.collections.MappingChange.Map;

import auxiliares.LeeProperties;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class AccesoJDBC implements I_Acceso_Datos {

	private String driver, urlbd, user, password; // Datos de la conexion
	private Connection conn1;

	public AccesoJDBC() {
		System.out.println("ACCESO A DATOS - Acceso JDBC");

		try {
			HashMap<String, String> datosConexion;

			LeeProperties properties = new LeeProperties("Ficheros/config/accesoJDBC.properties");
			datosConexion = properties.getHash();

			driver = datosConexion.get("driver");
			urlbd = datosConexion.get("urlbd");
			user = datosConexion.get("user");
			password = datosConexion.get("password");
			conn1 = null;

			Class.forName(driver);
			conn1 = DriverManager.getConnection(urlbd, user, password);
			if (conn1 != null) {
				System.out.println("Conectado a la base de datos");
			}

		} catch (ClassNotFoundException e1) {
			System.out.println("ERROR: No Conectado a la base de datos. No se ha encontrado el driver de conexion");
			// e1.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println("ERROR: No se ha podido conectar con la base de datos");
			System.out.println(e.getMessage());
			// e.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		}
	}

	public int cerrarConexion() {
		try {
			conn1.close();
			System.out.println("Cerrada conexion");
			return 0;
		} catch (Exception e) {
			System.out.println("ERROR: No se ha cerrado corretamente");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {

		HashMap<Integer, Deposito> hashDepositos = new HashMap<Integer, Deposito>();

		String query = "SELECT * from depositos";
		Statement st;
		ResultSet rs;

		try {
			st = conn1.createStatement();
			rs = st.executeQuery(query);
			Deposito deposito;
			while (rs.next()) {
				deposito = new Deposito(rs.getString("nombre"), rs.getInt("valor"), rs.getInt("cantidad"));
				hashDepositos.put(rs.getInt("valor"), deposito);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return hashDepositos;

	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> hashDispensadores = new HashMap<String, Dispensador>();
		String query = "SELECT * from dispensadores";
		Statement st;
		ResultSet rs;
		String clave;

		try {
			st = conn1.createStatement();
			rs = st.executeQuery(query);
			Dispensador dispensador;
			while (rs.next()) {
				clave = rs.getString("clave");
				dispensador = new Dispensador(clave, rs.getString("nombre"), rs.getInt("precio"),
						rs.getInt("cantidad"));
				hashDispensadores.put(clave, dispensador);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return hashDispensadores;
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;
		PreparedStatement pstmt = null;
		String query = "UPDATE `depositos` SET `cantidad` = ? WHERE `depositos`.`valor` = ?";
		try {

			for (Integer key : depositos.keySet()) {

				Deposito value = depositos.get(key);

				pstmt = conn1.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

				pstmt.setInt(1, value.getCantidad());
				pstmt.setInt(2, value.getValor());

				try {
					pstmt.executeUpdate();

				} catch (Exception e) {
					todoOK = false;
				}

			}

		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}

		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;
		PreparedStatement pstmt = null;
		String query = "UPDATE `dispensadores` SET `cantidad` = ? WHERE `dispensadores`.`clave` = ?";
		try {

			for (String key : dispensadores.keySet()) {

				Dispensador value = dispensadores.get(key);

				pstmt = conn1.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

				pstmt.setInt(1, value.getCantidad());
				pstmt.setString(2, value.getClave());

				try {
					pstmt.executeUpdate();

				} catch (Exception e) {
					todoOK = false;
				}

			}

		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}

		return todoOK;
	}

} // Fin de la clase