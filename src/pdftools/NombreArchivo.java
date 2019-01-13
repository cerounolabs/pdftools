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

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class NombreArchivo {
    
    public NombreArchivo() {
    
    }
    
    public String getNombre(){
        String fecha;
        
        Calendar c  = Calendar.getInstance();
        String dd   = nroVerifica(c.get(Calendar.DATE));
        String mm   = nroVerifica(c.get(Calendar.MONTH));
        String aa   = nroVerifica(c.get(Calendar.YEAR));
        String hh   = nroVerifica(c.get(Calendar.HOUR));
        String ii   = nroVerifica(c.get(Calendar.MINUTE));
        String ss   = nroVerifica(c.get(Calendar.SECOND));
        String nn   = nroRandom(100, 999);
        fecha       = aa + mm + dd + hh + ii + ss + nn; 

        return fecha;
    }
    
    private String nroRandom(int min, int max){
        String nroString;
        int nroInt  = ThreadLocalRandom.current().nextInt(min, max);
        nroString   = nroVerifica(nroInt);
        
        return nroString;
    }
    
    private String nroVerifica(int nroInt){
        String nroString;
        
        if (nroInt < 10) {
            nroString = '0' + Integer.toString(nroInt);
        } else {
            nroString = Integer.toString(nroInt);
        }
        
        return nroString;
    }
}