package cl.taurus.resource;

import java.io.PrintStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Correo
{
  public Correo() {}
  
  public String enviarCorreo(int suc_id, String emp_pass_mail, String emp_nombre_mail_emisor, String emp_nombre_mail_receptor,String nombreReporte,String fecha)
  {
    try
    {
      //DTE dte = new DTE();
      //LectorFichero lectorFichero = new LectorFichero();
      
      String origen = emp_nombre_mail_emisor;
      String destino = emp_nombre_mail_receptor;
      String pass = emp_pass_mail;
     // System.out.println("fecha "+fecha.substring(6, 7));
      //System.out.println("año "+fecha.substring(0, 4));
      String mensaje = "Se adjuntan reporte "+ nombreReporte+ ".pdf con los detalles de productos, analizados con fecha "+fecha+".";
      
      String rutaAdjunto = "";
      Properties props = new Properties();
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.setProperty("mail.smtp.starttls.enable", "true");
      props.setProperty("mail.smtp.port", "587");
      props.setProperty("mail.smtp.user", destino);
      props.setProperty("mail.smtp.auth", "true");
      

      props.setProperty("mail.smtp.starttls.enable", "true");
      
      Session session = Session.getDefaultInstance(props, null);
      

      BodyPart texto = new MimeBodyPart();
      texto.setText(mensaje);
      

      BodyPart adjuntoXML = new MimeBodyPart();
      
      
      adjuntoXML.setDataHandler(new DataHandler(new FileDataSource("Reportes/"+suc_id+"/"+fecha.substring(0, 4)+"/"+fecha.substring(6, 7)+"/Reporte.xlsx")));
      System.out.println("\tRuta DTE: Reportes/"+suc_id+"/"+fecha.substring(0,4)+"/"+fecha.substring(6, 7)+"/Reporte.xlsx");
      

      adjuntoXML.setFileName("Reporte.xlsx");
      


      MimeMultipart multiParte = new MimeMultipart();
      multiParte.addBodyPart(texto);
      multiParte.addBodyPart(adjuntoXML);
      


      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(destino));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(destino));
      message.setSubject("REPORTE ESTADO DE PRODUCTOS - PREINFORME PARA ORDEN DE COMPRA");
      message.setContent(multiParte);
      

      Transport t = session.getTransport("smtp");
      t.connect("smtp.gmail.com", origen, pass);
      t.sendMessage(message, message.getAllRecipients());
      t.close();
      
      return "ENVIO COMPLETADO";
    }
    catch (Exception e)
    {
      e.printStackTrace(); }
    return "ENVIO INCOMPLET0";
  }
  

  public Transport conexionCuenta(String origen, String pass, String destino)
    throws MessagingException
  {
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.setProperty("mail.smtp.starttls.enable", "true");
    props.setProperty("mail.smtp.port", "587");
    props.setProperty("mail.smtp.user", destino);
    props.setProperty("mail.smtp.auth", "true");
    props.setProperty("mail.smtp.starttls.enable", "true");
    
    Session session = Session.getDefaultInstance(props, null);
    
    Transport t = session.getTransport("smtp");
    t.connect(origen, pass);
    


    return t;
  }
}