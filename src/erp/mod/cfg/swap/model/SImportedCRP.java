package erp.mod.cfg.swap.model;

import com.fasterxml.jackson.databind.JsonNode;
import erp.mod.cfg.swap.SSwapUtils;
import java.io.Serializable;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 * In memory CRP document received from SWAP Services.
 *
 * @author Edwin Carmona
 */
public class SImportedCRP implements SGridRow, Serializable, Comparable<SImportedCRP> {

    public int externalDocumentId;
    public String externalDocumentUuid;
    public int bizPartnerId;
    public String bizPartner;
    public String numberSeries;
    public String number;
    public Date date;
    public double total;
    public int currencyId;
    public String currencyCode;
    public int functionalSubAreaId;
    public String functionalSubArea;
    public String fiscalUseCode;
    public boolean download;
    public boolean alreadyDownloaded;
    public int filesCount;

    public SImportedCRP() {
        externalDocumentId = 0;
        externalDocumentUuid = "";
        bizPartnerId = 0;
        bizPartner = "";
        numberSeries = "";
        number = "";
        date = null;
        total = 0;
        currencyId = 0;
        currencyCode = "";
        functionalSubAreaId = 0;
        functionalSubArea = "";
        fiscalUseCode = "";
        download = false;
        alreadyDownloaded = false;
        filesCount = 0;
    }

    public SImportedCRP(final JsonNode docNode, final int externalId, final int funcSubAreaId, final boolean alreadyDownloaded) throws Exception {
        this();

        externalDocumentId = externalId;
        externalDocumentUuid = docNode.has("uuid") && !docNode.path("uuid").isNull() ? docNode.path("uuid").asText() : "";

        JsonNode partnerNode = docNode.path("partner");
        bizPartnerId = partnerNode.get("external_id").asInt();
        bizPartner = partnerNode.get("full_name").asText();
        
        number = docNode.get("folio").asText();
        if (number.isEmpty()) {
            numberSeries = docNode.get("series").asText();
            number = docNode.get("number").asText();
        }

        date = SLibUtils.IsoFormatDate.parse(docNode.get("date").asText());
        total = SLibUtils.parseDouble(docNode.get("amount").asText());

        JsonNode currencyNode = docNode.path("currency");
        currencyId = SSwapUtils.getCurrencyId(currencyNode.get("id").asInt());
        currencyCode = currencyNode.get("code").asText();

        functionalSubAreaId = funcSubAreaId;
        functionalSubArea = docNode.path("functional_area").get("name").asText();
        fiscalUseCode = docNode.get("fiscal_use").asText();

        JsonNode filesNode = docNode.path("files");
        filesCount = filesNode.isArray() ? filesNode.size() : 0;

        this.alreadyDownloaded = alreadyDownloaded;
    }

    public String getFolio() {
        if (!numberSeries.isEmpty() || !number.isEmpty()) {
            return (numberSeries.isEmpty() ? "" : numberSeries + "-") + number;
        }
        return externalDocumentUuid.isEmpty() ? "" : externalDocumentUuid;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[]{externalDocumentId};
    }

    @Override
    public String getRowCode() {
        return getFolio();
    }

    @Override
    public String getRowName() {
        return bizPartner;
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
    }

    @Override
    public Object getRowValueAt(int col) {
        switch (col) {
            case 0:
                return bizPartner;
            case 1:
                return getFolio();
            case 2:
                return date;
            case 3:
                return total;
            case 4:
                return currencyCode;
            case 5:
                return download;
            case 6:
                return alreadyDownloaded;
            case 7:
                return functionalSubArea;
            case 8:
                return fiscalUseCode;
            case 9:
                return externalDocumentUuid;
            case 10:
                return filesCount;
            default:
                return null;
        }
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        if (col == 5) {
            download = (Boolean) value;
        }
    }

    @Override
    public int compareTo(SImportedCRP o) {
        return this.toString().compareTo(o.toString());
    }
}
