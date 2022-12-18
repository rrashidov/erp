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
import org.roko.erp.dto.list.ItemLedgerEntryList;
import org.roko.erp.dto.list.ItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public ItemList list() {
        List<ItemDTO> data = svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        ItemList list = new ItemList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/page/{page}")
    public ItemList list(@PathVariable("page") int page) {
        List<ItemDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        ItemList list = new ItemList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public ItemDTO get(@PathVariable("code") String code) {
        Item item = svc.get(code);

        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return svc.toDTO(item);
    }

    @GetMapping("/{code}/ledgerentries/page/{page}")
    public ItemLedgerEntryList ledgerEntries(@PathVariable("code") String code, @PathVariable("page") int page) {
        Item item = svc.get(code);

        ItemLedgerEntry itemLedgerEntry = new ItemLedgerEntry();
        itemLedgerEntry.setItem(item);
        itemLedgerEntry.setType(ItemLedgerEntryType.PURCHASE_ORDER);
        itemLedgerEntry.setQuantity(12.12);
        itemLedgerEntry.setDate(new Date());
        itemLedgerEntry.setDocumentCode("PPO" + System.currentTimeMillis());
        itemLedgerEntrySvc.create(itemLedgerEntry);

        List<ItemLedgerEntryDTO> data = itemLedgerEntrySvc.list(item, page).stream()
                .map(x -> itemLedgerEntrySvc.toDTO(x))
                .collect(Collectors.toList());

        ItemLedgerEntryList list = new ItemLedgerEntryList();
        list.setData(data);
        list.setCount(itemLedgerEntrySvc.count(item));
        return list;
    }

    @PostMapping
    public String post(@RequestBody ItemDTO itemDTO) {
        Item item = svc.fromDTO(itemDTO);
        svc.create(item);
        return item.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody ItemDTO itemDTO) {
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
