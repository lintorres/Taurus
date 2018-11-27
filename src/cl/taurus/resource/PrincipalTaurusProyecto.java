package cl.taurus.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import cl.taurus.dao.ConexionHelpcomImpl;

public class PrincipalTaurusProyecto {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
      ConexionHelpcomImpl conexionGeneral= new ConexionHelpcomImpl();
		
      ArrayList<ArrayList<String>> accesos=conexionGeneral.getDatosSucursal();
      
      for (int i = 0; i < accesos.get(0).size(); i++) {
    	  System.out.println("\nSUCURSAL "+accesos.get(0).get(i)+" NOMBRE: "+accesos.get(1).get(i));
          DatosSucursal datosSucursal= new DatosSucursal();
          datosSucursal.getDatosSucursal(Integer.parseInt(accesos.get(0).get(i)),accesos.get(2).get(i), accesos.get(3).get(i), accesos.get(4).get(i), accesos.get(5).get(i));
	      conexionGeneral.cerrarConexion();
      }
		
	
	}

}
