package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.list.ItemLedgerEntryList;
import org.roko.erp.dto.list.ItemList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.ItemLedgerEntryService;
import org.roko.erp.frontend.services.ItemService;
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
        ItemList itemList = svc.list(page);
        PagingData generate = pagingSvc.generate("item", page, (int) itemList.getCount());

        model.addAttribute("items", itemList.getData());
        model.addAttribute("paging", generate);

        return "itemList.html";
    }

    @GetMapping("/itemCard")
    public String card(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        ItemDTO item = new ItemDTO();

        if (code != null) {
            item = svc.get(code);

            ItemLedgerEntryList itemLedgerEntryList = itemLedgerEntrySvc.list(code, page);

            model.addAttribute("itemLedgerEntries", itemLedgerEntryList.getData());
            model.addAttribute("paging",
                    pagingSvc.generate("itemCard", code, page, (int) itemLedgerEntryList.getCount()));
        }

        model.addAttribute("item", item);

        return "itemCard.html";
    }

    @PostMapping("/itemCard")
    public RedirectView postCard(@ModelAttribute ItemDTO item) {
        ItemDTO itemFromDB = svc.get(item.getCode());

        if (itemFromDB == null) {
            svc.create(item);
        } else {
            svc.update(item.getCode(), item);
        }

        return new RedirectView("/itemList");
    }

    @GetMapping("/deleteItem")
    public RedirectView delete(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        svc.delete(code);

        redirectAttributes.addAttribute("page", page);

        return new RedirectView("/itemList");
    }
}
