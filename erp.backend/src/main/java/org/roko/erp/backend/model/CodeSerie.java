package org.roko.erp.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CodeSerie {
    
    @Id
    private String code = "";

    private String name = "";

    private String firstCode = "";
    private String lastCode = "";
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getFirstCode() {
        return firstCode;
    }
    public void setFirstCode(String firstCode) {
        this.firstCode = firstCode;
    }
    public String getLastCode() {
        return lastCode;
    }
    public void setLastCode(String lastCode) {
        this.lastCode = lastCode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
