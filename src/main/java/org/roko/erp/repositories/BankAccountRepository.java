package org.roko.erp.repositories;

import org.roko.erp.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    @Query("SELECT COALESCE(SUM(bankAccountLedgerEntry.amount), 0) FROM BankAccountLedgerEntry bankAccountLedgerEntry WHERE bankAccountLedgerEntry.bankAccount = :bankAccount")
    public double balance(@Param("bankAccount") BankAccount bankAccount);
}
