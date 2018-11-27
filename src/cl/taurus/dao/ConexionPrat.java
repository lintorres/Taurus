/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.taurus.dao;

import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import org.xml.sax.SAXException;

/**
 * 
 * @author Mauricio Rodriguez
 */
public class ConexionPrat {

	private static Connection conexion;
	static String bd = "";
	static String user = "";
	static String password = "";
	static String server = "";
	Properties propiedades = new Properties();

	// static String rut="";
	static boolean debug = true;

	public ConexionPrat(String datoServer,String baseDato,String usuario,String pass) throws ClassNotFoundException,
			SQLException, IOException {

		// server = propiedades.getProperty("SERVER_SERVER").toString();
		// bd = propiedades.getProperty("BASE_DATOS_SERVER").toString();
		// user = propiedades.getProperty("USUARIO_SERVER").toString();
		// password = propiedades.getProperty("PASSWORD_SERVER").toString();

		server = "jdbc:mysql://"+datoServer;
		bd = baseDato;
		user = usuario;
		password = pass;
		

		//System.out.println("Usuario "+user +" Password "+password+" db "+bd+" server "+server );
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection(server + ":3306/" + bd, user,
					password);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
	}

	public Connection getConexion() {
		return conexion;
	}

	public void cerrar(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				System.out.print("No es posible cerrar la Conexion");
			}
		}
	}

	public void cerrar(java.sql.Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
	}

	public void destruir() {

		if (conexion != null) {
			try {
				conexion.close();
			} catch (Exception e) {
			}
		}
	}
}
