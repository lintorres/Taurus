package cl.taurus.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.print.PrintException;

import com.itextpdf.text.DocumentException;

import cl.taurus.dao.ConexionHelpcomImpl;
import cl.taurus.dao.CreaPDF;

public class PrincipalThreadTaurus {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, DocumentException, PrintException {
     ConexionHelpcomImpl conexionGeneral= new ConexionHelpcomImpl();
		
     ArrayList<ArrayList<String>> accesos=conexionGeneral.getDatosSucursalThread();
		
     for (int i = 0; i < accesos.get(0).size(); i++) {
   	  System.out.println("\nSUCURSAL "+accesos.get(0).get(i)+" NOMBRE: "+accesos.get(1).get(i));
   	   DatosSucursalThread datosSucursal= new DatosSucursalThread();
       datosSucursal.getDatosSucursal(Integer.parseInt(accesos.get(0).get(i)),accesos.get(2).get(i), accesos.get(3).get(i), accesos.get(4).get(i), accesos.get(5).get(i),accesos.get(1).get(i),accesos.get(6).get(i));
	   conexionGeneral.cerrarConexion();
	   
     }	

	}

}
