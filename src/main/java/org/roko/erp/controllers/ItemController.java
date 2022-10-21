package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Item;
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

    @Autowired
    public ItemController(ItemService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/itemList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model){
        model.addAttribute("items", svc.list());

        PagingData generate = pagingSvc.generate("item", page, svc.count());

        model.addAttribute("paging", generate);

        return "itemList.html";
    }

    @GetMapping("/itemCard")
    public String card(@RequestParam(name = "code", required = false) String code, Model model){
        Item item = new Item();

        if (code != null){
            item = svc.get(code);
        }

        model.addAttribute("item", item);

        return "itemCard.html";
    }

    @PostMapping("/itemCard")
    public RedirectView postCard(@ModelAttribute Item item, Model model, 
    RedirectAttributes attributes){
        Item itemFromDB = svc.get(item.getCode());

        if (itemFromDB == null){
            itemFromDB = new Item();
            itemFromDB.setCode(item.getCode());
        }

        itemFromDB.setDescription(item.getDescription());
        itemFromDB.setSalesPrice(item.getSalesPrice());
        itemFromDB.setPurchasePrice(item.getPurchasePrice());

        svc.create(itemFromDB);

        return new RedirectView("/itemList");
    }
}
