/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.versioning;

import org.dspace.core.ConfigurationManager;
import org.dspace.storage.rdbms.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Mark Diggory  (mdiggory at atmire dot com)
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Kevin van de Velde (kevin at atmire.com)
 */
public class InitializeDatabase {

    private static String versionItem= "CREATE TABLE versionitem(versionitem_id integer NOT NULL PRIMARY KEY,"
                                   +"item_id INTEGER REFERENCES Item(item_id),"
                                   +" version_number integer,"
                                   +" eperson_id INTEGER REFERENCES EPerson(eperson_id),"
                                   +"version_date TIMESTAMP,"
                                   +"version_summary VARCHAR(255),"
                                   +"versionhistory_id INTEGER REFERENCES VersionHistory(versionhistory_id)";

    private static String versionItemSeq="CREATE SEQUENCE versionitem_seq;";
    private static String versionHistory = "CREATE TABLE versionhistory(versionhistory_id integer NOT NULL PRIMARY KEY);";
    private static String versionHistorySeq="CREATE SEQUENCE versionhistory_seq;";

    public static void main(String[] argv){
        System.out.println("InitializeDatabase Invoked.");
        ConfigurationManager.loadConfig(null);
        if(!exists("versionhistory")){
            createTable(versionHistory);
            createTable(versionHistorySeq);
        }
        if(!exists("versionitem")){
            createTable(versionItem);
            createTable(versionItemSeq);
        }
    }


    private static boolean exists(String table){
        Connection c=null;
        Statement s = null;
        try{
            s = c.createStatement();
            s.executeQuery("select * from " + table);
            s.close();
        }catch (SQLException e) {
            try {
                if(s!=null) s.close();
                if(c!=null){
                    c.rollback();
                    c.close();
                }
            }catch (SQLException e0) {
                e0.printStackTrace();
            }
            return false;
        }finally{
             try {
                if(s!=null) s.close();
                if(c!=null) c.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }


    private static boolean createTable(String sqlStatement){
        Connection c=null;
        Statement s=null;
        try{
            c = DatabaseManager.getConnection();
            s = c.createStatement();
            s.executeUpdate(sqlStatement);
            c.commit();
        }catch(SQLException e){
            try {
                if(s!=null) s.close();
                if(c!=null){
                    c.rollback();
                    c.close();
                }
            }catch (SQLException e0) {
                e0.printStackTrace();
            }
            return false;

        }finally{
           try {
               if(s!=null) s.close();
               if(c!=null) c.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }
}
