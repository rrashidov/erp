package org.roko.erp.controllers;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Item;
import org.roko.erp.services.ItemLedgerEntryService;
import org.roko.erp.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ItemController {

    private ItemService svc;
    private PagingService pagingSvc;
    private ItemLedgerEntryService itemLedgerEntrySvc;

    @Autowired
    public ItemController(ItemService svc, PagingService pagingSvc, ItemLedgerEntryService itemLedgerEntrySvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.itemLedgerEntrySvc = itemLedgerEntrySvc;
    }

    @GetMapping("/itemList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        model.addAttribute("items", svc.list(page));

        PagingData generate = pagingSvc.generate("item", page, svc.count());

        model.addAttribute("paging", generate);

        return "itemList.html";
    }

    @GetMapping("/itemCard")
    public String card(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        Item item = new Item();

        if (code != null) {
            item = svc.get(code);

            model.addAttribute("itemLedgerEntries", itemLedgerEntrySvc.list(item, page));
            model.addAttribute("paging", pagingSvc.generate("itemCard", code, page, itemLedgerEntrySvc.count(item)));
        }

        model.addAttribute("item", item);

        return "itemCard.html";
    }

    @PostMapping("/itemCard")
    public RedirectView postCard(@ModelAttribute Item item, Model model,
            RedirectAttributes attributes) {
        Item itemFromDB = svc.get(item.getCode());

        if (itemFromDB == null) {
            itemFromDB = new Item();
            itemFromDB.setCode(item.getCode());
        }

        itemFromDB.setName(item.getName());
        itemFromDB.setSalesPrice(item.getSalesPrice());
        itemFromDB.setPurchasePrice(item.getPurchasePrice());

        svc.create(itemFromDB);

        return new RedirectView("/itemList");
    }

    @GetMapping("/deleteItem")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/itemList");
    }
}
