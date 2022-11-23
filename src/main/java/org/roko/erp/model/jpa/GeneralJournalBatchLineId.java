package org.roko.erp.model.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.roko.erp.model.GeneralJournalBatch;

@Embeddable
public class GeneralJournalBatchLineId implements Serializable {

    @ManyToOne
    private GeneralJournalBatch generalJournalbatch;

    private int lineNo;

    public GeneralJournalBatch getGeneralJournalbatch() {
        return generalJournalbatch;
    }

    public void setGeneralJournalbatch(GeneralJournalBatch generalJournalbatch) {
        this.generalJournalbatch = generalJournalbatch;
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
        result = prime * result + ((generalJournalbatch == null) ? 0 : generalJournalbatch.hashCode());
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
        GeneralJournalBatchLineId other = (GeneralJournalBatchLineId) obj;
        if (generalJournalbatch == null) {
            if (other.generalJournalbatch != null)
                return false;
        } else if (!generalJournalbatch.equals(other.generalJournalbatch))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }
    
}
