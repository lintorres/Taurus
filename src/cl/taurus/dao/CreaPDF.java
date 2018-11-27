package cl.taurus.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.print.PrintException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BarcodePDF417;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


public class CreaPDF {
	
	public void creaPDF(String pdfTempleStr,String archivoSalida,ArrayList<ArrayList<String>> datos,String fecha, String nombre, String ciudad) throws IOException, DocumentException, PrintException{
        archivoSalida = archivoSalida+"/Reporte"+fecha+".pdf";
        CreaPDF lectorFichero = new CreaPDF();//**

        File a = new File(archivoSalida);
        if (a.exists()) {
        	a.delete();
        }
        PdfReader pdfTemplate = new PdfReader(pdfTempleStr);//Entrada3
        FileOutputStream pdfSalida = new FileOutputStream(archivoSalida);//Salida
        Files.setPosixFilePermissions(Paths.get(archivoSalida), lectorFichero.permisos());//permisos a PDF        
        PdfStamper stamper = new PdfStamper(pdfTemplate, pdfSalida);
        stamper.setFormFlattening(true);

        DecimalFormat formato = new DecimalFormat("#,###.###");

        //Info Emisor InfoEmisor
        String nomEmpresa = new String("Serviteca Dacsa".getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("fecha", fecha);
        String giroEmpresa = new String("Giro".getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("nomReceptor", "MANT Y REP DE VEH AUT");
        String direccionEmisor= new String("Suc Nombre".getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("sucNombre",nombre);
        String comunaEmisor= new String("Ciudad ".getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("ciudad", ciudad);
        int m=1;
			for (int j = 0; j < datos.get(0).size(); j++) {
				stamper.getAcroFields().setField("cod"+m,datos.get(0).get(j));
				stamper.getAcroFields().setField("det"+m,datos.get(1).get(j));
				stamper.getAcroFields().setField("totalGrid"+m,datos.get(2).get(j));
				stamper.getAcroFields().setField("cantidad"+m,datos.get(3).get(j));
				stamper.getAcroFields().setField("dtos"+m,datos.get(4).get(j));
				m++;
			}        
			
		System.out.println(archivoSalida+"/Reporte"+fecha+".pdf");
        PdfContentByte content = stamper.getOverContent(1);  
        stamper.close();
        pdfTemplate.close();
    }
	public String crearFicheroMMDDFlex (String rutaFolder,String fecha) throws IOException{

		String aux="";
		String anio="";String mes="";
		aux=fecha;
		anio= fecha.substring(0,fecha.indexOf("-"));//mm-dd
		aux=aux.substring(aux.indexOf("-")+1);
		mes=aux.substring(0,fecha.indexOf("-")-2);

		rutaFolder = rutaFolder +"/"+ anio + "/" + Integer.valueOf(mes);

		File folder = new File(rutaFolder);

		if (folder.exists()) {
				//carpeta correcta
			} else {
				// folder.mkdir();
				folder.mkdirs();
				Files.setPosixFilePermissions(Paths.get(rutaFolder), this.permisos());
				//Se crea una nueva carpeta
			}

		 return rutaFolder;
	 }
     public Set<PosixFilePermission> permisos(){
		
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
		
        return perms;
	}


}
