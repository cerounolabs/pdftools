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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class Main {
    
    private static TransactionSQL tr;
    private static PDFManager pdf;
    
    private static String WEBCON_COD;
    private static String WEBCON_DOC;
    
    private static String WEBINF_ARC;
    private static String WEBINF_FAJ;
    
    private static String WEBINFEMP_EMP;
    private static String WEBINFEMP_TEL;
    private static String WEBINFEMP_FEC;
    private static String WEBINFEMP_TIP;
    
    private static String WEBINFPER_NOM;
    private static String WEBINFPER_APE;
    private static String WEBINFPER_DOC;
    private static String WEBINFPER_SEX;
    private static String WEBINFPER_EST;
    private static String WEBINFPER_NAC;
    private static String WEBINFPER_FEC;
    
    private static String WEBINFDIR_CIU;
    private static String WEBINFDIR_BAR;
    private static String WEBINFDIR_DIR;
    private static String WEBINFDIR_TEL;
    private static String WEBINFDIR_FRP;
    private static String WEBINFDIR_FRS;
    
    private static String WEBINFTRA_CIU;
    private static String WEBINFTRA_DIR;
    private static String WEBINFTRA_EMP;
    private static String WEBINFTRA_CAR;
    private static String WEBINFTRA_TEL;
    private static String WEBINFTRA_ING;
    private static String WEBINFTRA_FRP;
    private static String WEBINFTRA_FRS;
    
    private static String fileName;
    private static String txt1;
    
    
    private static int posText;
    private static int cantPag;
    private static int index;
    
    public static void main(String[] args) throws IOException, SQLException, ParseException {
        tr          = new TransactionSQL();
        
        WEBCON_COD  = args[0];
        WEBCON_DOC  = tr.WEBCONGetDocumento(WEBCON_COD);
        WEBINF_ARC  = tr.WEBINFGetArchivo(WEBCON_COD);
        fileName    = generarPDF(WEBCON_DOC.trim(), WEBINF_ARC.trim(), WEBCON_COD);

        pdf         = new PDFManager();
        pdf.setFilePath(fileName);

        try {
            txt1        = pdf.toText();
            
            //solicitudDireccion(txt1);
            solicitudPersona(txt1);
            solicitudTrabajo(txt1);

            posText     = posicionPalabra(txt1, "Pág: 1/");
            cantPag     = Integer.parseInt(txt1.substring(posText + 7, posText + 8));
            
            posText     = posicionPalabra(txt1, "Faja");
            WEBINF_FAJ  = txt1.substring(posText + 6, posText + 7);
            tr.WEBINFSetFaja(WEBCON_COD, WEBINF_FAJ);
            
            index       = 0;
            
            for (int i = 0; i < cantPag; i++) {
                txt1 = solicitudConsulta(txt1);
            }
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
    
    public static void solicitudPersona (String txt) throws SQLException{
        String auxTxt;
        
        posText         = posicionPalabra(txt, "Nombre: ");
        auxTxt          = txt.substring(posText + 8);
        WEBINFPER_NOM   = auxTxt.substring(0, auxTxt.indexOf("\n"));
        
        posText         = posicionPalabra(txt, "Apellido: ");
        auxTxt          = txt.substring(posText + 10);
        WEBINFPER_APE   = auxTxt.substring(0, auxTxt.indexOf("\n"));
        
        posText         = posicionPalabra(txt, "Documento: ");
        auxTxt          = txt.substring(posText + 11);
        WEBINFPER_DOC   = auxTxt.substring(0, 10);
        
        posText         = posicionPalabra(txt, "Sexo: ");
        auxTxt          = txt.substring(posText + 6);
        WEBINFPER_SEX   = auxTxt.substring(0, 9);
        
        posText         = posicionPalabra(txt, "Estado Civil: ");
        auxTxt          = txt.substring(posText + 14);
        WEBINFPER_EST   = auxTxt.substring(0, auxTxt.indexOf(" "));
               
        posText         = posicionPalabra(txt, "Nacionalidad: ");
        auxTxt          = txt.substring(posText + 14);
        WEBINFPER_NAC   = auxTxt.substring(0, auxTxt.indexOf("\n"));
        
        posText         = posicionPalabra(txt, "Fec.Nacimiento: ");
        auxTxt          = txt.substring(posText + 16);
        WEBINFPER_FEC   = auxTxt.substring(0, auxTxt.indexOf("\n"));
        
        if (WEBINFPER_EST.equals("mes")){
            posText         = posicionPalabra(txt, "Sexo: ");
            auxTxt          = txt.substring(posText + 16);
            WEBINFPER_EST   = auxTxt.substring(0, auxTxt.indexOf("\n"));
        }
        
        if (WEBINFPER_NAC.equals("mes Confidenciales\r")){
            posText         = posicionPalabra(txt, "Histórico de Lugares de Trabajo");
            auxTxt          = txt.substring(posText - 11);
            WEBINFPER_NAC   = auxTxt.substring(0, auxTxt.indexOf("\n"));
        }
        
        if (WEBINFPER_FEC.equals("s Confidenciales\r")){
            posText         = posicionPalabra(txt, "PARAGUAYA");
            auxTxt          = txt.substring(posText - 12);
            WEBINFPER_FEC   = auxTxt.substring(0, 10);
        }
        
        tr.WEBINFPERSetPersona(WEBCON_COD, 1, WEBINFPER_NOM, WEBINFPER_APE, WEBINFPER_DOC, WEBINFPER_SEX, WEBINFPER_EST, WEBINFPER_NAC, WEBINFPER_FEC);
    }
    /*
    public static void solicitudDireccion (String txt) throws SQLException{
        String auxTxt;
        String forTxt;
        int indexInt    = 0;
        forTxt          = txt.substring(0, 6);
        posText         = posicionPalabra(txt, "Histórico de Direcciones");

        if (posText > 0) {
            if (forTxt.equals("Docume")) {
                posText         = posicionPalabra(txt, "Histórico de Direcciones\r\n");
                auxTxt          = txt.substring(posText + 33);

                posText         = posicionPalabra(auxTxt, "Solicitudes de Informes (Últimos 3 años)\r\n");
                auxTxt          = auxTxt.substring(0, posText - 2);
            } else {
                posText         = posicionPalabra(txt, "Histórico de Direcciones");
                auxTxt          = txt.substring(posText + 26);

                posText         = posicionPalabra(auxTxt, "Solicitudes de Informes (Últimos 3 años)\r\n");
                auxTxt          = auxTxt.substring(0, posText - 1);

                while(auxTxt.compareToIgnoreCase("") != 0) {
                    System.out.println("+++++++++++++++++++++++++");
                    int auxPosText = 0;
                    auxTxt = auxTxt.substring(7);

                    posText         = posicionPalabra(auxTxt, "Ciudad:");
                    WEBINFDIR_DIR   = auxTxt.substring(0, posText - 1);
                    System.out.println("WEBINFDIR_DIR => " + WEBINFDIR_DIR);
                    
                    auxTxt          = auxTxt.substring(posText + 8);
                    //WEBINFDIR_CIU   = auxTxt.substring(0, auxTxt.indexOf(" "));
                    WEBINFDIR_CIU   = auxTxt.substring(0, auxTxt.indexOf("\r\n"));
                    System.out.println("WEBINFDIR_CIU => " + WEBINFDIR_CIU);
                    
                    auxTxt          = auxTxt.substring(auxTxt.indexOf(" ") + 1);
                    
                    WEBINFDIR_TEL   = auxTxt.substring(0, auxTxt.indexOf("\r\n"));
                    posText         = posicionPalabra(WEBINFDIR_TEL, "Prim. y Ult. Ref:");

                    if (posText == 0) {
                        WEBINFDIR_TEL   = "";
                    } else {
                        WEBINFDIR_TEL   = auxTxt.substring(0, auxTxt.indexOf("\r\n"));
                    }
                    
                    System.out.println("WEBINFDIR_TEL => " + WEBINFDIR_TEL);
                    posText         = posicionPalabra(auxTxt, "Barrio:");
                    WEBINFDIR_BAR   = auxTxt.substring(2, posText);
                    System.out.println("WEBINFDIR_BAR => " + WEBINFDIR_BAR);
                    
                    auxTxt          = auxTxt.substring(posText + 15);
                    auxTxt          = auxTxt.substring(18);
                    WEBINFDIR_FRP   = auxTxt.substring(0, auxTxt.indexOf(" "));
                    System.out.println("WEBINFDIR_FRP => " + WEBINFDIR_FRP);
                    
                    auxTxt          = auxTxt.substring(auxTxt.indexOf(" "));
                    WEBINFDIR_FRS   = auxTxt.substring(1, auxTxt.indexOf("\r\n"));
                    System.out.println("WEBINFDIR_FRS => " + WEBINFDIR_FRS);
                    
                    auxTxt          = auxTxt.substring(auxTxt.indexOf("\r\n") + 2);
                    posText         = posicionPalabra(auxTxt, "Fecha");
                    auxPosText      = posicionPalabra(auxTxt, "Calle:");
                                       
                    if (posText == auxPosText){
                        auxTxt = "";
                    }
                }
            }
        }
    }
    */
    public static void solicitudTrabajo (String txt) throws SQLException{
        String auxTxt;
        String forTxt;
        int indexInt    = 0;
        forTxt          = txt.substring(0, 6);
        posText         = posicionPalabra(txt, "Histórico de Lugares de Trabajo");
        
        if (posText > 0) {
            if (forTxt.equals("Docume")){
                posText         = posicionPalabra(txt, "Histórico de Lugares de Trabajo\r\n");
                auxTxt          = txt.substring(posText + 33);

                posText         = posicionPalabra(auxTxt, "Solicitudes de Informes (Últimos 3 años)\r\n");
                auxTxt          = auxTxt.substring(0, posText - 2);
                
                while(auxTxt.compareToIgnoreCase("") != 0) {
                    indexInt        = indexInt + 1;

                    posText         = posicionPalabra(auxTxt, "Lugar:");
                    auxTxt          = auxTxt.substring(posText + 7);

                    posText         = posicionPalabra(auxTxt, "Cargo:");
                    WEBINFTRA_EMP   = auxTxt.substring(0, posText);
                    auxTxt          = auxTxt.substring(posText + 7);

                    posText         = posicionPalabra(auxTxt, "Tel:");
                    WEBINFTRA_CAR   = auxTxt.substring(0, posText);
                    auxTxt          = auxTxt.substring(posText + 5);

                    posText         = posicionPalabra(auxTxt, "Dir.:");
                    WEBINFTRA_TEL   = auxTxt.substring(0, posText - 1);
                    auxTxt          = auxTxt.substring(posText + 6);

                    posText         = posicionPalabra(auxTxt, "Ciudad:");
                    WEBINFTRA_DIR   = auxTxt.substring(0, posText);
                    auxTxt          = auxTxt.substring(posText + 8);
                    
                    posText         = posicionPalabra(auxTxt, "F.Ing.:");

                    if (posText > 0) {
                        WEBINFTRA_CIU   = auxTxt.substring(0, posText - 1);
                        auxTxt          = auxTxt.substring(posText + 8);
                        posText         = posicionPalabra(auxTxt, "Fec. de 1ra. y Ultima Referencia de este trabajo:");
                        WEBINFTRA_ING   = auxTxt.substring(0, posText);
                        
                    } else {
                        posText         = posicionPalabra(auxTxt, "Fec. de 1ra. y Ultima Referencia de este trabajo:");
                        WEBINFTRA_CIU   = auxTxt.substring(0, posText - 1);
                        WEBINFTRA_ING   = "";
                    }
                    
                    WEBINFTRA_FRP   = auxTxt.substring(posText + 50, posText + 60);
                    WEBINFTRA_FRS   = auxTxt.substring(posText + 61, posText + 71);
                    auxTxt          = auxTxt.substring(posText + 71);

                    tr.WEBINFPERSetTrabajo(WEBCON_COD, indexInt, WEBINFTRA_EMP, WEBINFTRA_CAR, WEBINFTRA_ING, WEBINFTRA_TEL, WEBINFTRA_CIU, WEBINFTRA_DIR, WEBINFTRA_FRP, WEBINFTRA_FRS);
                }
            } else {
                posText         = posicionPalabra(txt, "Histórico de Lugares de Trabajo ");
                auxTxt          = txt.substring(posText + 35);

                posText         = posicionPalabra(auxTxt, "Histórico de Direcciones\r\n");
                auxTxt          = auxTxt.substring(0, posText - 2);

                while(auxTxt.compareToIgnoreCase("") != 0) {
                    indexInt        = indexInt + 1;

                    posText         = posicionPalabra(auxTxt, "Cargo:");
                    WEBINFTRA_TEL   = auxTxt.substring(5, posText);
                    auxTxt          = auxTxt.substring(posText);

                    posText         = posicionPalabra(auxTxt, "Ciudad:");
                    WEBINFTRA_CIU   = auxTxt.substring(posText + 8, posText + 10 + auxTxt.indexOf("\n"));
                    
                    auxTxt          = auxTxt.substring(posText + 11 + auxTxt.indexOf("\n"));
                    WEBINFTRA_CAR   = auxTxt.substring(0, auxTxt.indexOf("\n"));

                    posText         = posicionPalabra(auxTxt, WEBINFTRA_CAR);
                    auxTxt          = auxTxt.substring(1 + auxTxt.indexOf("\n"));
                    WEBINFTRA_DIR   = auxTxt.substring(0, auxTxt.indexOf("\n"));

                    posText         = posicionPalabra(auxTxt, "Lugar:");
                    auxTxt          = auxTxt.substring(posText + 7);
                    posText         = posicionPalabra(auxTxt, "Dir.:");
                    WEBINFTRA_EMP   = auxTxt.substring(0, posText - 1);
                    auxTxt          = auxTxt.substring(posText + 6);
                    
                    posText         = posicionPalabra(auxTxt, "F.Ing.:");
                    if (posText > 0) {
                        WEBINFTRA_ING   = auxTxt.substring(9, 19);
                        auxTxt          = auxTxt.substring(19);
                    }
                    
                    WEBINFTRA_FRS   = auxTxt.substring(51, 61);
                    auxTxt          = auxTxt.substring(61);

                    WEBINFTRA_FRP   = auxTxt.substring(0);
                    auxTxt          = auxTxt.substring(10);
                    tr.WEBINFPERSetTrabajo(WEBCON_COD, indexInt, WEBINFTRA_EMP, WEBINFTRA_CAR, WEBINFTRA_ING, WEBINFTRA_TEL, WEBINFTRA_CIU, WEBINFTRA_DIR, WEBINFTRA_FRP, WEBINFTRA_FRS);
                }
            }
        }
    }

    public static String solicitudConsulta (String txt) throws SQLException, ParseException{
        int position    = posicionPalabra(txt, "Tipo OperaciónAfiliado");
        
        String text1    = txt.substring(position + 24);
        String text2    = null;
        String result   = text1.substring(0, text1.indexOf("\n"));
        String compEmp1 = "Solicitudes de Informes  (Resumen últimos 30 días)";
        String compEmp2 = "Informconf Credit Scoring M0200INF";
        String compEmp3 = "Solicitudes de Informes ";
        String compEmp4 = "  (Resumen últimos 30 días)";
        String compEmp5 = "(Art.28 C.N.) eximiéndose de toda responsabilidad por cualquier alteración o error que pudiera existir, no siendo tampoco carta";
        String compEmp6 = "Solicitudes de Informes  (Últimos 3 años)";
        String compEmp7 = "  (Resumen últimos 3 años)";
        String compEmp8 = "s  (Resumen últimos 30 días)";

        
        while(result.compareToIgnoreCase(compEmp1) != 0 && result.compareToIgnoreCase(compEmp2) != 0 && result.compareToIgnoreCase(compEmp3) != 0 && result.compareToIgnoreCase(compEmp4) != 0 && result.compareToIgnoreCase(compEmp5) != 0 && result.compareToIgnoreCase(compEmp6) != 0 && result.compareToIgnoreCase(compEmp7) != 0 && result.compareToIgnoreCase(compEmp8) != 0) {
            if (compEmp4.trim().equals(result.trim()) || compEmp8.trim().equals(result.trim())) {
                break;
            }

            if (!"".equals(result)) {
                String xResult      = null;
                
                index               = index + 1;
                posText             = posicionPalabra(result, "(Tel:");
                WEBINFEMP_EMP       = result.substring(0, posText).toUpperCase();
                
                xResult             = result.substring(posText);
                posText             = posicionPalabra(xResult, ")");
                WEBINFEMP_TEL       = xResult.substring(0, posText + 1).toUpperCase();
                
                xResult             = xResult.substring(posText + 2);
                WEBINFEMP_FEC       = xResult.substring(0, 10);
                
                xResult             = xResult.substring(11);
                WEBINFEMP_TIP       = xResult.toUpperCase();
                
                tr.WEBINFEMPSetEmpresa(index, WEBCON_COD, result, WEBINFEMP_EMP, WEBINFEMP_TEL, WEBINFEMP_FEC, WEBINFEMP_TIP);
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

    public static String generarPDF(String arcCI, String arcByte, String arcName) throws FileNotFoundException, IOException, SQLException{
        String dir          = tr.PARCOM01GetDirectorio(101);
        File folder         = new File(dir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        NombreArchivo na    = new NombreArchivo();
        String nameFile     = dir.trim() + "\\Informconf_CI_" + arcCI.trim() + "_" + arcName.trim() + ".pdf";
        byte[] arcBase64    = Base64.getDecoder().decode(arcByte);

        try (FileOutputStream fos = new FileOutputStream(nameFile)) {
            fos.write(arcBase64);
        }

        return nameFile;
    }
}
