package controller;

import model.HairService;
import model.Service;
import model.SkinService;
import org.springframework.beans.factory.annotation.Autowired;
import repository.ServiceFileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class ServiceController {

    public final ServiceFileRepository repository;

    @Autowired
    public ServiceController(ServiceFileRepository repository){
        this.repository=repository;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/services";
    }

    @GetMapping("/services")
    public String viewServices(@RequestParam(required = false)String category,Model model){

        List<Service> services;

        if(category != null && !category.isEmpty()){
            services=repository.finfByCategory(category);
        }else {
            services=repository.findAll();
        }
        model.addAttribute("services",services);
        model.addAttribute("category",category);

        return "service-list";
    }



    @GetMapping("/services/new")
    public String showAddForm() {
        return "service";
    }

    @PostMapping("/services")
    public String addService(@RequestParam String type,
                             @RequestParam String name,
                             @RequestParam String category,
                             @RequestParam double price,
                             @RequestParam int duration,
                             @RequestParam String description,
                             @RequestParam String extraValue) {

        Service service;

        if (type.equalsIgnoreCase("HAIR")) {
            service = new HairService(null, name, category, price, duration, description, extraValue);
        } else {
            service = new SkinService(null, name, category, price, duration, description, extraValue);
        }

        repository.save(service);

        return "redirect:/services";
    }

    @GetMapping("/services/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Service service = repository.findById(id);

        if (service == null) {
            return "redirect:/services";
        }

        model.addAttribute("service", service);

        return "service-edit";
    }



    @PostMapping("/services/update/{id}")
    public String updateService(@PathVariable String id,
                                @RequestParam String type,
                                @RequestParam String name,
                                @RequestParam String category,
                                @RequestParam double price,
                                @RequestParam int duration,
                                @RequestParam String description,
                                @RequestParam String extraValue) {
        Service updateService;

        if(type.equalsIgnoreCase("HAIR")){
            updateService=new HairService(id,name,category,price,duration,description,extraValue);
        }else{
            updateService=new SkinService(id,name,category,price,duration,description,extraValue);
        }

        repository.update(updateService);

        return "redirect:/services";

    }

    @PostMapping("/services/delete/{id}")
    public String deleteService(@PathVariable String id){
        repository.deleteById(id);
        return "redirect:/services";
    }


}