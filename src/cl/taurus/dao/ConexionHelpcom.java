/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.taurus.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


/**
 *
 * @author Mauricio Rodriguez
 */
public class ConexionHelpcom {

    private static Connection conexion;
    static String bd = "";
    static String user = "";
    static String password = "";
    static String server = "";
    Properties propiedades = new Properties();

    //static String rut="";
    static boolean debug = true;

    public ConexionHelpcom(String datoServer,String baseDato,String usuario,String pass) throws ClassNotFoundException, SQLException, IOException {

    	server ="jdbc:mysql://"+datoServer;
        bd = baseDato;
        user = usuario;
        password = pass;

       
        try {
        	 Class.forName("com.mysql.jdbc.Driver");

             conexion = DriverManager.getConnection(server+"/"+bd, user, password);
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
