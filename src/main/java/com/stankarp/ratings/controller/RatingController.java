package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.service.RatingService;
import com.stankarp.ratings.message.request.RatingForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ratings")
public class RatingController {

    private RatingService ratingService;

    private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path = "save", produces = {"application/hal+json"})
    public Resource<Rating> save(@RequestBody RatingForm ratingForm) {
        logger.info(ratingForm.toString());
        Resource<Rating> res = ratingService.save(ratingForm)
                .map(album -> new Resource<>(album))
                .orElse(new Resource<>(new Rating()));
        logger.info(res.toString());
        return res;
    }

}
