package org.roko.erp.model.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.roko.erp.model.GeneralJournalBatch;

@Embeddable
public class GeneralJournalBatchLineId implements Serializable {

    @ManyToOne
    private GeneralJournalBatch generalJournalBatch;

    private int lineNo;

    public GeneralJournalBatch getGeneralJournalBatch() {
        return generalJournalBatch;
    }

    public void setGeneralJournalBatch(GeneralJournalBatch generalJournalBatch) {
        this.generalJournalBatch = generalJournalBatch;
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
        result = prime * result + ((generalJournalBatch == null) ? 0 : generalJournalBatch.hashCode());
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
        if (generalJournalBatch == null) {
            if (other.generalJournalBatch != null)
                return false;
        } else if (!generalJournalBatch.equals(other.generalJournalBatch))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }
}
