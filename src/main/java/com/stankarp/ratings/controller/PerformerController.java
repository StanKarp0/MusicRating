package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.RatingForm;
import com.stankarp.ratings.service.PerformerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/performers")
public class PerformerController {

    private PerformerService performerService;

    private static final Logger logger = LoggerFactory.getLogger(PerformerController.class);

    public PerformerController(PerformerService performerService) {
        this.performerService = performerService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path = "save", produces = {"application/json"})
    public Resource<Performer> save(@RequestBody PerformerForm performerForm) {
        logger.info(performerForm.toString());
        return new Resource<>(performerService.save(performerForm));
        // TODO throw error, or throw
    }

}
