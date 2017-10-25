package accesoDatos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Statement;
import java.util.HashMap;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

/*
 * Todas los accesos a datos implementan la interfaz de Datos
 */

public class FicherosTexto implements I_Acceso_Datos {

	File fDis; // FicheroDispensadores
	File fDep; // FicheroDepositos

	public FicherosTexto() {
		System.out.println("ACCESO A DATOS - FICHEROS DE TEXTO");
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {

		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();

		fDep = new File("Ficheros/datos/depositos.txt");

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(fDep));
			String text = null;

			while ((text = reader.readLine()) != null) {

				String[] datosaux = text.split(";");
				Deposito deposito;
				int clave = Integer.parseInt(datosaux[1]);
				deposito = new Deposito(datosaux[0], clave, Integer.parseInt(datosaux[2]));

				depositosCreados.put(clave, deposito);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return depositosCreados;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {

		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();

		fDis = new File("Ficheros/datos/dispensadores.txt");

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(fDis));
			String text = null;

			while ((text = reader.readLine()) != null) {

				String[] datosaux = text.split(";");
				Dispensador dispensador;
				dispensador = new Dispensador(datosaux[0], datosaux[1], Integer.parseInt(datosaux[2]),
						Integer.parseInt(datosaux[3]));

				dispensadoresCreados.put(datosaux[0], dispensador);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dispensadoresCreados;

	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {

		boolean todoOK = true;

		
		PrintWriter pw = null;
		try {
			fDep = new File("Ficheros/datos/depositos.txt");
			pw = new PrintWriter(fDep);
			
			for (Integer key : depositos.keySet()) {

				Deposito value = depositos.get(key);

				pw.println(value.getNombreMoneda() + ";" + value.getValor()
						+ ";" + value.getCantidad());
				


			}

			
		}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		
		if (fDep != null) {
			pw.close();
			
		}
	

		

		return todoOK;

	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {

		boolean todoOK = true;
		
		PrintWriter pw = null;
		try {
			fDep = new File("Ficheros/datos/dispensadores.txt");
			pw = new PrintWriter(fDep);
			
			for (String key : dispensadores.keySet()) {

				Dispensador value = dispensadores.get(key);

			
				 pw.println(key+";"+value.getNombreProducto()+";"+value.getPrecio() +";"+value.getCantidad());
				


			}

			
		}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		
		if (fDep != null) {
			pw.close();
			
		}

		return todoOK;
	}

} // Fin de la clase