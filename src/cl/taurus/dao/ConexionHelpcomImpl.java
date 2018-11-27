/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.taurus.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.print.PrintException;

import org.hibernate.result.Output;

import oracle.jdbc.oracore.TDSPatch;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.mysql.jdbc.Blob;
import com.mysql.jdbc.PreparedStatement;

import cl.taurus.resource.DatosProductos;
import cl.taurus.resource.DatosSucursal;
import cl.taurus.resource.DatosSucursalThread;

public class ConexionHelpcomImpl {

    final String SUFIJO = "";
    private ConexionHelpcom conexion;
    Connection con;

	public ConexionHelpcomImpl() throws ClassNotFoundException, SQLException, IOException {
    	conexion = new ConexionHelpcom("190.13.136.236","taurus","admin_helpcom_db","Helpcom.0170");
    	this.con = conexion.getConexion();
	}
	
	/** CONSULTAR estado de un DTE
	 * @param codTipoDoc
	 * @param folio
	 * @return
	 * @throws SQLException
	 */
	
	
public void agregarDatosGeneralesProductoSuc1(double precio,String pro_codigo_plu, int suc_id, int prv_id,String nombreProducto, double cantidadMensual, double minimo,double Max, double unidadesActuales,double inv_seguridad,double cantidad_reorden,double pronostico,double demanda_media,double inventarioMaximo, double puntoReorden) throws SQLException {
		
		Statement comando = this.con.createStatement();
        String sql = "INSERT INTO productos (id,plu, suc_id, prv_id, nombre,total_unidades_vendidas,"
        		+ "consumo_maximo,consumo_minimo,total_unidades_actual,cantidad_reorden_seguridad,"
        		+ "cantidad_reorden,cantidad_pronostico,demanda_media_diaria,inventario_maximo,"
        		+ "estado_producto_id, punto_cantidad_reorden, precio_compra) VALUES (?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = (PreparedStatement) this.con.prepareStatement(sql);
        statement.setInt(1, 0);
        statement.setInt(2, Integer.parseInt(pro_codigo_plu));
        statement.setInt(3, suc_id);
        statement.setInt(4, prv_id);
        statement.setString(5, nombreProducto);
        statement.setDouble(6, cantidadMensual);
        statement.setDouble(7, Max);
        statement.setDouble(8, minimo);
        statement.setDouble(9, unidadesActuales);
        statement.setDouble(10, inv_seguridad);
        statement.setDouble(11, cantidad_reorden);
        statement.setDouble(12, pronostico);
        statement.setDouble(13, demanda_media);
        statement.setDouble(14, inventarioMaximo);
        statement.setInt(15, 3);
        statement.setDouble(16, puntoReorden);
        statement.setDouble(17, precio);

        //guardarZIP(filenamePDF, tdo_id,folio);//Guardar documentoPDF Binario BLOB

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Base Datos OK");
        }else {
        	System.out.println("NO se ha podido insertar el registro correctamente");
		}
	}
