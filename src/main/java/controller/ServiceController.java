package controller;

import model.HairService;
import model.Service;
import model.SkinService;
import repository.ServiceFileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ServiceController {

    public final ServiceFileRepository repository;

    private ServiceController(ServiceFileRepository repository){
        this.repository=repository;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/services";
    }

    @GetMapping("/service")
    public String viewservices(@RequestParam(required = false)String category,Model model){

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

    @PostMapping("/service/update/{id}")
    public String updateService(@PathVariable String id,
                                @RequestParam String type,
                                @RequestParam String name,
                                @RequestParam String category,
                                @RequestParam double price,
                                @RequestParam int duration,
                                @RequestParam String description,
                                @RequestParam String extravalue) {
        Service updateService;

        if(type.equalsIgnoreCase("HAIR")){
            updateService=new HairService(id,name,category,price,duration,description,extravalue);
        }else{
            updateService=new SkinService(id,name,category,price,duration,description,extravalue);
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
