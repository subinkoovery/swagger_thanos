package com.swager.prethanos;

import com.swager.prethanos.entity.SwaggerSpec;
import com.swager.prethanos.repository.SwaggerSpecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("admin")
@Controller
public class ManagementController {

    @Autowired
    SwaggerSpecRepository swaggerSpecRepository;


    @GetMapping("/index.html")
    public String getManagementData(SwaggerSpec swaggerSpec){

        return "management";
    }

    @PostMapping("/add-swagger.html")
    public String addSwaggerSpec(SwaggerSpec swaggerSpec, Model model){

        swaggerSpecRepository.save(swaggerSpec);
        return "redirect:/swagger-ui.html";
    }
}
