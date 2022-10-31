package org.roko.erp.controllers;

import java.util.Date;
import java.util.List;

import org.roko.erp.controllers.model.SalesOrderLineModel;
import org.roko.erp.controllers.model.SalesOrderModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.model.Item;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.SalesOrderLineId;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.SalesOrderLineService;
import org.roko.erp.services.SalesOrderService;
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
public class SalesOrderController {
    
    private SalesOrderService svc;
    private PagingService pagingSvc;
    private CustomerService customerSvc;
    private PaymentMethodService paymentMethodSvc;
    private SalesOrderLineService salesOrderLineSvc;
    private ItemService itemSvc;

    @Autowired
    public SalesOrderController(SalesOrderService svc, PagingService pagingSvc, CustomerService customerSvc,
            PaymentMethodService paymentMethodSvc, SalesOrderLineService salesOrderLineSvc, ItemService itemSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.customerSvc = customerSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.salesOrderLineSvc = salesOrderLineSvc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/salesOrderList")
    public String list(@RequestParam(name="page", required=false) Long page, Model model){
        List<SalesOrder> salesOrders = svc.list();
        PagingData pagingData = pagingSvc.generate("salesOrder", page, svc.count());

        model.addAttribute("salesOrders", salesOrders);
        model.addAttribute("paging", pagingData);

        return "salesOrderList.html";
    }

    @GetMapping("/salesOrderWizard")
    public String wizard(@RequestParam(name="code", required=false) String code, Model model){
        SalesOrderModel salesOrderModel = new SalesOrderModel();

        if (code != null){
            SalesOrder salesOrder = svc.get(code);
            toModel(salesOrder, salesOrderModel);
        }

        model.addAttribute("salesOrder", salesOrderModel);
        model.addAttribute("customers", customerSvc.list());
        
        return "salesOrderWizardFirstPage.html";
    }

    @PostMapping("/salesOrderWizardFirstPage")
    public String postWizardFirstPage(@ModelAttribute SalesOrderModel salesOrder, Model model) {
        Customer customer = customerSvc.get(salesOrder.getCustomerCode());

        salesOrder.setPaymentMethodCode(customer.getPaymentMethod().getCode());
        salesOrder.setDate(new Date());
        salesOrder.setCustomerName(customer.getName());

        model.addAttribute("salesOrder", salesOrder);
        model.addAttribute("paymentMethods", paymentMethodSvc.list());

        return "salesOrderWizardSecondPage.html";
    }

    @PostMapping("/salesOrderWizardSecondPage")
    public RedirectView postWizardSecondPage(@ModelAttribute SalesOrderModel salesOrderModel){
        if (salesOrderModel.getCode().isEmpty()) {
            SalesOrder salesOrder = fromModel(salesOrderModel);

            svc.create(salesOrder);
        } else {
            SalesOrder salesOrder = svc.get(salesOrderModel.getCode());

            fromModel(salesOrder, salesOrderModel);

            svc.update(salesOrderModel.getCode(), salesOrder);
        }

        return new RedirectView("/salesOrderList");
    }

    @GetMapping("/deleteSalesOrder")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/salesOrderList");
    }

    @GetMapping("/salesOrderCard")
    public String card(@RequestParam(name="code") String code, Model model) {
        SalesOrder salesOrder = svc.get(code);
        List<SalesOrderLine> salesOrderLineList = salesOrderLineSvc.list(salesOrder);
        PagingData salesOrderLinePagingData = pagingSvc.generate("salesOrderLine", null, salesOrderLineSvc.count(salesOrder));

        model.addAttribute("salesOrder", salesOrder);
        model.addAttribute("salesOrderLines", salesOrderLineList);
        model.addAttribute("paging", salesOrderLinePagingData);

        return "salesOrderCard.html";
    }

    @GetMapping("/salesOrderLineWizard")
    public String salesOrderLineWizard(@RequestParam(name="salesOrderCode") String salesOrderCode, Model model){
        SalesOrderLineModel salesOrderLineModel = new SalesOrderLineModel();
        salesOrderLineModel.setSalesOrderCode(salesOrderCode);

        model.addAttribute("salesOrderLine", salesOrderLineModel);
        model.addAttribute("items", itemSvc.list());

        return "salesOrderLineWizardFirstPage.html";
    }

    @PostMapping("/salesOrderLineWizardFirstPage")
    public String postSalesOrderLineWizardFirstPage(@ModelAttribute SalesOrderLineModel salesOrderLine, Model model){
        Item item = itemSvc.get(salesOrderLine.getItemCode());

        salesOrderLine.setItemName(item.getName());
        salesOrderLine.setPrice(item.getSalesPrice());

        model.addAttribute("salesOrderLine", salesOrderLine);
        
        return "salesOrderLineWizardSecondPage.html";
    }

    @PostMapping("/salesOrderLineWizardSecondPage")
    public String postSalesOrderLineWizardSecondPage(@ModelAttribute SalesOrderLineModel salesOrderLine, Model model){
        salesOrderLine.setAmount(salesOrderLine.getQuantity() * salesOrderLine.getPrice());

        model.addAttribute("salesOrderLine", salesOrderLine);

        return "salesOrderLineWizardThirdPage.html";
    }

    @PostMapping("/salesOrderLineWizardThirdPage")
    public RedirectView postSalesOrderLineWizardThirdPage(@ModelAttribute SalesOrderLineModel salesOrderLine,
            RedirectAttributes redirectAttributes) {
        
        SalesOrder salesOrder = svc.get(salesOrderLine.getSalesOrderCode());

        long lineNo = salesOrderLineSvc.count(salesOrder) + 1;

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrder);
        salesOrderLineId.setLineNo((int)lineNo);

        SalesOrderLine salesOrderLineToCreate = new SalesOrderLine();
        salesOrderLineToCreate.setSalesOrderLineId(salesOrderLineId);
        salesOrderLineToCreate.setItem(itemSvc.get(salesOrderLine.getItemCode()));
        salesOrderLineToCreate.setQuantity(salesOrderLine.getQuantity());
        salesOrderLineToCreate.setPrice(salesOrderLine.getPrice());
        salesOrderLineToCreate.setAmount(salesOrderLine.getAmount());

        salesOrderLineSvc.create(salesOrderLineToCreate);

        redirectAttributes.addAttribute("code", salesOrderLine.getSalesOrderCode());

        return new RedirectView("/salesOrderCard");
    }

    private SalesOrder fromModel(SalesOrderModel salesOrderModelMock) {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCode("SO" + System.currentTimeMillis());
        salesOrder.setCustomer(customerSvc.get(salesOrderModelMock.getCustomerCode()));
        salesOrder.setDate(salesOrderModelMock.getDate());
        salesOrder.setPaymentMethod(paymentMethodSvc.get(salesOrderModelMock.getPaymentMethodCode()));
        return salesOrder;
    }

    private void toModel(SalesOrder salesOrder, SalesOrderModel salesOrderModel) {
        salesOrderModel.setCode(salesOrder.getCode());
        salesOrderModel.setCustomerCode(salesOrder.getCustomer().getCode());
        salesOrderModel.setDate(salesOrder.getDate());
        salesOrderModel.setPaymentMethodCode(salesOrder.getPaymentMethod().getCode());
    }

    private void fromModel(SalesOrder salesOrder, SalesOrderModel salesOrderModel) {
        salesOrder.setCustomer(customerSvc.get(salesOrderModel.getCustomerCode()));
        salesOrder.setDate(salesOrderModel.getDate());
        salesOrder.setPaymentMethod(paymentMethodSvc.get(salesOrderModel.getPaymentMethodCode()));
    }

}
