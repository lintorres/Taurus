package cl.taurus.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ConversorXml {
	private static final Logger LOGGER = Logger.getLogger("newexcel.ExcelOOXML");
	public String getXls(String archivoSalida,ArrayList<ArrayList<String>> datos,  String fehca) throws IOException {
		
		File archivo = new File(archivoSalida+"/Reporte.xlsx");
		if(archivo.exists()) archivo.delete();

		archivo.createNewFile();
        // Creamos el libro de trabajo de Excel formato OOXML
		Workbook libro = new HSSFWorkbook();
        
        // La hoja donde pondremos los datos
        Sheet pagina = libro.createSheet("Reporte de productos");
        
        // Creamos el estilo paga las celdas del encabezado
        CellStyle style = libro.createCellStyle();
        // Indicamos que tendra un fondo azul aqua
        // con patron solido del color indicado
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        
        String[] titulos = {"PLU ","Detalle ","Stock"," Punto reorden","Estado"};
        
     // Creamos una fila en la hoja en la posicion 0
        Row fila = pagina.createRow(0);
        
     // Creamos el encabezado
        for(int i = 0; i < titulos.length; i++) {
            // Creamos una celda en esa fila, en la posicion 
            // indicada por el contador del ciclo
            Cell celda = fila.createCell(i);
            
            // Indicamos el estilo que deseamos 
            // usar en la celda, en este caso el unico 
            // que hemos creado
            celda.setCellStyle(style); 
            celda.setCellValue(titulos[i]);
        }
               
        int indice=1;
        for (int j = 0; j < datos.get(0).size(); j++) {
        
        	fila = pagina.createRow(indice);
             
             // Creamos el encabezado
                for(int i = 0; i < 5; i++) {
                    // Creamos una celda en esa fila, en la posicion 
                    // indicada por el contador del ciclo
                	Cell celda = fila.createCell(i);
                    
                    celda.setCellValue( datos.get(i).get(j));
                }
          
          indice++;
			
			
		}
        
        
        
        FileOutputStream file2 = new FileOutputStream(archivo);
        libro.write(file2);
        libro.close();
        
        
        return ""+file2;
		
	}

}