public void agregarDatosPrv(String prv_rut,String prv_nombre,String direccion, String giro,String ciudad,String tiempo_reposicion) throws SQLException {
	
	Statement comando = this.con.createStatement();
    String sql = "INSERT INTO proveedores (id,rut,nombre,direccion,giro,ciudad,tiempo_reposicion_producto) "
    		+ "VALUES (?,?,?,?,?, ?, ?)";

    PreparedStatement statement = (PreparedStatement) this.con.prepareStatement(sql);
    statement.setInt(1, 0);
    statement.setString(2, prv_rut);
    statement.setString(3, prv_nombre);
    statement.setString(4, direccion);
    statement.setString(5, giro);
    statement.setString(6, ciudad);
    statement.setInt(7, Integer.parseInt(tiempo_reposicion));

    //guardarZIP(filenamePDF, tdo_id,folio);//Guardar documentoPDF Binario BLOB

    int rowsInserted = statement.executeUpdate();
    if (rowsInserted > 0) {
        System.out.println("Base Datos OK");
    }else {
    	System.out.println("NO se ha podido insertar el registro correctamente");
	}
}
	
	public void updateDatosGeneralesProducto(double precio,String pro_codigo_plu, int suc_id, int prv_id,String nombreProducto, double cantidadMensual, double minimo,double Max, double unidadesActuales,double inv_seguridad,double cantidad_reorden,double pronostico,double demanda_media,double inventarioMaximo, double puntoReorden) throws SQLException {
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando = this.con.createStatement();
	        comando.executeUpdate("UPDATE productos set total_unidades_vendidas="+cantidadMensual+",prv_id="+prv_id+" ,consumo_maximo =" + Max + ",consumo_minimo=" + minimo + ",precio_compra="+precio+",total_unidades_actual =" + cantidadMensual + ",cantidad_reorden_seguridad =" + inv_seguridad + ",cantidad_reorden =" + cantidad_reorden + ",cantidad_pronostico=" + pronostico + ",demanda_media_diaria =" + demanda_media + ", inventario_maximo="+inventarioMaximo+",nombre='"+nombreProducto+"' , estado_producto_id=3, punto_cantidad_reorden="+puntoReorden+" where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
	    
	}
	
	
	public boolean getProductoSerHelpcom(String pro_codigo_plu, int suc_id) throws SQLException {
		
		
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando =con.createStatement();
	        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
		 boolean existeProducto=false;
	        		
	        ResultSet registro;
	        String res="";
	        String prv_rut="";
	        DatosProductos datos= new DatosProductos();
	        registro = comando.executeQuery("SELECT nombre FROM productos WHERE plu='"+pro_codigo_plu+"' AND suc_id="+suc_id+"");
	        while (registro.next()) {
	            	res=registro.getString("nombre");
	        }
	        
	        if(res.equalsIgnoreCase("")) {
	        	existeProducto=false;
	        }else {
	        	existeProducto=true;
	        }
	        
		return existeProducto;
    	
    }
	public ArrayList<ArrayList<String>> getDatosSucursal() throws SQLException, ClassNotFoundException, IOException {
		
		
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando =con.createStatement();
	        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
		 boolean existeProducto=false;
	        		
	        ResultSet registro;
	        String nombre="";
	        String suc_ip="";
	        String suc_nombre_db="";
	        String suc_usuario_db="";
	        String suc_password_db="";
	        int id;
	        ArrayList<String> ids=new ArrayList<>();
	        ArrayList<String> nombres=new ArrayList<>();
	        ArrayList<String> ips=new ArrayList<>();
	        ArrayList<String> nombresdb=new ArrayList<>();
	        ArrayList<String> usuarios=new ArrayList<>();
	        ArrayList<String> password=new ArrayList<>();
	        
	        ArrayList<ArrayList<String>> accesos=new ArrayList<ArrayList<String>>();
	        
	        registro = comando.executeQuery("SELECT id,nombre,ip_internet, nombre_db, usuario_db, password_db FROM sucursal; ");
	        while (registro.next()) {
	        	    id=registro.getInt("id");
	            	nombre=registro.getString("nombre");
	            	suc_ip=registro.getString("ip_internet");
	            	suc_nombre_db=registro.getString("nombre_db");
	            	suc_usuario_db=registro.getString("usuario_db");
	            	suc_password_db=registro.getString("password_db");
	            	
	           ids.add(id+"");
	           nombres.add(nombre);
	           ips.add(suc_ip);
	           nombresdb.add(suc_nombre_db);
	           usuarios.add(suc_usuario_db);
	           password.add(suc_password_db);
	        
	        }
	        
	        accesos.add(ids);
	        accesos.add(nombres);
	        accesos.add(ips);
	        accesos.add(nombresdb);
	        accesos.add(usuarios);
	        accesos.add(password);
	        
	        return accesos;
	        
    	
    }
     public ArrayList<ArrayList<String>> getDatosSucursalThread() throws SQLException, ClassNotFoundException, IOException, DocumentException, PrintException {
		
		
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando =con.createStatement();
	        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
		 boolean existeProducto=false;
	        		
	        ResultSet registro;
	        String nombre="";
	        String ciu="";
	        String suc_ip="";
	        String suc_nombre_db="";
	        String suc_usuario_db="";
	        String suc_password_db="";
	        int id;
	        
	        ArrayList<String> ids=new ArrayList<>();
	        ArrayList<String> nombres=new ArrayList<>();
	        ArrayList<String> ips=new ArrayList<>();
	        ArrayList<String> nombresdb=new ArrayList<>();
	        ArrayList<String> usuarios=new ArrayList<>();
	        ArrayList<String> password=new ArrayList<>();
	        ArrayList<String> ciudad=new ArrayList<>();
	        
	        ArrayList<ArrayList<String>> accesos=new ArrayList<ArrayList<String>>();
	        
	        registro = comando.executeQuery("SELECT id,nombre,ip_internet, nombre_db, usuario_db, password_db,ciudad FROM sucursal; ");
	        while (registro.next()) {
	        	    id=registro.getInt("id");
	            	nombre=registro.getString("nombre");
	            	suc_ip=registro.getString("ip_internet");
	            	suc_nombre_db=registro.getString("nombre_db");
	            	suc_usuario_db=registro.getString("usuario_db");
	            	suc_password_db=registro.getString("password_db");
	            	ciu=registro.getString("ciudad");
	            	
	               ids.add(id+"");
		           nombres.add(nombre);
		           ips.add(suc_ip);
		           nombresdb.add(suc_nombre_db);
		           usuarios.add(suc_usuario_db);
		           password.add(suc_password_db);
		           ciudad.add(ciu);
		        
		        }
		        
		        accesos.add(ids);
		        accesos.add(nombres);
		        accesos.add(ips);
		        accesos.add(nombresdb);
		        accesos.add(usuarios);
		        accesos.add(password);
		        accesos.add(ciudad);
		        
		        return accesos;
    	
    }
	public double getPuntoReorden(String pro_codigo_plu ,int suc_id) throws SQLException, ClassNotFoundException, IOException{

    Statement comando =this.con.createStatement();
    //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
    		
    ResultSet registro;
    String fecha="",doc="";
    Double cantidad=0.0;
    String contenedor="";

    registro = comando.executeQuery("SELECT punto_cantidad_reorden FROM productos where plu="+pro_codigo_plu+" and suc_id="+suc_id+";");
    while (registro.next()) {
        	cantidad=registro.getDouble("punto_cantidad_reorden");   	
    }
    
    return cantidad;
   // this.agregarDatosGeneralesProductoSuc1(pro_codigo_plu,1,prv_rut, cantidad);
	}
	public ArrayList<ArrayList<String>> getDatosPDF(int suc_id) throws SQLException, ClassNotFoundException, IOException{

	    Statement comando =this.con.createStatement();
	    //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
	    ArrayList<ArrayList<String>> arrayPDFDatos = new ArrayList<ArrayList<String>>();
	    ArrayList<String> pluArray = new ArrayList<String>();
	    ArrayList<String> nombreArray = new ArrayList<String>();
	    ArrayList<String> estado = new ArrayList<String>();
	    ArrayList<String> stock = new ArrayList<String>();
	    ArrayList<String> cantidad = new ArrayList<String>();
	    ResultSet registro;
	    String fecha="",doc="";
	    String contenedor="";
        String plu,nombre,estadoCompra,stockActual,cantidadReorden;
        int i=0;
	    registro = comando.executeQuery("SELECT plu,nombre,total_unidades_actual,punto_cantidad_reorden, descripcion FROM productos p inner join estado_producto e on p.estado_producto_id=e.id where (descripcion='CRITICO' or descripcion='EN RIESGO' OR descripcion='REV INVENTARIO') and suc_id="+suc_id+" limit 10;");
	    while (registro.next()) {
	        	plu=registro.getString("plu"); 
	        	nombre=registro.getString("nombre"); 
	        	estadoCompra=registro.getString("descripcion"); 
	        	stockActual=registro.getString("total_unidades_actual"); 
	        	cantidadReorden=registro.getString("punto_cantidad_reorden"); 
	        	pluArray.add(plu);
	        	nombreArray.add(nombre);
	        	estado.add(estadoCompra);
	        	stock.add(stockActual);
	        	cantidad.add(cantidadReorden);
	    }
	    arrayPDFDatos.add(pluArray);
	    arrayPDFDatos.add(nombreArray);
	    arrayPDFDatos.add(estado);
	    arrayPDFDatos.add(stock);
	    arrayPDFDatos.add(cantidad);
	    return arrayPDFDatos;
	   // this.agregarDatosGeneralesProductoSuc1(pro_codigo_plu,1,prv_rut, cantidad);
		}
	public void actualizarStock(double stock, String pro_codigo_plu, int suc_id) throws SQLException, ClassNotFoundException, IOException{

		Statement comando = this.con.createStatement();
        comando.executeUpdate("UPDATE productos set total_unidades_actual="+stock+" where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
    
		}
	public int existeBackOrden(String pro_codigo_plu) throws SQLException, ClassNotFoundException, IOException{

        Statement comando =this.con.createStatement();
        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
        		
        ResultSet registro;
        String fecha="",doc="";
        int cantidad=0;
        String contenedor="";

        registro = comando.executeQuery("SELECT oc_id FROM orden_compra INNER JOIN orden_compra_mov USING (id) " + 
        		" where plu="+pro_codigo_plu+" and estado=5");
        while (registro.next()) {
            	cantidad=registro.getInt("oc_id");   	
        }
        
        return cantidad;
        
        
        
       // this.agregarDatosGeneralesProductoSuc1(pro_codigo_plu,1,prv_rut, cantidad);
        
    }
	public void updateBackOrder(String pro_codigo_plu, int suc_id) throws SQLException {
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando = this.con.createStatement();
	        comando.executeUpdate("UPDATE productos set backorder='SI' where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
	    
	}
	public void updateBackOrderNo(String pro_codigo_plu, int suc_id) throws SQLException {
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando = this.con.createStatement();
	        comando.executeUpdate("UPDATE productos set backorder='NO' where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
	    
	}
	public void updateEstadoRequiereCompra(String pro_codigo_plu, int suc_id) throws SQLException {
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando = this.con.createStatement();
	        comando.executeUpdate("UPDATE productos set estado_producto_id=1 where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
	    
	}
	public void updateEstadoRequiereCompraRiesgo(String pro_codigo_plu, int suc_id) throws SQLException {
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando = this.con.createStatement();
	        comando.executeUpdate("UPDATE productos set estado_producto_id=2 where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
	    
	}
	public void updateEstadoRequiereRevision(String pro_codigo_plu, int suc_id) throws SQLException {
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando = this.con.createStatement();
	        comando.executeUpdate("UPDATE productos set estado_producto_id=6 where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
	    
	}
	public void updateEstadoNormal(String pro_codigo_plu, int suc_id) throws SQLException {
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando = this.con.createStatement();
	        comando.executeUpdate("UPDATE productos set estado_producto_id=3 where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
	    
	}
	public void updateEstadoRequiereRevisionPtoReorden(String pro_codigo_plu, int suc_id) throws SQLException {
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando = this.con.createStatement();
	        comando.executeUpdate("UPDATE productos set estado_producto_id=7 where plu=" + pro_codigo_plu +" AND suc_id= "+suc_id+"" );
	    
	}
	public double getPronostico(String pro_codigo_plu, int suc_id) throws SQLException {
		
		
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando =con.createStatement();
	        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
		 boolean existeProducto=false;
	        		
	        ResultSet registro;
	        double res=0.0;
	        String prv_rut="";
	        DatosProductos datos= new DatosProductos();
	        registro = comando.executeQuery("SELECT cantidad_pronostico FROM productos WHERE plu='"+pro_codigo_plu+"' AND suc_id="+suc_id+"");
	        while (registro.next()) {
	            	res=registro.getDouble("cantidad_pronostico");
	        }
	        
	        
		return res;
    	
    }

   public ArrayList<Integer>  prvExiste(String prv_rut) throws SQLException, ClassNotFoundException, IOException{

    Statement comando =this.con.createStatement();
    //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
    		
    ResultSet registro;
    String existe="NO";
    int cantidad=0,id=0;
    ArrayList<Integer> datos= new ArrayList<>();
    registro = comando.executeQuery("SELECT count(*) as item,id FROM proveedores WHERE rut='"+prv_rut+"';");
    while (registro.next()) {
        	cantidad=registro.getInt("item");
        	id=registro.getInt("id"); 
    }
    datos.add(cantidad);
    datos.add(id);
    return datos;
    
    
    
   // this.agregarDatosGeneralesProductoSuc1(pro_codigo_plu,1,prv_rut, cantidad);
    
    }
	public void cerrarConexion() throws SQLException {
		this.con.close();
	}


}