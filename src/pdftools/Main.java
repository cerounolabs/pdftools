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
    private static String txt1;
    
    private static int index;
    
    public static void main(String[] args) throws IOException, SQLException {
        tr          = new TransactionSQL();
        
        WEBCON_COD  = args[0];
        WEBCON_DOC  = tr.WEBCONGetDocumento(WEBCON_COD);
        WEBINF_ARC  = tr.WEBINFGetArchivo(WEBCON_COD);
        fileName    = generarPDF(WEBCON_DOC.trim(), WEBINF_ARC.trim());
        
        pdf         = new PDFManager();
        pdf.setFilePath(fileName);
        
        try {
            txt1        = pdf.toText();
            posText     = posicionPalabra(txt1, "Faja");
            WEBINF_FAJ  = txt1.substring(posText + 6, posText + 7);
            tr.WEBINFSetFaja(WEBCON_COD, WEBINF_FAJ);
            index       = 0;
            txt1        = solicitudConsulta(txt1);
            txt1        = solicitudConsulta(txt1);
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
    
    public static String solicitudConsulta (String txt) throws SQLException{
        int position    = posicionPalabra(txt, "Tipo OperaciónAfiliado");
        
        String text1    = txt.substring(position + 24);
        String text2    = null;
        String result   = text1.substring(0, text1.indexOf("\n"));
        String compEmp1 = "Solicitudes de Informes  (Resumen últimos 30 días)";
        String compEmp2 = "Informconf Credit Scoring M0200INF";
        String compEmp3 = "Solicitudes de Informes ";
        String compEmp4 = "  (Resumen últimos 30 días)";

        while(result.compareToIgnoreCase(compEmp1) != 0 && result.compareToIgnoreCase(compEmp2) != 0 && result.compareToIgnoreCase(compEmp3) != 0 && result.compareToIgnoreCase(compEmp4) != 0) {
            if ("(Resumen últimos 30 días)".equals(result.trim())) {
                break;
            }

            if (!"".equals(result)) {
                index = index + 1;
                tr.WEBINFEMPSetEmpresa(index, WEBCON_COD, result);
            }

            text2   = text1.substring(result.length() + 1);
            result  = text2.substring(0, text2.indexOf("\r"));
            result  = text2.substring(0, text2.indexOf("\n"));
            result  = text2.substring(0, text2.indexOf("\r\n"));
            result  = result.trim();
            text1   = text2;
        }
        
        return text1;
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