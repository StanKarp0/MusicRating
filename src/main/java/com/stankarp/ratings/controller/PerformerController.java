package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.PerformerUpdateForm;
import com.stankarp.ratings.service.PerformerService;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/performers/save")
public class PerformerController {

    private PerformerService performerService;

    public PerformerController(PerformerService performerService) {
        this.performerService = performerService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "", produces = {"application/json"})
    public Resource<Performer> save(@RequestBody PerformerForm performerForm) {
        return new Resource<>(performerService.save(performerForm));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(path = "", produces = {"application/json"})
    public Resource<Performer> update(@RequestBody PerformerUpdateForm performerForm) {
        return new Resource<>(performerService.update(performerForm));
    }

}
