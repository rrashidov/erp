package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.BankAccount;
import org.roko.erp.model.BankAccountLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountLedgerEntryRepository extends JpaRepository<BankAccountLedgerEntry, Long> {

    @Query("SELECT bankAccountLedgerEntry FROM BankAccountLedgerEntry bankAccountLedgerEntry WHERE bankAccountLedgerEntry.bankAccount = :bankAccount")
    public List<BankAccountLedgerEntry> findFor(@Param("bankAccount") BankAccount bankAccount);

    @Query("SELECT COUNT(bankAccountLedgerEntry) FROM BankAccountLedgerEntry bankAccountLedgerEntry WHERE bankAccountLedgerEntry.bankAccount = :bankAccount")
    public long count(@Param("bankAccount") BankAccount bankAccount);

}
