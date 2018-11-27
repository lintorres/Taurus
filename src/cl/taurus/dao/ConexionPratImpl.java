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

public class ConexionPratImpl {

    final String SUFIJO = "";
    private ConexionPrat conexion;
    Connection con;

	public ConexionPratImpl(String server, String db, String usuario, String pass) throws ClassNotFoundException, SQLException, IOException {
    	conexion = new ConexionPrat(server,db,usuario,pass);
    	this.con = conexion.getConexion();
	}
	
	/** CONSULTAR estado de un DTE
	 * @param codTipoDoc
	 * @param folio
	 * @return
	 * @throws SQLException
	 */
	
	
	public void getProductos(int suc_id,String server,String bd,String usuario,String pass) throws SQLException, ClassNotFoundException, IOException{

        Statement comando =this.con.createStatement();
        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
        		
        ResultSet registro;
        String res="";
        String pro_precio_total="";
        String prv_rut="";
        String pro_nombre="";
        double venta=0;
        int minimo=0;
        int maximo=0;
        double stock=0;
        DatosProductos datos= new DatosProductos();
        registro = comando.executeQuery("SELECT pro_codigo_plu,pro_nombre_producto,pro_rut,pro_precio_compra,sum(pro_venta) as venta_total,min(pro_minimo) as minimo,pro_saldo,max(pro_maximo) as maximo from tbl_taurus_ventas "
        		+ "group by pro_codigo_plu  order by pro_codigo_plu;");
        while (registro.next()) {
        	    pro_precio_total=registro.getString("pro_precio_compra");
            	res=registro.getString("pro_codigo_plu");
            	prv_rut=registro.getString("pro_rut");
            	pro_nombre=registro.getString("pro_nombre_producto");
            	venta=registro.getDouble("venta_total");
            	minimo=registro.getInt("minimo");
            	maximo=registro.getInt("maximo");
            	stock=registro.getDouble("pro_saldo");
            	datos.capturarDatosProductos(pro_precio_total,suc_id,res, 1, pro_nombre,server,bd,usuario, pass, prv_rut,venta,stock,minimo,maximo);//prv_rut ver proveedor
        }
        
    }
	public void getProductosThread(int suc_id,String server,String bd,String usuario,String pass) throws SQLException, ClassNotFoundException, IOException, DocumentException, PrintException{

        Statement comando =this.con.createStatement();
        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
        		
        ResultSet registro;
        String res="";
        String prv_rut="";
        String pro_nombre="";
        DatosProductos datos= new DatosProductos();
        registro = comando.executeQuery("SELECT distinct pro_codigo_plu,pro_ultimo_proveedor,pro_nombre_producto FROM cci_productos WHERE pro_activo='SI' ORDER BY pro_codigo_plu ASC;");
        while (registro.next()) {
            	res=registro.getString("pro_codigo_plu");
            	prv_rut=registro.getString("pro_ultimo_proveedor");
            	pro_nombre=registro.getString("pro_nombre_producto");
            	datos.capturarDatosProductosThread(suc_id,res, 1, pro_nombre,server,bd,usuario, pass);//prv_rut ver proveedor
        }
        
    }
	
	public double getCantidadVentasMensual(String pro_codigo_plu) throws SQLException, ClassNotFoundException, IOException{

        Statement comando =this.con.createStatement();
        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
        		
        ResultSet registro;
        String fecha="",doc="";
        Double cantidad=0.0;
        String contenedor="";

        registro = comando.executeQuery("SELECT sum(cantidad_total) as cantidad_mensual from (SELECT Fecha,sum(Cantidad) as cantidad_total from (SELECT fac_fecha AS Fecha,'FAC' AS DOC, IFNULL(SUM(fam_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_factura INNER JOIN ven_factura_mov USING(tdo_id,fac_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  fac_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  UNION ALL\n" + 
        		"\n" + 
        		"  SELECT bol_fecha AS Fecha,'BOL' AS DOC, IFNULL(SUM(bom_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_boleta INNER JOIN ven_boleta_mov b USING(tdo_id,bol_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  bol_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  UNION ALL\n" + 
        		"\n" + 
        		"  SELECT ncr_fecha AS Fecha,'NCR' AS DOC, IFNULL(-SUM(ncm_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_nota_credito INNER JOIN ven_nota_credito_mov b USING(tdo_id,ncr_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  ncr_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  ORDER BY Fecha,DOC)temp_datos group by Fecha order by cantidad_total desc) temp_mensual;");
        while (registro.next()) {
            	cantidad=registro.getDouble("cantidad_mensual");   	
        }
        
        return cantidad;
        
        
        
       // this.agregarDatosGeneralesProductoSuc1(pro_codigo_plu,1,prv_rut, cantidad);
        
    }
	
