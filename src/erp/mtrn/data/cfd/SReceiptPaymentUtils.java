/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import erp.client.SClientInterface;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Sergio Flores
 */
public abstract class SReceiptPaymentUtils {

    public static int getReceiptPaymentId(final SClientInterface client, final int cfdId) throws Exception {
        int receiptPaymentId = 0;
        
        try (Statement statement = client.getSession().getStatement().getConnection().createStatement()) {
            String sql = "SELECT fid_rcp_pay_n FROM trn_cfd WHERE id_cfd = " + cfdId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                receiptPaymentId = resultSet.getInt(1);
            }
        }
        
        return receiptPaymentId;
    }
}
