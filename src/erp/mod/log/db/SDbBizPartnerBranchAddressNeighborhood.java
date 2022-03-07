/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import erp.mloc.data.SDataNeighborhood;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBizPartnerBranchAddressNeighborhood extends SDbRegistryUser implements Serializable {

    protected int mnPkBizPartnerBranchAddressId;
    protected int mnPkAddressAddressId;
    protected int mnFkNeighborhoodZipCode;
    
    protected SDataNeighborhood moDbmsNeighborhood;
    
    public SDbBizPartnerBranchAddressNeighborhood() {
        super(SModConsts.LOGU_BPB_ADD_NEI);
    }
    
    public void setPkBizPartnerBranchAddressId(int n) { mnPkBizPartnerBranchAddressId = n; }
    public void setPkAddressAddressId(int n) { mnPkAddressAddressId = n; }
    public void setFkNeighborhoodZipCode(int n) { mnFkNeighborhoodZipCode = n; }

    public int getPkBizPartnerBranchAddressId() { return mnPkBizPartnerBranchAddressId; }
    public int getPkAddressAddressId() { return mnPkAddressAddressId; }
    public int getFkNeighborhoodZipCode() { return mnFkNeighborhoodZipCode; }

    public void setDbmsNeighborhood(SDataNeighborhood o) { moDbmsNeighborhood = o; }
    
    public SDataNeighborhood getDbmsNeighborhood() { return moDbmsNeighborhood; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerBranchAddressId = pk[0];
        mnPkAddressAddressId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerBranchAddressId, mnPkAddressAddressId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBizPartnerBranchAddressId = 0;
        mnPkAddressAddressId = 0;
        mnFkNeighborhoodZipCode = 0;
        
        moDbmsNeighborhood = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bpb_add = " + mnPkBizPartnerBranchAddressId + " AND id_add_add = " + mnPkAddressAddressId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpb_add = " + pk[0] + " AND id_add_add = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * FROM " + getSqlTable() + " WHERE id_bpb_add = " + pk[0] + " AND id_add_add = " + pk[1];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBizPartnerBranchAddressId = resultSet.getInt("id_bpb_add");
            mnPkAddressAddressId = resultSet.getInt("id_add_add");
            mnFkNeighborhoodZipCode = resultSet.getInt("fk_nei_zip_code");

            mbRegistryNew = false;
        }
        
        // Read Neighborhood
        
        if (mnFkNeighborhoodZipCode != 0) {
            moDbmsNeighborhood = new SDataNeighborhood();
            moDbmsNeighborhood.read(new int[] { mnFkNeighborhoodZipCode } , session.getStatement());
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if(mbRegistryNew) {
            verifyRegistryNew(session);
        }
        
        if (mbRegistryNew) {

            mbDeleted = false;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkBizPartnerBranchAddressId + ", " + 
                mnPkAddressAddressId + ", " + 
                mnFkNeighborhoodZipCode + " " + 
                ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
//                "id_bpb_add = " + mnPkBizPartnerBranchAddressId + ", " +
//                "id_add_add = " + mnPkAddressAddressId + ", " +
                "fk_nei_zip_code = " + mnFkNeighborhoodZipCode + " " +
                getSqlWhere();
        }
        
        if (mnFkNeighborhoodZipCode != 0) {
            moDbmsNeighborhood = new SDataNeighborhood();
            moDbmsNeighborhood.read(new int[] { mnFkNeighborhoodZipCode } , session.getStatement());
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbBizPartnerBranchAddressNeighborhood registry = new SDbBizPartnerBranchAddressNeighborhood();
        
        registry.setPkBizPartnerBranchAddressId(this.getPkBizPartnerBranchAddressId());
        registry.setPkAddressAddressId(this.getPkAddressAddressId());
        registry.setFkNeighborhoodZipCode(this.getFkNeighborhoodZipCode());
        
        registry.setDbmsNeighborhood(this.getDbmsNeighborhood());
        
        return registry;
    } 
}
