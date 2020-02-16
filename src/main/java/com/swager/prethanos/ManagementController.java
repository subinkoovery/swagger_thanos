package com.swager.prethanos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swager.prethanos.entity.SwaggerSpec;
import com.swager.prethanos.model.Dashboard;
import com.swager.prethanos.model.MicroService;
import com.swager.prethanos.model.SwaggerSchema;
import com.swager.prethanos.repository.SwaggerSpecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("admin")
@Controller
public class ManagementController {

    @Autowired
    SwaggerSpecRepository swaggerSpecRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("/index.html")
    public String getManagementData(SwaggerSpec swaggerSpec, Model model) {

        model.addAttribute("swaggerSpecList", swaggerSpecRepository.findAllByOrderByPriorityAscNameAsc());
        model.addAttribute("swaggerSpec", getSwaggerSpec());
        return "management";
    }

    @PostMapping("/add-swagger.html")
    public String addSwaggerSpec(SwaggerSpec swaggerSpec, Model model) {

        swaggerSpecRepository.save(swaggerSpec);
        model.addAttribute("swaggerSpec", getSwaggerSpec());
        return "redirect:/admin/index.html";
    }

    @GetMapping("/edit.html/{specId}")
    public String editSwagger(Model model, @PathVariable("specId") Long specId) {

        model.addAttribute("swaggerSpec", swaggerSpecRepository.findById(specId).get());
        return "management";
    }

    @GetMapping("/delete.html/{specId}")
    public String deleteSwagger(Model model, @PathVariable("specId") Long specId) {
        swaggerSpecRepository.deleteById(specId);
        model.addAttribute("swaggerSpec", getSwaggerSpec());
        return "redirect:/admin/index.html";
    }

    @GetMapping("/dashboard.html")
    public String getDashBoard(Model model) {

        List<MicroService> microServiceList = getMicroServiceDetail();
        model.addAttribute("microServiceList", microServiceList);
        model.addAttribute("dashBoard", getDashboardData(microServiceList));
        return "dashboard";
    }

    /**
     * Get dashboard data based on the micro service detail.
     * @param microServiceList microServiceList
     * @return Dashboard
     */
    private Dashboard getDashboardData(List<MicroService> microServiceList) {

        long microServiceCount = microServiceList.stream()
                .filter(microService -> microService.getNoOfAPIs() != null)
                .map(e -> e.getNoOfAPIs())
                .reduce((e1, e2) -> e1 + e2).orElse(0L);

        return Dashboard.builder()
                .noOfMicroservice(microServiceList.size())
                .totalNoOfAPIs(microServiceCount)
                .build();
    }

    private List<MicroService> getMicroServiceDetail() {
        return swaggerSpecRepository.findAllByOrderByPriorityAscNameAsc().parallelStream()
                .map(e -> getMicroServiceDetail(e))
                .collect(Collectors.toList());
    }


    private SwaggerSpec getSwaggerSpec() {

        return SwaggerSpec.builder().priority(100)
                .isAuthRequired(false)
                .version("2.0").build();

    }

    /**
     * This method returns micro service detail from Swagger spec.
     * @param swaggerSpec SwaggerSpec
     * @return MicroService
     */
    private MicroService getMicroServiceDetail(SwaggerSpec swaggerSpec) {

        SwaggerSchema swaggerSchema;
        MicroService microService = new MicroService();
        microService.setName(swaggerSpec.getName());
        microService.setAssociatedSpecId(swaggerSpec.getId());

        try {
            swaggerSchema = getSwaggerSchema(swaggerSpec);
            long noOfApis = swaggerSchema.getPaths() != null ? swaggerSchema.getPaths().entrySet().stream()
                    .flatMap(e -> e.getValue().entrySet().stream())
                    .count() : 0L;
            microService.setNoOfAPIs(noOfApis);

        } catch (Exception e) {

            microService.setError(e.getLocalizedMessage());
        }
        return microService;
    }

    /**
     * This method create swagger schema from swagger spec.
     * @param swaggerSpec SwaggerSpec
     * @return SwaggerSchema
     * @throws IOException
     */
    private SwaggerSchema getSwaggerSchema(SwaggerSpec swaggerSpec) throws IOException {

        SwaggerSchema swaggerSchema;

        if (!swaggerSpec.getIsAuthRequired()){
            swaggerSchema= objectMapper.readValue(new URL(swaggerSpec.getUrl()), SwaggerSchema.class);
        }else {

            HttpEntity<MultiValueMap<String, String>> request = getTokenHttpEntity(swaggerSpec);
            ResponseEntity<SwaggerSchema> swaggerSchemaResponseEntity =
                    restTemplate.exchange(swaggerSpec.getUrl(), HttpMethod.GET,request,SwaggerSchema.class);
            if (swaggerSchemaResponseEntity.hasBody()){
                swaggerSchema=swaggerSchemaResponseEntity.getBody();
            }else {
                throw new IOException("No Response from '"+swaggerSpec.getUrl()+"'");
            }
        }

        return swaggerSchema;
    }

    private HttpEntity<MultiValueMap<String, String>> getTokenHttpEntity(SwaggerSpec swaggerSpec) {
        String clientCredentials = swaggerSpec.getUserName() + ":" + swaggerSpec.getPassword();
        String base64Encoded = Base64.getEncoder().encodeToString(clientCredentials.getBytes(StandardCharsets.UTF_8));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + base64Encoded);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<MultiValueMap<String, String>>(null, httpHeaders);
    }

}
