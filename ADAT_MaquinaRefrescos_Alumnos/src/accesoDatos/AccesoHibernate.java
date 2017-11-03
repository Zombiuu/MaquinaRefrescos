package accesoDatos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import auxiliares.HibernateUtil;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class AccesoHibernate implements I_Acceso_Datos {

	Session session;

	public AccesoHibernate() {

		HibernateUtil util = new HibernateUtil();

		session = util.getSessionFactory().openSession();

	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {

		HashMap<Integer, Deposito> hashDepositos = new HashMap<Integer, Deposito>();
		Query q = session.createQuery("select depo from Deposito depo");
		List results = q.list();

		Iterator depoIterator = results.iterator();

		while (depoIterator.hasNext()) {
			Deposito depo = (Deposito) depoIterator.next();

			hashDepositos.put(depo.getValor(), depo);

		}

		return hashDepositos;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> hashDispensadores = new HashMap<String, Dispensador>();
		Query q = session.createQuery("select disp from Dispensador disp ");
		List results = q.list();

		Iterator dispIterator = results.iterator();

		while (dispIterator.hasNext()) {
			Dispensador disp = (Dispensador) dispIterator.next();

			hashDispensadores.put(disp.getClave(), disp);

		}

		return hashDispensadores;
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {

		Boolean todoOk = true;
		
		try {
			session.beginTransaction();
			for (int key : depositos.keySet()) {

				session.save(depositos.get(key));
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			todoOk = false;
		}

		return todoOk;

	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		Boolean todoOk = true;
		
		try {
			session.beginTransaction();
			for (String key : dispensadores.keySet()) {

				session.save(dispensadores.get(key));
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			todoOk = false;
		}

		return todoOk;

	}
}
