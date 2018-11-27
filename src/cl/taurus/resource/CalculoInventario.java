package cl.taurus.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import cl.taurus.dao.ConexionHelpcomImpl;
import cl.taurus.dao.ConexionPratImpl;

public class CalculoInventario {

	public CalculoInventario() {
		// TODO Auto-generated constructor stub
	}
	
	public void CalcularDatos(String precio,String pro_codigo_plu,double cantidad, int prv_id, String pro_nombre_producto,int suc_id,String server, String bd, String usuario, String pass, String prv_rut) throws ClassNotFoundException, SQLException, IOException {
		ConexionPratImpl conexion= new ConexionPratImpl(server, bd,usuario, pass);//CONECCION PRAT
		//CONEXION CENTRAL HELPCOM
		ConexionHelpcomImpl conexion2= new ConexionHelpcomImpl();
		conexion2.getPronostico(pro_codigo_plu, suc_id);
		double pronostico_anterior=cantidad;
		double stock=conexion.getStockProducto(pro_codigo_plu);
		double pronostico=pronostico_anterior+0.1*(cantidad-pronostico_anterior);
		int dias_habiles=24;
		double demanda_media=pronostico/dias_habiles;
		//conexion2.getDiasReposicion(pro_codigo_plu, suc_id, prv_rut);//los dias de reposicion 
		int dias_reposicion=15;
		double inv_seguridad=0.0;

		double [] minMax=conexion.getConsumoMaximoMinimo(pro_codigo_plu, "", cantidad);//maximo y min por producto
		String [] moda=conexion.getModaVentas(pro_codigo_plu, "");
		
		DecimalFormat formato1 = new DecimalFormat("#.##");
		double punto_reorden=(demanda_media*dias_reposicion)+(minMax[1]*dias_reposicion);
		//System.out.println("Punto reorden "+punto_reorden);

		//System.out.println(" Punto reorden formateado "+(formato1.format(punto_reorden)).replaceAll(",", "."));
		double puntoReorden=Double.parseDouble((formato1.format(punto_reorden)).replaceAll(",", "."));
		
		
		if(moda[0].equalsIgnoreCase("true")) {
			inv_seguridad=punto_reorden+(Double.parseDouble(moda[1])-1);
		}
		
		double inventarioMaximo=minMax[1]*dias_reposicion+minMax[0]*dias_reposicion;
		
	    double cantidad_reorden=inventarioMaximo-(minMax[1]*dias_reposicion);
	    
	    
	    boolean existeProducto=conexion2.getProductoSerHelpcom(pro_codigo_plu, suc_id);
	    double precio_producto=Double.parseDouble(precio);
	    if(existeProducto==true) {
	    	System.out.println("ACTUALIZAR PRODUCTO");
	    	conexion2.updateDatosGeneralesProducto(precio_producto,pro_codigo_plu, suc_id, prv_id, pro_nombre_producto, cantidad, minMax[0], minMax[1],stock, inv_seguridad, cantidad_reorden, pronostico, demanda_media, inventarioMaximo, puntoReorden);
	    }else {
	    	System.out.println("CREAR PRODUCTO");
	    	conexion2.agregarDatosGeneralesProductoSuc1(precio_producto,pro_codigo_plu, suc_id, prv_id,pro_nombre_producto, cantidad,minMax[0],minMax[1],cantidad,inv_seguridad,cantidad_reorden,pronostico,demanda_media,inventarioMaximo, puntoReorden);
	    }
	 
	}

}
