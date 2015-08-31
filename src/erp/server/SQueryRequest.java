/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

import erp.lib.table.STableField;
import java.io.Serializable;
import java.util.ArrayList;
import sa.lib.SLibRpnArgument;

/**
 *
 * @author Sergio Flores
 */
public class SQueryRequest implements Serializable {

    private ArrayList<STableField> maPrimaryKeyFields;
    private ArrayList<STableField> maQueryFields;
    private ArrayList<STableField> maQueryAdditonalFields;
    private ArrayList<SLibRpnArgument>[] maaRpnArguments;
    private String[] masQuerySentences;

     /**
     * Creates query request object to retrieve rows for views.
     * @param primaryKeyFields Primary Key fields of view.
     * @param queryFields  Query fields of view.
     * @param queryAdditionalFields Additional query fields of view.
     * @param rpnArguments Reverse Polish Notation computed fields of view.
     * @param querySentences SQL query sentences. Normally array is composed of one element, the SELECT sentence.
     * When composed of more than one element, the first ones are local variable declarations, and the very last one must be the SELECT sentence.
     */
   public SQueryRequest(ArrayList<STableField> primaryKeyFields, ArrayList<STableField> queryFields, ArrayList<STableField> queryAdditionalFiels, ArrayList<SLibRpnArgument>[] rpnArguments, String[] querySentences) {
        maPrimaryKeyFields = primaryKeyFields;
        maQueryFields = queryFields;
        maQueryAdditonalFields = queryAdditionalFiels;
        maaRpnArguments = rpnArguments;
        masQuerySentences = querySentences;
    }

    public ArrayList<STableField> getPrimaryKeyFields() { return maPrimaryKeyFields; }
    public ArrayList<STableField> getQueryFields() { return maQueryFields; }
    public ArrayList<STableField> getQueryAdditionalFields() { return maQueryAdditonalFields; }
    public ArrayList<SLibRpnArgument>[] getRpnArguments() { return maaRpnArguments; }
    public String[] getQuerySentences() { return masQuerySentences; }
}
