package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.model.Item;
import org.roko.erp.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    private ItemService svc;

    @Autowired
    public ItemController(ItemService svc) {
        this.svc = svc;
    }

    @GetMapping("/itemList")
    public String list(@RequestParam(name = "page", required = false) Integer page, Model model){
        List<Item> list = svc.list();
        Item i = new Item();
        i.setCode("item00");
        i.setDescription("first item");
        i.setSalesPrice(12.12);
        i.setPurchasePrice(23.34);
        i.setInventory(123.23);
        list.add(i);
        model.addAttribute("items", list);

        return "itemList.html";
    }
}
