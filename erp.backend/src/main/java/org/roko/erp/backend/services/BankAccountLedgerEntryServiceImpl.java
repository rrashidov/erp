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
    public BankAccountLedgerEntryList list(BankAccount bankAccount, int page) {
        List<BankAccountLedgerEntryDTO> data = repo
                .findFor(bankAccount, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList().stream()
                .map(x -> toDto(x))
                .collect(Collectors.toList());
        BankAccountLedgerEntryList list = new BankAccountLedgerEntryList();
        list.setData(data);
        list.setCount(repo.count(bankAccount));
        return list;
    }

    private BankAccountLedgerEntryDTO toDto(BankAccountLedgerEntry bankAccountLedgerEntry) {
        BankAccountLedgerEntryDTO dto = new BankAccountLedgerEntryDTO();
        dto.setId(bankAccountLedgerEntry.getId());
        dto.setType(bankAccountLedgerEntry.getType().name());
        dto.setDocumentCode(bankAccountLedgerEntry.getDocumentCode());
        dto.setDate(bankAccountLedgerEntry.getDate());
        dto.setAmount(bankAccountLedgerEntry.getAmount());
        return dto;
    }
}
