package org.roko.erp.backend.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.backend.model.ItemLedgerEntryType;
import org.roko.erp.backend.services.ItemLedgerEntryService;
import org.roko.erp.backend.services.ItemService;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.ItemLedgerEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {
   
    private ItemService svc;
    private ItemLedgerEntryService itemLedgerEntrySvc;

    @Autowired
    public ItemController(ItemService svc, ItemLedgerEntryService itemLedgerEntrySvc) {
        this.svc = svc;
        this.itemLedgerEntrySvc = itemLedgerEntrySvc;
	}

    @GetMapping
    public List<ItemDTO> list() {
        return svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/page/{page}")
    public List<ItemDTO> list(@PathVariable("page") int page) {
        return svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public ItemDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/ledgerentries/page/{page}")
    public List<ItemLedgerEntryDTO> ledgerEntries(@PathVariable("code") String code, @PathVariable("page") int page) {
        Item item = svc.get(code);

        ItemLedgerEntry itemLedgerEntry = new ItemLedgerEntry();
        itemLedgerEntry.setItem(item);
        itemLedgerEntry.setType(ItemLedgerEntryType.PURCHASE_ORDER);
        itemLedgerEntry.setQuantity(12.12);
        itemLedgerEntry.setDate(new Date());
        itemLedgerEntry.setDocumentCode("PPO" + System.currentTimeMillis());
        itemLedgerEntrySvc.create(itemLedgerEntry);

        return itemLedgerEntrySvc.list(item, page).stream()
            .map(x -> itemLedgerEntrySvc.toDTO(x))
            .collect(Collectors.toList());
    }

    @PostMapping
    public String post(@RequestBody ItemDTO itemDTO) {
        Item item = svc.fromDTO(itemDTO);
        svc.create(item);
        return item.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody ItemDTO itemDTO){
        Item item = svc.fromDTO(itemDTO);
        svc.update(code, item);
        return item.getCode();
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