	public double getStockProducto(String pro_codigo_plu) throws SQLException, ClassNotFoundException, IOException{

        Statement comando =this.con.createStatement();
        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
        		
        ResultSet registro;
        String fecha="",doc="";
        double cantidad=0.0;
        String contenedor="";

        registro = comando.executeQuery("SELECT (pro_entradas-pro_salidas-pro_mermas-pro_ajustes) as stock from cci_productos where pro_codigo_plu="+pro_codigo_plu+"");
        while (registro.next()) {
            	cantidad=registro.getDouble("stock");   	
        }
        
        return cantidad;
        
        
        
       // this.agregarDatosGeneralesProductoSuc1(pro_codigo_plu,1,prv_rut, cantidad);
        
	}
	
	
	
	
	public double[] getConsumoMaximoMinimo(String pro_codigo_plu,String mes, double venta_maxima) throws SQLException, ClassNotFoundException, IOException{

        Statement comando =this.con.createStatement();
        Statement comando2 =this.con.createStatement();
        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
        		
        ResultSet registro;
        ResultSet minimo_valor;
        String doc="";
        double cantidad=0.0;
        double cantidad_minima=0.0;
        String contenedor="";
        double [] cm= new double[2];

        registro = comando.executeQuery("SELECT Fecha,sum(Cantidad) as cantidad_total from (SELECT fac_fecha AS Fecha,'FAC' AS DOC, IFNULL(SUM(fam_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_factura INNER JOIN ven_factura_mov USING(tdo_id,fac_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  fac_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		" \n" + 
        		"\n" + 
        		"  UNION ALL\n" + 
        		"\n" + 
        		" \n" + 
        		"\n" + 
        		"  SELECT bol_fecha AS Fecha,'BOL' AS DOC, IFNULL(SUM(bom_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_boleta INNER JOIN ven_boleta_mov b USING(tdo_id,bol_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  bol_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		" \n" + 
        		"\n" + 
        		"  UNION ALL\n" + 
        		"\n" + 
        		" \n" + 
        		"\n" + 
        		"  SELECT ncr_fecha AS Fecha,'NCR' AS DOC, IFNULL(-SUM(ncm_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_nota_credito INNER JOIN ven_nota_credito_mov b USING(tdo_id,ncr_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  ncr_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  ORDER BY Fecha,DOC)temp_datos group by Fecha order by cantidad_total desc limit 3;");
        while (registro.next()) {
            	cantidad=registro.getDouble("cantidad_total");
            	contenedor+=cantidad+";";
        }
        
        minimo_valor=comando2.executeQuery("SELECT MIN(cantidad_total) as minimo from (SELECT Fecha,sum(Cantidad) as cantidad_total from (SELECT fac_fecha AS Fecha,'FAC' AS DOC, IFNULL(SUM(fam_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_factura INNER JOIN ven_factura_mov USING(tdo_id,fac_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  fac_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  UNION ALL\n" + 
        		"\n" + 
        		"  SELECT bol_fecha AS Fecha,'BOL' AS DOC, IFNULL(SUM(bom_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_boleta INNER JOIN ven_boleta_mov b USING(tdo_id,bol_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  bol_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  UNION ALL\n" + 
        		"\n" + 
        		"  SELECT ncr_fecha AS Fecha,'NCR' AS DOC, IFNULL(-SUM(ncm_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_nota_credito INNER JOIN ven_nota_credito_mov b USING(tdo_id,ncr_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  ncr_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  ORDER BY Fecha,DOC)temp_datos group by Fecha)temp_minimo;");
        
        while (minimo_valor.next()) {
        	cantidad_minima=minimo_valor.getDouble("minimo");
           }
       
        String [] valores=contenedor.split(";");
        if(!valores[0].equalsIgnoreCase("")) {
        	if(valores.length>1) {
        		for (int i = 0; i < valores.length; i++) {
         			double promedio=Double.parseDouble(valores[i])/venta_maxima;
         			
         			if(promedio<0.3) {
         				cm[0]=Double.parseDouble(valores[i]);
         				break;
         			}
         		}
        	}else {
        		cm[0]=Double.parseDouble(valores[0]);
        	}
        	 
        }
       
        cm[1]=cantidad_minima;
        //System.out.println("codigo plu "+pro_codigo_plu+" maximo "+cm[0]+ " minimo  "+cm[1]);
        
        return cm;
        
    }
	public String [] getModaVentas(String pro_codigo_plu,String mes) throws SQLException, ClassNotFoundException, IOException{

        Statement comando =this.con.createStatement();
        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
        		
        ResultSet registro;
        ResultSet minimo_valor;
        String doc="";
        double cantidad=0.0;
        double cantidad_minima=0.0;
        double repeticiones=0.0;
        String contenedor="";
        double [] cm= new double[2];
        String [] respuesta=new String[2];

        registro = comando.executeQuery("SELECT cantidad_total, count(*) as repeticiones from ( select Fecha,sum(Cantidad) as cantidad_total from (SELECT fac_fecha AS Fecha,'FAC' AS DOC, IFNULL(SUM(fam_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_factura INNER JOIN ven_factura_mov USING(tdo_id,fac_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  fac_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  UNION ALL\n" + 
        		"\n" + 
        		"  SELECT bol_fecha AS Fecha,'BOL' AS DOC, IFNULL(SUM(bom_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_boleta INNER JOIN ven_boleta_mov b USING(tdo_id,bol_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  bol_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  UNION ALL\n" + 
        		"\n" + 
        		"  SELECT ncr_fecha AS Fecha,'NCR' AS DOC, IFNULL(-SUM(ncm_cantidad),0) AS Cantidad\n" + 
        		"\n" + 
        		"  FROM ven_nota_credito INNER JOIN ven_nota_credito_mov b USING(tdo_id,ncr_numero) INNER JOIN cci_productos USING(pro_codigo_plu)\n" + 
        		"\n" + 
        		"  WHERE cli_id NOT IN (SELECT cli_id FROM ven_empresas_relacionadas INNER JOIN ven_clientes ON cli_rut=emr_rut WHERE ert_id=1) AND\n" + 
        		"\n" + 
        		"  ncr_fecha BETWEEN '2018-05-01' AND '2018-05-31' AND pro_codigo_plu="+pro_codigo_plu+"\n" + 
        		"\n" + 
        		"  GROUP BY Fecha\n" + 
        		"\n" + 
        		"  ORDER BY Fecha,DOC)temp_datos group by Fecha) temp_dato group by cantidad_total order by repeticiones desc limit 2");
        
        String repet="";
        while (registro.next()) {
            	cantidad=registro.getDouble("cantidad_total");
            	repeticiones=registro.getDouble("repeticiones");
            	contenedor+=cantidad+";";
            	repet+=repeticiones+";";
        }
       
        
        if(contenedor.equalsIgnoreCase(";")) {
        	String [] valores=contenedor.split(";");
            String [] rep=repet.split(";");
        	for (int i = 0; i < rep.length; i++) {
    			if(Double.parseDouble(valores[i])>3 && Double.parseDouble(rep[i])>4) {
    				respuesta[0]="true";
    				respuesta[1]=valores[i];
    				break;
    			}else {
    				respuesta[0]="false";
    			}
    		}
        }else {
        	respuesta[0]="false";
        }
        
        return respuesta;
    }
public ArrayList<String> getDatosPrv(String pro_codigo_plu, String prv_rut) throws SQLException {
		
		
		//Connection conexionCH=this.conexionCentral.getConexion();
		Statement comando =con.createStatement();
	        //CrearEnvioConsumoFolio2 crearConsumoFolio= new CrearEnvioConsumoFolio2();
		
	        		
	        ResultSet registro;
	        String plu="",dias="",nombre="",giro="",direccion="",ciudad="",dep="",fam ="";
	        registro = comando.executeQuery("SELECT prv_dias_reposicion,prv_nombre,prv_giro,prv_direccion,prv_ciudad, dep_id, fam_id " + 
	        		"FROM cci_productos INNER JOIN cci_mix_productos USING (pro_codigo_plu) INNER\r\n" + 
	        		"JOIN cci_proveedores USING (prv_rut) where pro_codigo_plu='"+pro_codigo_plu+"' and prv_rut='"+prv_rut+"'");
	        while (registro.next()) {
	            	//plu=registro.getString("pro_codigo_plu");
	            	dias=registro.getString("prv_dias_reposicion");
	            	nombre=registro.getString("prv_nombre");
	            	giro=registro.getString("prv_giro");
	            	direccion=registro.getString("prv_direccion");
	            	ciudad=registro.getString("prv_ciudad");
	            	dep=registro.getString("dep_id");
	            	fam=registro.getString("fam_id");
	        }
	        
	        ArrayList<String> dato=new ArrayList<>();
	        //dato.add(plu);
	        dato.add(dias);
	        dato.add(nombre);
	        dato.add(giro);
	        dato.add(direccion);
	        dato.add(ciudad);
	        dato.add(dep);
	        dato.add(fam);
	        
	        
		return dato;
    	
    }

	public void cerrarConexion() throws SQLException {
		this.con.close();
	}


}