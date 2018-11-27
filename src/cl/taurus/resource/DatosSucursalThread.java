package cl.taurus.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.print.PrintException;

import com.itextpdf.text.DocumentException;

import cl.taurus.dao.ConexionHelpcomImpl;
import cl.taurus.dao.ConexionPratImpl;
import cl.taurus.dao.CreaPDF;


public class DatosSucursalThread {

	public void getDatosSucursal(int id,String server, String db, String usuario, String pass, String nombre, String ciudad) throws ClassNotFoundException, SQLException, IOException, DocumentException, PrintException{
		
		
		
		ConexionPratImpl consql= new ConexionPratImpl(server, db,usuario, pass);
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
		
		//System.out.println("que sale muchacha "+consql.getDatosBE());
		Date fecha = new Date();
        consql.getProductosThread(id,server, db,usuario, pass);
        
        ConexionHelpcomImpl conSql2= new ConexionHelpcomImpl();
        
        //String pdfTemplate = "/home/lin/Descargas/PlantillaOCDacsa.pdf";
		
		CreaPDF crearPdf= new CreaPDF();
		
		
		String outPDF = crearPdf.crearFicheroMMDDFlex("Reportes/"+id, formatoDeFecha.format(fecha));			
		
		ArrayList<ArrayList<String>> datos = new ArrayList<ArrayList<String>>();
		datos=conSql2.getDatosPDF(id);
		if(datos.get(0).size()>0) {
			ConversorXml conversor= new ConversorXml();
			conversor.getXls(outPDF, datos,formatoDeFecha.format(fecha));
			//crearPdf.creaPDF(pdfTemplate, outPDF, datos,formatoDeFecha.format(fecha), nombre,ciudad);
			
			//System.out.println(outPDF);
			
			Correo correo= new Correo();
			
			correo.enviarCorreo(id, "helpcom0170", "tauruscentralreportes@gmail.com", "yerlin.torres.ii@gmail.com","Reporte"+formatoDeFecha.format(fecha), formatoDeFecha.format(fecha));
			correo.enviarCorreo(id, "helpcom0170", "tauruscentralreportes@gmail.com", "anavarrete@helpcom.cl","Reporte"+formatoDeFecha.format(fecha), formatoDeFecha.format(fecha));
			correo.enviarCorreo(id, "helpcom0170", "tauruscentralreportes@gmail.com", "ibecker@dacsa.cl","Reporte"+formatoDeFecha.format(fecha), formatoDeFecha.format(fecha));
			correo.enviarCorreo(id, "helpcom0170", "tauruscentralreportes@gmail.com", "ccasasdelvalle@dacsa.cl","Reporte"+formatoDeFecha.format(fecha), formatoDeFecha.format(fecha));
			
		}else {
			System.out.println("NO EXISTEN REPORTES DE PRODUCTOS ");
		}
		
	}

}
