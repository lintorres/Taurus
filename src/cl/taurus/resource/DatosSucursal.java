package cl.taurus.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cl.taurus.dao.ConexionHelpcomImpl;
import cl.taurus.dao.ConexionPratImpl;


public class DatosSucursal {

	public void getDatosSucursal(int id,String server, String db, String usuario, String pass) throws ClassNotFoundException, SQLException, IOException{
		
		
		
		ConexionPratImpl consql= new ConexionPratImpl(server, db,usuario, pass);
        SimpleDateFormat formatoDeFecha2 = new SimpleDateFormat("yyyy-MM-dd");
		
		//System.out.println("que sale muchacha "+consql.getDatosBE());
		Date fecha = new Date();
        consql.getProductos(id,server, db,usuario, pass);

	}

}
