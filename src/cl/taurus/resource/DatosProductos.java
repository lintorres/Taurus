package cl.taurus.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.print.PrintException;

import com.itextpdf.text.DocumentException;

import cl.taurus.dao.ConexionHelpcomImpl;
import cl.taurus.dao.ConexionPratImpl;
import cl.taurus.dao.CreaPDF;

public class DatosProductos {

	public DatosProductos() {
		// TODO Auto-generated constructor stub
	}
	public  void capturarDatosProductos(String precio,int suc_id,String pro_codigo_plu, int emp_id, String pro_nombre, String server,String bd, String usuario, String pass, String prv_rut, double venta,double stockpro, int minimo, int maximo) throws ClassNotFoundException, SQLException, IOException {
		
		ConexionPratImpl consql= new ConexionPratImpl(server,bd,usuario,pass);
		//double cantidadMensual=consql.getCantidadVentasMensual(pro_codigo_plu);
		
		ConexionHelpcomImpl conexionGeneral= new ConexionHelpcomImpl();
		
		ArrayList<Integer>  item=conexionGeneral.prvExiste(prv_rut);
		
		if (item.get(0)==0) {
			
			ArrayList<String> datos_prv=consql.getDatosPrv(pro_codigo_plu, prv_rut);
			if(!datos_prv.get(0).equalsIgnoreCase("")) {
				conexionGeneral.agregarDatosPrv(prv_rut,datos_prv.get(1), datos_prv.get(3), datos_prv.get(2), datos_prv.get(4), datos_prv.get(0));
			}
		} 
		
		int prv_ids=0;
		if(item.get(1)==0) {
			prv_ids=1;
		}else {
			prv_ids=item.get(1);
		}
		
		//CalculoInventario calculos= new CalculoInventario();
       // calculos.CalcularDatos(precio,pro_codigo_plu,cantidadMensual,prv_ids, pro_nombre,suc_id,server,bd,usuario,pass,prv_rut);
        
		//CONEXION CENTRAL HELPCOM
		//ConexionHelpcomImpl conexion2= new ConexionHelpcomImpl();
		double pro=conexionGeneral.getPronostico(pro_codigo_plu, suc_id);
		double pronostico_anterior;
		//if(pro==0.0) {
			pronostico_anterior=venta;
		/**}else {
			pronostico_anterior=pro;
		}**/
		double stock=stockpro;
		double pronostico=pronostico_anterior+0.1*(venta-pronostico_anterior);
		int dias_habiles=24;
		double demanda_media=pronostico/dias_habiles;
		//conexion2.getDiasReposicion(pro_codigo_plu, suc_id, prv_rut);//los dias de reposicion 
		int dias_reposicion=15;
		double inv_seguridad=0.0;

		//double [] minMax=conexion.getConsumoMaximoMinimo(pro_codigo_plu, "", cantidad);//maximo y min por producto
		String [] moda=consql.getModaVentas(pro_codigo_plu, "");
		
		DecimalFormat formato1 = new DecimalFormat("#.##");
		double punto_reorden=(demanda_media*dias_reposicion)+(minimo*dias_reposicion);
		//System.out.println("Punto reorden "+punto_reorden);

		//System.out.println(" Punto reorden formateado "+(formato1.format(punto_reorden)).replaceAll(",", "."));
		double puntoReorden=Double.parseDouble((formato1.format(punto_reorden)).replaceAll(",", "."));
		
		
		if(moda[0].equalsIgnoreCase("true")) {
			inv_seguridad=punto_reorden+(Double.parseDouble(moda[1])-1);
		}
		
		double inventarioMaximo=minimo*dias_reposicion+maximo*dias_reposicion;
		
	    double cantidad_reorden=inventarioMaximo-(minimo*dias_reposicion);
	    
	    boolean existeProducto=conexionGeneral.getProductoSerHelpcom(pro_codigo_plu, suc_id);
	    double precio_producto=Double.parseDouble(precio);
	    if(existeProducto==true) {
	    	System.out.println("ACTUALIZAR PRODUCTO");
	    	conexionGeneral.updateDatosGeneralesProducto(precio_producto,pro_codigo_plu, suc_id, prv_ids, pro_nombre, venta, minimo, maximo,stock, inv_seguridad, cantidad_reorden, pronostico, demanda_media, inventarioMaximo, puntoReorden);
	    }else {
	    	System.out.println("CREAR PRODUCTO");
	    	conexionGeneral.agregarDatosGeneralesProductoSuc1(precio_producto,pro_codigo_plu, suc_id, prv_ids,pro_nombre, venta,minimo,maximo,stock,inv_seguridad,cantidad_reorden,pronostico,demanda_media,inventarioMaximo, puntoReorden);
	    }
        
        consql.cerrarConexion();
		
	}
    public  void capturarDatosProductosThread(int suc_id,String pro_codigo_plu, int emp_id, String pro_nombre, String server,String bd, String usuario, String pass) throws ClassNotFoundException, SQLException, IOException, DocumentException, PrintException {
		
		ConexionPratImpl consql= new ConexionPratImpl(server,bd,usuario,pass);
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
		
		//System.out.println("que sale muchacha "+consql.getDatosBE());
		Date fecha = new Date();
		//System.out.println("Fecha:: "+fecha);
		
		double stockActual=consql.getStockProducto(pro_codigo_plu);
		
		ConexionHelpcomImpl consql2= new ConexionHelpcomImpl();
		consql2.actualizarStock(stockActual, pro_codigo_plu, suc_id);
		double cantidadReorden=consql2.getPuntoReorden(pro_codigo_plu, suc_id);
		double riesgo=cantidadReorden-10;
		
		int oc_asociada=consql2.existeBackOrden(pro_codigo_plu);
		
		if(oc_asociada!=0) {
			consql2.updateBackOrder(pro_codigo_plu, suc_id);
		}else {
			consql2.updateBackOrderNo(pro_codigo_plu, suc_id);
		}
		
		if(stockActual<0.0) {
			System.out.println("PRODUCTO REQUIERE REVISION DE INVENTARIO. \n"
					+ "PRODUCTO POSEE STOCK NEGATIVO.");
				consql2.updateEstadoRequiereRevision(pro_codigo_plu, suc_id);
			
		}else {
		
			if(stockActual>0 && cantidadReorden>0){
				if(stockActual<=cantidadReorden){
					System.out.println("PRODUCTO REQUIERE ORDEN DE COMPRA.");
					consql2.updateEstadoRequiereCompra(pro_codigo_plu, suc_id);
				}else if(riesgo<=cantidadReorden) {
					System.out.println("PRODUCTO REQUIERE ORDEN DE COMPRA PRONTAMENTE.");
					consql2.updateEstadoRequiereCompraRiesgo(pro_codigo_plu, suc_id);
				}
			}else if(cantidadReorden<=0.0 || cantidadReorden<=0){
				System.out.println("PRODUCTO NO TIENE CANTIDAD REORDEN REVISAR PRODUCT0: "+pro_codigo_plu);	
				consql2.updateEstadoRequiereRevisionPtoReorden(pro_codigo_plu, suc_id);
				
			}else if(stockActual>0 && cantidadReorden==0.0){
				//consql2.updateEstadoRequiereCompraRiesgo(pro_codigo_plu, suc_id);
				System.out.println("PRODUCTO NO TIENE CANTIDAD REORDEN REVISAR PRODUCT0: "+pro_codigo_plu);	
				consql2.updateEstadoRequiereRevisionPtoReorden(pro_codigo_plu, suc_id);
			}else {
				consql2.updateEstadoNormal(pro_codigo_plu, suc_id);
			}
			
		}
		
        
		
	}
}
