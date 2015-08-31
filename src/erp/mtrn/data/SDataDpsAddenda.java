/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Manuel Barajas Cárabes
 */
public class SDataDpsAddenda extends erp.lib.data.SDataRegistry implements java.io.Serializable  {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.lang.String msLorealFolioNotaRecepcion;
    protected java.lang.String msBachocoSociedad;
    protected java.lang.String msBachocoOrganizacionCompra;
    protected java.lang.String msBachocoDivision;
    protected int mnSorianaTienda;
    protected int mnSorianaEntregaMercancia;
    protected java.util.Date mtSorianaRemisionFecha;
    protected java.lang.String msSorianaRemisionFolio;
    protected java.lang.String msSorianaPedidoFolio;
    protected int mnSorianaBultoTipo;
    protected double mdSorianaBultoCantidad;
    protected java.lang.String msSorianaNotaEntradaFolio;
    protected java.lang.String msModeloDpsDescripcion;
    protected int mnCfdAddendaSubtype;
    protected int mnFkCfdAddendaTypeId;

    protected java.util.Vector<SDataDpsAddendaEntry> mvDbmsDpsAddEntries;

    public SDataDpsAddenda() {
        super(SDataConstants.TRN_DPS_ADD);
        mvDbmsDpsAddEntries = new Vector<SDataDpsAddendaEntry>();
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setLorealFolioNotaRecepcion(java.lang.String s) { msLorealFolioNotaRecepcion = s; }
    public void setBachocoSociedad(java.lang.String s) { msBachocoSociedad = s; }
    public void setBachocoOrganizacionCompra(java.lang.String s) { msBachocoOrganizacionCompra = s; }
    public void setBachocoDivision(java.lang.String s) { msBachocoDivision = s; }
    public void setSorianaTienda(int n) { mnSorianaTienda = n; }
    public void setSorianaEntregaMercancia(int n) { mnSorianaEntregaMercancia = n; }
    public void setSorianaRemisionFecha(java.util.Date t) { mtSorianaRemisionFecha = t; }
    public void setSorianaRemisionFolio(java.lang.String s) { msSorianaRemisionFolio = s; }
    public void setSorianaPedidoFolio(java.lang.String s) { msSorianaPedidoFolio = s; }
    public void setSorianaBultoTipo(int n) { mnSorianaBultoTipo = n; }
    public void setSorianaBultoCantidad(double d) { mdSorianaBultoCantidad = d; }
    public void setSorianaNotaEntradaFolio(java.lang.String s) { msSorianaNotaEntradaFolio = s; }
    public void setModeloDpsDescripcion(java.lang.String s) { msModeloDpsDescripcion = s; }
    public void setCfdAddendaSubtype(int n) { mnCfdAddendaSubtype = n; }

    public void setFkCfdAddendaTypeId(int n) { mnFkCfdAddendaTypeId = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.lang.String getLorealFolioNotaRecepcion() { return msLorealFolioNotaRecepcion; }
    public java.lang.String getBachocoSociedad() { return msBachocoSociedad; }
    public java.lang.String getBachocoOrganizacionCompra() { return msBachocoOrganizacionCompra; }
    public java.lang.String getBachocoDivision() { return msBachocoDivision; }
    public int getSorianaTienda() { return mnSorianaTienda; }
    public int getSorianaEntregaMercancia() { return mnSorianaEntregaMercancia; }
    public java.util.Date getSorianaRemisionFecha() { return mtSorianaRemisionFecha; }
    public java.lang.String getSorianaRemisionFolio() { return msSorianaRemisionFolio; }
    public java.lang.String getSorianaPedidoFolio() { return msSorianaPedidoFolio; }
    public int getSorianaBultoTipo() { return mnSorianaBultoTipo; }
    public double getSorianaBultoCantidad() { return mdSorianaBultoCantidad; }
    public java.lang.String getSorianaNotaEntradaFolio() { return msSorianaNotaEntradaFolio; }
    public java.lang.String getModeloDpsDescripcion() { return msModeloDpsDescripcion; }
    public int getCfdAddendaSubtype() { return mnCfdAddendaSubtype; }
    public int getFkCfdAddendaTypeId() { return mnFkCfdAddendaTypeId; }

    public java.util.Vector<SDataDpsAddendaEntry> getDbmsDpsAddEntries() { return mvDbmsDpsAddEntries; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        msLorealFolioNotaRecepcion = "";
        msBachocoSociedad = "";
        msBachocoOrganizacionCompra = "";
        msBachocoDivision = "";
        mnSorianaTienda = 0;
        mnSorianaEntregaMercancia = 0;
        mtSorianaRemisionFecha = null;
        msSorianaRemisionFolio = "";
        msSorianaPedidoFolio = "";
        mnSorianaBultoTipo = 0;
        mdSorianaBultoCantidad = 0;
        msSorianaNotaEntradaFolio = "";
        msModeloDpsDescripcion = "";
        mnCfdAddendaSubtype = 0;
        mnFkCfdAddendaTypeId = 0;

        mvDbmsDpsAddEntries.clear();
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement oStatementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_add WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                msLorealFolioNotaRecepcion = resultSet.getString("fol_not_rec");
                msBachocoSociedad = resultSet.getString("bac_soc");
                msBachocoOrganizacionCompra = resultSet.getString("bac_org");
                msBachocoDivision = resultSet.getString("bac_div");
                mnSorianaTienda = resultSet.getInt("sor_tda");
                mnSorianaEntregaMercancia = resultSet.getInt("sor_ent_mer");
                mtSorianaRemisionFecha = resultSet.getDate("sor_rem_fec");
                msSorianaRemisionFolio = resultSet.getString("sor_rem_fol");
                msSorianaPedidoFolio = resultSet.getString("sor_ped_fol");
                mnSorianaBultoTipo = resultSet.getInt("sor_bto_tp");
                mdSorianaBultoCantidad = resultSet.getDouble("sor_bto_can");
                msSorianaNotaEntradaFolio = resultSet.getString("sor_not_ent_fol");
                msModeloDpsDescripcion = resultSet.getString("dps_description");
                mnCfdAddendaSubtype = resultSet.getInt("stp_cfd_add");
                mnFkCfdAddendaTypeId = resultSet.getInt("fid_tp_cfd_add");
            }

            oStatementAux = statement.getConnection().createStatement();

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;

            // Read Addenda entries:
            sql = "SELECT id_year, id_doc, id_ety FROM trn_dps_add_ety " +
                    "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " " +
                    "ORDER BY bac_num_pos, id_ety ";
            resultSet = statement.executeQuery(sql);
            SDataDpsAddendaEntry entry = new SDataDpsAddendaEntry();
            while (resultSet.next()) {
                if (entry.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_ety") }, oStatementAux) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mvDbmsDpsAddEntries.add(entry);
                }
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        String sql = "";
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();

            sql = "DELETE FROM trn_dps_add WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
            statement.execute(sql);

            sql = "INSERT INTO trn_dps_add (id_year, id_doc, " +
                    "fol_not_rec, bac_soc, bac_org, bac_div, sor_tda, sor_ent_mer, sor_rem_fec, sor_rem_fol, sor_ped_fol, sor_bto_tp, sor_bto_can, sor_not_ent_fol, dps_description, stp_cfd_add, fid_tp_cfd_add) " +
                    "VALUES (" + mnPkYearId + ", " + mnPkDocId + ", '" + msLorealFolioNotaRecepcion +
                    "', '" + msBachocoSociedad + "', '" + msBachocoOrganizacionCompra +
                    "', '" + msBachocoDivision + "', " + mnSorianaTienda +
                    ", " + mnSorianaEntregaMercancia + ", '" + SLibUtils.DbmsDateFormatDate.format(mtSorianaRemisionFecha) +
                    "', '" + msSorianaRemisionFolio + "', '" + msSorianaPedidoFolio +
                    "', " + mnSorianaBultoTipo + ", " + mdSorianaBultoCantidad +
                    ", '" + msSorianaNotaEntradaFolio + "', '" + msModeloDpsDescripcion +
                    "', " + mnCfdAddendaSubtype + ", " + mnFkCfdAddendaTypeId + ")";
            statement.execute(sql);

            mnDbmsErrorId = 0;
            msDbmsError = "";

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                for (SDataDpsAddendaEntry dpsAddEntry : mvDbmsDpsAddEntries) {
                    if (dpsAddEntry.getIsRegistryNew() || dpsAddEntry.getIsRegistryEdited()) {
                        if (dpsAddEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }
            }
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int delete(Connection connection) {
        String sql = "";
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();

            sql = "DELETE FROM trn_dps_add WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
            statement.execute(sql);

            mnDbmsErrorId = 0;
            msDbmsError = "";

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                for (SDataDpsAddendaEntry dpsAddEntry : mvDbmsDpsAddEntries) {
                    if (dpsAddEntry.delete(connection) != SLibConstants.DB_ACTION_DELETE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_DELETE_DEP);
                        }
                }
            }
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
