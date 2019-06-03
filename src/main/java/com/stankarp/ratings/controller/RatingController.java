package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.message.request.RatingForm;
import com.stankarp.ratings.service.RatingService;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ratings")
public class RatingController {

    private RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "/save", produces = {"application/json"})
    public Resource<Rating> save(@RequestBody RatingForm ratingForm) {
        return ratingService.save(ratingForm)
                .map(rating -> new Resource<>(rating))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot save rating"));
    }

}
