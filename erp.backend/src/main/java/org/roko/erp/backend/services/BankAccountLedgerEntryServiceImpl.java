package org.roko.erp.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.BankAccountLedgerEntry;
import org.roko.erp.backend.repositories.BankAccountLedgerEntryRepository;
import org.roko.erp.dto.BankAccountLedgerEntryDTO;
import org.roko.erp.dto.list.BankAccountLedgerEntryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BankAccountLedgerEntryServiceImpl implements BankAccountLedgerEntryService {

    private BankAccountLedgerEntryRepository repo;

    @Autowired
    public BankAccountLedgerEntryServiceImpl(BankAccountLedgerEntryRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(BankAccountLedgerEntry bankAccountLedgerEntry) {
        repo.save(bankAccountLedgerEntry);
    }

    @Override
    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount) {
        return repo.findFor(bankAccount);
    }

    @Override
    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount, int page) {
        return repo.findFor(bankAccount, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count(BankAccount bankAccount) {
        return new Long(repo.count(bankAccount)).intValue();
    }

    @Override
    public BankAccountLedgerEntryDTO toDTO(BankAccountLedgerEntry bankAccountLedgerEntry) {
        BankAccountLedgerEntryDTO dto = new BankAccountLedgerEntryDTO();
        dto.setId(bankAccountLedgerEntry.getId());
        dto.setType(bankAccountLedgerEntry.getType().name());
        dto.setDocumentCode(bankAccountLedgerEntry.getDocumentCode());
        dto.setDate(bankAccountLedgerEntry.getDate());
        dto.setAmount(bankAccountLedgerEntry.getAmount());
        return dto;
    }
}
