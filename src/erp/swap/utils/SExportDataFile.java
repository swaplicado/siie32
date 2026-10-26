/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Isabel Servín, Edwin Carmona
 */
public class SExportDataFile implements SExportData {
    
    @JsonProperty("filename_storage")
    public String filenameStorage;
    
    @JsonProperty("filename_original")
    public String filenameOriginal;
    
    @JsonProperty("url_storage")
    public String urlStorage;
    
    @JsonProperty("url_database")
    public String urlDatabase;
    
    @JsonProperty("bucket_name")
    public String bucketName;
    
    @JsonProperty("file_type_id")
    public int fileTypeId;
}
