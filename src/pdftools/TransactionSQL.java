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
}