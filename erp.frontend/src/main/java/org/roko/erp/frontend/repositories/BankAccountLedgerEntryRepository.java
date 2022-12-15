package org.roko.erp.frontend.repositories;

import java.util.List;

import org.roko.erp.frontend.model.BankAccount;
import org.roko.erp.frontend.model.BankAccountLedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountLedgerEntryRepository extends JpaRepository<BankAccountLedgerEntry, Long> {

    @Query("SELECT bankAccountLedgerEntry FROM BankAccountLedgerEntry bankAccountLedgerEntry WHERE bankAccountLedgerEntry.bankAccount = :bankAccount")
    public List<BankAccountLedgerEntry> findFor(@Param("bankAccount") BankAccount bankAccount);

    @Query("SELECT bankAccountLedgerEntry FROM BankAccountLedgerEntry bankAccountLedgerEntry WHERE bankAccountLedgerEntry.bankAccount = :bankAccount")
    public Page<BankAccountLedgerEntry> findFor(@Param("bankAccount") BankAccount bankAccount, Pageable pageable);

    @Query("SELECT COUNT(bankAccountLedgerEntry) FROM BankAccountLedgerEntry bankAccountLedgerEntry WHERE bankAccountLedgerEntry.bankAccount = :bankAccount")
    public long count(@Param("bankAccount") BankAccount bankAccount);

}
