/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataBomLevelRow extends erp.lib.table.STableRow {

    public SDataBomLevelRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBom data = (SDataBom) moData;

        mvValues.clear();
        mvValues.add(data.getDbmsItem());
        mvValues.add(data.getLevel());
        mvValues.add(data.getDbmsItemKeyRm());
        mvValues.add(data.getDbmsItemRm());
        mvValues.add(data.getQuantity());
        mvValues.add(data.getDbmsItemRmUnitSymbol());
        mvValues.add(data.getPercentage());
        mvValues.add(data.getCost());
        mvValues.add(data.getDateStart());
        mvValues.add(data.getDateEnd_n());
        mvValues.add(data.getDbmsBomSubstitute().size()>0 ? data.getDbmsBomSubstitute().get(0).getDbmsItemSubstituteKey() : "");
        mvValues.add(data.getDbmsBomSubstitute().size()>0 ? data.getDbmsBomSubstitute().get(0).getDbmsItemSubstitute() : "");
        mvValues.add(data.getDbmsBomSubstitute().size()>0 ? data.getDbmsBomSubstitute().get(0).getDbmsUnitSubstitute() : "");
        mvValues.add(data.getDbmsBomSubstitute().size()>0 ? data.getDbmsBomSubstitute().get(0).getPercentage() : 0);
        mvValues.add(data.getDbmsBomSubstitute().size()>0 ? data.getDbmsBomSubstitute().get(0).getPercentageMax() : 0);
        mvValues.add(data.getIsNotExplotion());
        mvValues.add(data.getIsRequested());
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}
