package com.echostream.controller;

import com.echostream.domain.entity.DigitalLicense;
import com.echostream.service.RightsManagementService;
import com.echostream.service.dto.RightsCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rights")
public class RightsManagementController {

    private final RightsManagementService rightsManagementService;

    public RightsManagementController(RightsManagementService rightsManagementService) {
        this.rightsManagementService = rightsManagementService;
    }

    @PostMapping("/tracks/{trackId}/licenses")
    @ResponseStatus(HttpStatus.CREATED)
    public DigitalLicense issueLicense(@PathVariable Long trackId, @RequestBody RightsCommand command) {
        return rightsManagementService.issueLicense(trackId, command);
    }
}
