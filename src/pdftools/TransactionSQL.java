/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdftools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author soporte3
 */
public class TransactionSQL {
    private Connection db           = null;
    private PreparedStatement ps    = null;
    private ResultSet rs            = null;
    
    public TransactionSQL() {
    
    }
    
    public String WEBCONGetDocumento(String WEBCON_COD) throws SQLException{
        String SQLReturn        = null;
        
        try {
            db = new Connection();
            ps = db.getConnection().prepareStatement("SELECT * FROM WEBCON WHERE WEBCON_COD = ?");
            ps.setString(1, WEBCON_COD);
            ps.executeQuery();
            rs = ps.executeQuery();
            
            while (rs.next()) {
                SQLReturn = rs.getString("WEBCON_DOC");
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            SQLReturn = "ERROR";
            
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        
        return SQLReturn;
    }
    
    public String PARCOM01GetDirectorio(int PaCEmp) throws SQLException{
        String SQLReturn        = null;
        String auxPaCIP         = "";
        String auxPaCCarp       = "";
        try {
            db = new Connection();
            ps = db.getConnection().prepareStatement("SELECT * FROM PARCOM01 WHERE PaCEmp = ?");
            ps.setInt(1, PaCEmp);
            ps.executeQuery();
            rs = ps.executeQuery();
            
            while (rs.next()) {
                auxPaCIP    = rs.getString("PaCIP");
                auxPaCCarp  = rs.getString("PaCCarp");
            }
            
            rs.close();
            ps.close();
           
            SQLReturn = auxPaCIP.trim() + "\\" + auxPaCCarp.trim();
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            SQLReturn = "ERROR";
            
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        
        return SQLReturn;
    }
    
    public String WEBINFGetArchivo(String WEBINF_CON) throws SQLException{
        String SQLReturn        = null;
        
        try {
            db = new Connection();
            ps = db.getConnection().prepareStatement("SELECT * FROM WEBINF WHERE WEBINF_CON = ?");
            ps.setString(1, WEBINF_CON);
            ps.executeQuery();
            rs = ps.executeQuery();
            
            while (rs.next()) {
                SQLReturn = rs.getString("WEBINF_ARC");
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            SQLReturn = "ERROR";
            
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        
        return SQLReturn;
    }
    
    public String WEBINFSetFaja(String WEBINF_CON, String WEBINF_FAJ) throws SQLException{
        String SQLReturn = null;
        
        try {
            db = new Connection();
            ps = db.getConnection().prepareStatement("UPDATE WEBINF SET WEBINF_FAJ = ? WHERE WEBINF_CON = ?");
            ps.setString(1, WEBINF_FAJ);
            ps.setString(2, WEBINF_CON);
            ps.executeUpdate();
            ps.close();
            SQLReturn = "OK";
            
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            SQLReturn = "ERROR";
            
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        
        return SQLReturn;
    }
    
    public String WEBINFEMPSetEmpresa(int WEBINFEMP_COD, String WEBINFEMP_CON, String WEBINFEMP_COM, String WEBINFEMP_EMP, String WEBINFEMP_TEL, String WEBINFEMP_FEC, String WEBINFEMP_TIP) throws SQLException{
        String SQLReturn = null;
        
        try {
            db = new Connection();
            ps = db.getConnection().prepareStatement("INSERT INTO WEBINFEMP (WEBINFEMP_COD, WEBINFEMP_CON, WEBINFEMP_COM, WEBINFEMP_EMP, WEBINFEMP_TEL, WEBINFEMP_FEC, WEBINFEMP_TIP) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, WEBINFEMP_COD);
            ps.setString(2, WEBINFEMP_CON);
            ps.setString(3, WEBINFEMP_COM);
            ps.setString(4, WEBINFEMP_EMP);
            ps.setString(5, WEBINFEMP_TEL);
            ps.setString(6, WEBINFEMP_FEC);
            ps.setString(7, WEBINFEMP_TIP);
            ps.executeUpdate();
            ps.close();
            SQLReturn = "OK";
            
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            SQLReturn = "ERROR";
            
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        
        return SQLReturn;
    }
    
    public String WEBINFPERSetPersona(String WEBINFPER_CON, int WEBINFPER_COD, String WEBINFPER_NOM, String WEBINFPER_APE, String WEBINFPER_DOC, String WEBINFPER_SEX, String WEBINFPER_EST, String WEBINFPER_NAC, String WEBINFPER_FEC) throws SQLException{
        String SQLReturn = null;
        
        try {
            db = new Connection();
            ps = db.getConnection().prepareStatement("INSERT INTO WEBINFPER (WEBINFPER_CON, WEBINFPER_COD, WEBINFPER_NOM, WEBINFPER_APE, WEBINFPER_DOC, WEBINFPER_SEX, WEBINFPER_EST, WEBINFPER_NAC, WEBINFPER_FEC) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, WEBINFPER_CON);
            ps.setInt(2, WEBINFPER_COD);
            ps.setString(3, WEBINFPER_NOM);
            ps.setString(4, WEBINFPER_APE);
            ps.setString(5, WEBINFPER_DOC);
            ps.setString(6, WEBINFPER_SEX);
            ps.setString(7, WEBINFPER_EST);
            ps.setString(8, WEBINFPER_NAC);
            ps.setString(9, WEBINFPER_FEC);
            ps.executeUpdate();
            ps.close();
            SQLReturn = "OK";
            
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            SQLReturn = "ERROR";
            
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        
        return SQLReturn;
    }
    
    public String WEBINFDIRSetDireccion(String WEBINFDIR_CON, int WEBINFDIR_COD, String WEBINFDIR_CIU, String WEBINFDIR_BAR, String WEBINFDIR_DIR, String WEBINFDIR_TEL, String WEBINFDIR_FRP, String WEBINFDIR_FRS) throws SQLException{
        String SQLReturn = null;
        
        try {
            db = new Connection();
            ps = db.getConnection().prepareStatement("INSERT INTO WEBINFDIR (WEBINFDIR_CON, WEBINFDIR_COD, WEBINFDIR_CIU, WEBINFDIR_BAR, WEBINFDIR_DIR, WEBINFDIR_TEL, WEBINFDIR_FRP, WEBINFDIR_FRS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, WEBINFDIR_CON);
            ps.setInt(2, WEBINFDIR_COD);
            ps.setString(3, WEBINFDIR_CIU);
            ps.setString(4, WEBINFDIR_BAR);
            ps.setString(5, WEBINFDIR_DIR);
            ps.setString(6, WEBINFDIR_TEL);
            ps.setString(7, WEBINFDIR_FRP);
            ps.setString(8, WEBINFDIR_FRS);
            ps.executeUpdate();
            ps.close();
            SQLReturn = "OK";
            
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            SQLReturn = "ERROR";
            
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        
        return SQLReturn;
    }
    
    public String WEBINFPERSetTrabajo(String WEBINFTRA_CON, int WEBINFTRA_COD, String WEBINFTRA_EMP, String WEBINFTRA_CAR, String WEBINFTRA_ING, String WEBINFTRA_TEL, String WEBINFTRA_CIU, String WEBINFTRA_DIR, String WEBINFTRA_FRP, String WEBINFTRA_FRS) throws SQLException{
        String SQLReturn = null;
        
        try {
            db = new Connection();
            ps = db.getConnection().prepareStatement("INSERT INTO WEBINFTRA (WEBINFTRA_CON, WEBINFTRA_COD, WEBINFTRA_EMP, WEBINFTRA_CAR, WEBINFTRA_ING, WEBINFTRA_TEL, WEBINFTRA_CIU, WEBINFTRA_DIR, WEBINFTRA_FRP, WEBINFTRA_FRS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, WEBINFTRA_CON);
            ps.setInt(2, WEBINFTRA_COD);
            ps.setString(3, WEBINFTRA_EMP);
            ps.setString(4, WEBINFTRA_CAR);
            ps.setString(5, WEBINFTRA_ING);
            ps.setString(6, WEBINFTRA_TEL);
            ps.setString(7, WEBINFTRA_CIU);
            ps.setString(8, WEBINFTRA_DIR);
            ps.setString(9, WEBINFTRA_FRP);
            ps.setString(10, WEBINFTRA_FRS);
            ps.executeUpdate();
            ps.close();
            SQLReturn = "OK";
            
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            SQLReturn = "ERROR";
            
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        
        return SQLReturn;
    }
}