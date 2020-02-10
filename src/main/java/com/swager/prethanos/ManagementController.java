package com.swager.prethanos;

import com.swager.prethanos.entity.SwaggerSpec;
import com.swager.prethanos.repository.SwaggerSpecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("admin")
@Controller
public class ManagementController {

    @Autowired
    SwaggerSpecRepository swaggerSpecRepository;


    @GetMapping("/index.html")
    public String getManagementData(SwaggerSpec swaggerSpec, Model model) {

        model.addAttribute("swaggerSpecList", swaggerSpecRepository.findAllByOrderByPriorityAscNameAsc());
        model.addAttribute("swaggerSpec",getSwaggerSpec());
        return "management";
    }

    @PostMapping("/add-swagger.html")
    public String addSwaggerSpec(SwaggerSpec swaggerSpec, Model model) {

        swaggerSpecRepository.save(swaggerSpec);
        model.addAttribute("swaggerSpec",getSwaggerSpec());
        return "redirect:/admin/index.html";
    }

    @GetMapping("/edit.html/{specId}")
    public String editSwagger(Model model, @PathVariable("specId") Long specId) {

        model.addAttribute("swaggerSpec", swaggerSpecRepository.findById(specId));
        return "management";
    }

    @GetMapping("/delete.html/{specId}")
    public String deleteSwagger(Model model, @PathVariable("specId") Long specId) {
        swaggerSpecRepository.deleteById(specId);
        model.addAttribute("swaggerSpec", getSwaggerSpec());
        return "redirect:/admin/index.html";
    }

    private SwaggerSpec getSwaggerSpec(){

        return SwaggerSpec.builder().priority(100).version("2.0").build();

    }
}
