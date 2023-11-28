package clases;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Propiedades{
    public final String LOCATE_PATCH = System.getProperty("user.dir")+"\\";
    
    
    public static final String URL_Ejecutable_Cloud = "https://github.com/fParrax/Gruper/raw/master/distribution/Gruper.jar";
    public static final String URL_Properties_Cloud = "https://github.com/fParrax/Gruper/raw/master/distribution/version.properties";
    
    public static final String NAME_EXECUTABLE = "Gruper.jar";
    private static final String NAME_PROPERTIES = "version.properties";
    
    Properties myProperties = new Properties();
    BufferedWriter bw;
    
    public Propiedades(){
        openProperties();
    }
   
    
    protected void openProperties() {
       
        if(!new File(LOCATE_PATCH) .exists()){
           new File(LOCATE_PATCH)  .mkdirs();
        }
        
        File fileMyProperties = new File(LOCATE_PATCH+"\\"+NAME_PROPERTIES);
        
        if(!fileMyProperties.exists()){
            descargar("properties");
        }
        
        try {
                myProperties.load(new FileReader(fileMyProperties.getAbsolutePath()));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex);
            }
        
    }
    

    
    protected  void descargar(String nameArchivox) {
       String nameArchivoDescarga = "";
       String outPutNameFile ="";
        try {
            
            
            switch (nameArchivox){
                case "properties":
                    nameArchivoDescarga = URL_Properties_Cloud;
                    outPutNameFile = NAME_PROPERTIES;
                    
                    break;
                case "Ejecutable":
                     nameArchivoDescarga = URL_Ejecutable_Cloud;
                      outPutNameFile = NAME_EXECUTABLE;
                    break;
                
            }
            File file = new File(LOCATE_PATCH+"\\"+outPutNameFile);
            URL url = new URL(nameArchivoDescarga);
            
            
        URLConnection connection = url.openConnection();
        long size = connection.getContentLengthLong();
        InputStream inputStream = connection.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        FileOutputStream outputStream = new FileOutputStream(file);
       
        byte[] bytes = new byte[1024];
        int bytesRead;
        while ((bytesRead = bufferedInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, bytesRead);
        }
        outputStream.close();
            
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error descargando "+outPutNameFile+" \n" + e);
        }

        
        
    }
    
    public String getPropiedad(String property) {
        if(!myProperties.isEmpty()){
            return myProperties.getProperty(property);
        }else{
            openProperties();
            return myProperties.getProperty(property);
        }
        
    }
   
    protected ArrayList listarPropiedades() {
        ArrayList<String> lista = new ArrayList();

        Enumeration<Object> keys = myProperties.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            lista.add(key + " = " + myProperties.get(key));            
        }

        return lista;
    }
    
    protected void guardarCambios(String comentario){
        try {
            myProperties.store(new FileWriter(LOCATE_PATCH+NAME_PROPERTIES),comentario);
        } catch (IOException ex) {
            Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    protected void setPropert(String llave, String valor){
        if(!myProperties.isEmpty()){
             myProperties.setProperty(llave,valor);
             myProperties.setProperty("actualizado",new SimpleDateFormat("dd/MM/yyyy").format(new Date()).toString());
             
             guardarCambios(" "+new SimpleDateFormat("dd/MM/yyyy").format(new Date())+"");
        }else{
            openProperties();
            myProperties.setProperty(llave,valor);
             myProperties.setProperty("actualizado",new SimpleDateFormat("dd/MM/yyyy").format(new Date()).toString());
             guardarCambios(" "+new SimpleDateFormat("dd/MM/yyyy").format(new Date())+"");
        }
       
    }
   
    protected String getPathLocation_IS_NOT_USED() {
        String pathx = "";
        String osName = System.getProperty("os.name");

        if (osName.toLowerCase().startsWith("windows")) {
            pathx = System.getenv("ProgramFiles");
        } else if (osName.toLowerCase().startsWith("linux")) {
            String[] paths = System.getenv("PATH").split(":");

            pathx = Arrays.stream(paths)
                    .filter(path -> path.startsWith("/usr/bin"))
                    .findFirst()
                    .orElse(null);
        } else {
            System.out.println("El sistema operativo no es compatible");
        }

        return pathx;
    }
    

}
