package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.services.ItemService;
import org.roko.erp.model.dto.ItemDTO;
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

    @Autowired
    public ItemController(ItemService svc) {
        this.svc = svc;
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
