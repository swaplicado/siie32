/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import cfd.DElement;
import cfd.ver33.DElementCfdiRelacionados;
import erp.SParamsApp;
import erp.cfd.SXmlDpsCfd;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;

/**
 *
 * @author Isabel Servín
 */
public class SUpdateTrnDpsCfd {

    public static void main(String[] args) throws Exception {
        
        SParamsApp paramsApp = new SParamsApp();
        SDbDatabase dbErp;
        SDbDatabase dbCompany;

        if (!paramsApp.read()) {
            throw new Exception(erp.SClient.ERR_PARAMS_APP_READING);
        }

        // create database connections:

        int result = 0;

        // connect to ERP database:

        dbErp = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        result = dbErp.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(), 
                paramsApp.getDatabaseName(), paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());

        if (result != SDbConsts.CONNECTION_OK) {
            throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
        }

        // connect to company database:

        String companyDatabase;

        String sql = "SELECT bd "
                + "FROM erp.cfgu_co ";
        try (ResultSet resultSet = dbErp.getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                companyDatabase = resultSet.getString("bd");
                
                System.out.println(resultSet.getString("bd"));

                dbCompany = new SDbDatabase(SDbConsts.DBMS_MYSQL);
                result = dbCompany.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(), 
                        companyDatabase, paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());

                if (result != SDbConsts.CONNECTION_OK) {
                    throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
                }

                ResultSet resultSetDpsCfd;
                ResultSet resultSetCfd;
                Statement readStatement = dbCompany.getConnection().createStatement();
                Statement readCfdStatement = dbCompany.getConnection().createStatement();
                Statement updateStatement = dbCompany.getConnection().createStatement();

                sql = "SELECT * FROM trn_dps_cfd;";
                resultSetDpsCfd = readStatement.executeQuery(sql);
                while (resultSetDpsCfd.next()) {
                    SXmlDpsCfd dpsCfd = new SXmlDpsCfd();
                    if (!resultSetDpsCfd.getString("xml").isEmpty()) {
                        dpsCfd.processXml(resultSetDpsCfd.getString("xml"));
                    }
                    
                    String tipoRelacion = "";
                    String uuid = "";
                    int year = 0, doc = 0;

                    if (dpsCfd.isAvailableCfdiRelacionados()) {
                        for (DElement element : dpsCfd.getElements()) {
                            if (element instanceof DElementCfdiRelacionados) {
                                DElementCfdiRelacionados cfdiRelacionados = (DElementCfdiRelacionados) element;
                                tipoRelacion = cfdiRelacionados.getAttTipoRelacion().getString();
                                uuid = cfdiRelacionados.getEltCfdiRelacionados().get(0).getAttUuid().getString();

                                break;
                            }
                        }
                    }
                    if (!uuid.isEmpty()) {
                        sql = "SELECT fid_dps_year_n, fid_dps_doc_n FROM trn_cfd WHERE uuid = '" + uuid + "'";
                        resultSetCfd = readCfdStatement.executeQuery(sql);
                        if (resultSetCfd.next()) {
                            year = resultSetCfd.getInt(1);
                            doc = resultSetCfd.getInt(2);
                        }

                        System.out.println("actualizando " + dbCompany.getDbName() + " " + resultSetDpsCfd.getInt("id_year") + "-" + resultSetDpsCfd.getInt("id_doc"));
                        sql = "UPDATE trn_dps_cfd set relation_tp = '" + tipoRelacion + "', related_uuid = '" + uuid + "', "
                                + "fid_related_dps_year_n = " + (year == 0 ? " NULL, " : year + ", ") + "fid_related_dps_doc_n = " + (doc == 0 ? " NULL " : doc + " ")
                                + "WHERE id_year = " + resultSetDpsCfd.getInt("id_year") + " AND id_doc = " + resultSetDpsCfd.getInt("id_doc");
                        updateStatement.execute(sql);
                    }
                }
            }
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Fin del proceso!");
    }
}

