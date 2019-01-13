/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdftools;

/*
 * @author CEROUNO Labs - Christian Zelaya
 * @version 1.0
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

public class Main {
    private static int posText;
    private static TransactionSQL tr;
    private static PDFManager pdf;
    
    private static String WEBCON_COD;
    private static String WEBCON_DOC;
    private static String WEBINF_ARC;
    private static String WEBINF_FAJ;
    private static String fileName;
    
    public static void main(String[] args) throws IOException, SQLException {
        tr  = new TransactionSQL();
        
        WEBCON_COD  = "20190113145300";
        //WEBCON_COD  = args[0];
        WEBCON_DOC  = tr.WEBCONGetDocumento(WEBCON_COD);
        WEBINF_ARC  = tr.WEBINFGetArchivo(WEBCON_COD);
        fileName    = generarPDF(WEBCON_DOC.trim(), WEBINF_ARC.trim());
        
        pdf = new PDFManager();
        pdf.setFilePath(fileName);
        
        try {
            String text     = pdf.toText();
            System.out.println(text);
            
            posText     = posicionPalabra(text, "Faja");
            WEBINF_FAJ  = text.substring(posText + 6, posText + 7);
            tr.WEBINFSetFaja(WEBCON_COD, WEBINF_FAJ);
            
        } catch (IOException ex) {
            System.err.print(ex);
        }
    }
    
    public static int posicionPalabra(String textoBusca, String palabraBusca){
        String[] palabras = palabraBusca.split("\\s+");
        int posicion = 0;

        for (String palabra : palabras) {
            if (textoBusca.contains(palabra)) {
                posicion = textoBusca.indexOf(palabraBusca);
            }
        }
        return posicion;
    }

    public static String generarPDF(String arcCI, String arcByte) throws FileNotFoundException, IOException{
        File folder         = new File("c:\\DATOS\\Informconf\\");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        NombreArchivo na    = new NombreArchivo();
        String nameFile     = "c:\\DATOS\\Informconf\\Informconf_CI_" + arcCI + "_" + na.getNombre() + ".pdf";
        byte[] arcBase64    = Base64.getDecoder().decode(arcByte);

        try (FileOutputStream fos = new FileOutputStream(nameFile)) {
            fos.write(arcBase64);
        }

        return nameFile;
    }
}