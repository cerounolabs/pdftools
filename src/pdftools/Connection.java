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

import java.sql.SQLException;

public class Connection {
    private java.sql.Connection conn_driver = null;
    private final String conn_url           = "jdbc:sqlserver://";
    private final String conn_server        = "PC-01";
    private final String conn_port          = "1433";
    private final String conn_instance      = "SQLFULLS2012";
    private final String conn_database      = "CARSA_20180730";
    private final String conn_user          = "sa";
    private final String conn_pass          = "sa123";
    private final String conn_method        = "Direct";
    private final String conn_security      = "false";
/*
    private java.sql.Connection conn_driver = null;
    private final String conn_url           = "jdbc:sqlserver://";
    private final String conn_server        = "SRVAPP";
    private final String conn_port          = "1433";
    private final String conn_instance      = "SQLEXPRESS2012";
    private final String conn_database      = "SISTEMAA";
    private final String conn_user          = "christian";
    private final String conn_pass          = "Carsa@2019";
    private final String conn_method        = "Direct";
    private final String conn_security      = "false";
*/
    public Connection() {
    
    }
    
    public java.sql.Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn_driver = java.sql.DriverManager.getConnection(getConnectionUrl(), conn_user, conn_pass);
            System.out.print("YES");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.print("Error Trace in getConnection() : " + e.getMessage());
        }
        return conn_driver;
    }
    
    private String getConnectionUrl() {
        return conn_url + conn_server + ":" + conn_port + ";databaseName=" + conn_database + ";selectMethod=" + conn_method + ";integratedSecurity=" + conn_security + ";";
    }
}
