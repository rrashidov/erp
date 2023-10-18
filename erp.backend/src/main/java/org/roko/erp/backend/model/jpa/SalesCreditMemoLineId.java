package org.roko.erp.backend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import org.roko.erp.backend.model.SalesCreditMemo;

@Embeddable
public class SalesCreditMemoLineId implements Serializable {

    @ManyToOne
    private SalesCreditMemo salesCreditMemo;

    private int lineNo;

    public SalesCreditMemo getSalesCreditMemo() {
        return salesCreditMemo;
    }

    public void setSalesCreditMemo(SalesCreditMemo salesCreditMemo) {
        this.salesCreditMemo = salesCreditMemo;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((salesCreditMemo == null) ? 0 : salesCreditMemo.hashCode());
        result = prime * result + lineNo;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SalesCreditMemoLineId other = (SalesCreditMemoLineId) obj;
        if (salesCreditMemo == null) {
            if (other.salesCreditMemo != null)
                return false;
        } else if (!salesCreditMemo.equals(other.salesCreditMemo))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }
}
