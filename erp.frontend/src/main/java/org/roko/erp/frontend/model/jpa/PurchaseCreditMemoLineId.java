package org.roko.erp.frontend.model.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.roko.erp.frontend.model.PurchaseCreditMemo;

@Embeddable
public class PurchaseCreditMemoLineId implements Serializable {

    @ManyToOne
    private PurchaseCreditMemo purchaseCreditMemo;

    private int lineNo;

    public PurchaseCreditMemo getPurchaseCreditMemo() {
        return purchaseCreditMemo;
    }

    public void setPurchaseCreditMemo(PurchaseCreditMemo purchaseCreditMemo) {
        this.purchaseCreditMemo = purchaseCreditMemo;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int l) {
        this.lineNo = l;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((purchaseCreditMemo == null) ? 0 : purchaseCreditMemo.hashCode());
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
        PurchaseCreditMemoLineId other = (PurchaseCreditMemoLineId) obj;
        if (purchaseCreditMemo == null) {
            if (other.purchaseCreditMemo != null)
                return false;
        } else if (!purchaseCreditMemo.equals(other.purchaseCreditMemo))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }


}
