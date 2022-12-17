package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.services.VendorLedgerEntryService;
import org.roko.erp.backend.services.VendorService;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.VendorLedgerEntryDTO;
import org.roko.erp.dto.list.VendorLedgerEntryList;
import org.roko.erp.dto.list.VendorList;
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
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private VendorService svc;
    private VendorLedgerEntryService vendorLedgerEntrySvc;

    @Autowired
    public VendorController(VendorService svc, VendorLedgerEntryService vendorLedgerEntrySvc) {
        this.svc = svc;
        this.vendorLedgerEntrySvc = vendorLedgerEntrySvc;
    }

    @GetMapping
    public VendorList list() {
        List<VendorDTO> data = svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        VendorList list = new VendorList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/page/{page}")
    public VendorList list(@PathVariable("page") int page) {
        List<VendorDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        VendorList list = new VendorList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public VendorDTO get(@PathVariable("code") String code) {
        Vendor vendor = svc.get(code);

        if (vendor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return svc.toDTO(vendor);
    }

    @GetMapping("/{code}/ledgerentries/page/{page}")
    public VendorLedgerEntryList listLedgerEntries(@PathVariable("code") String code,
            @PathVariable("page") int page) {
        Vendor vendor = svc.get(code);

        List<VendorLedgerEntryDTO> data = vendorLedgerEntrySvc.findFor(vendor, page).stream()
                .map(x -> vendorLedgerEntrySvc.toDTO(x))
                .collect(Collectors.toList());

        VendorLedgerEntryList list = new VendorLedgerEntryList();
        list.setData(data);
        list.setCount(vendorLedgerEntrySvc.count(vendor));
        return list;
    }

    @PostMapping
    public String post(@RequestBody VendorDTO dto) {
        Vendor vendor = svc.fromDTO(dto);
        svc.create(vendor);
        return vendor.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody VendorDTO dto) {
        Vendor vendor = svc.fromDTO(dto);
        svc.update(code, vendor);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
