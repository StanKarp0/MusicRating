package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.message.request.RatingForm;
import com.stankarp.ratings.message.response.ResponseMessage;
import com.stankarp.ratings.service.RatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/ratings")
public class RatingController {

    private RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    private PagedResources<?> handleNull(Page<Rating> ratings, PagedResourcesAssembler<Rating> assembler) {
        return Optional.ofNullable(ratings)
                .map(page -> page.isEmpty() ? assembler.toEmptyResource(page, Resource.class) : assembler.toResource(page))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ratings found"));
    }

    @GetMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public PagedResources<?> all(@PageableDefault Pageable pageable, PagedResourcesAssembler<Rating> assembler) {
        return handleNull(ratingService.findAll(pageable), assembler);
    }

    @GetMapping(path={"user"}, produces = {"application/hal+json"})
    public PagedResources<?> user(@RequestParam String user, @PageableDefault Pageable pageable,
                                  PagedResourcesAssembler<Rating> assembler) {
        return handleNull(ratingService.findByUser(user, pageable), assembler);
    }

    @GetMapping(path={"album"}, produces = {"application/hal+json"})
    public PagedResources<?> album(@RequestParam long albumId, @PageableDefault Pageable pageable,
                                   PagedResourcesAssembler<Rating> assembler) {
        return handleNull(ratingService.findByAlbumId(albumId, pageable), assembler);
    }

    @GetMapping(path = "{ratingId:[0-9]+}", produces = {"application/hal+json"})
    public Resource<Rating> findById(@PathVariable long ratingId) {
        return ratingService.findById(ratingId)
                .map(performer -> new Resource<>(performer))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find rating."));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = {"", "/"}, produces = {"application/json"})
    public Resource<Rating> save(@RequestBody @Valid RatingForm ratingForm,
                                 Principal principal) {
        return ratingService.save(ratingForm, principal.getName())
                .map(rating -> new Resource<>(rating))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot save rating"));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public ResponseEntity<?> delete(@RequestParam long ratingId) {
        return ratingService.delete(ratingId).map(rating ->
                ResponseEntity.ok(new ResponseMessage("Rating removed")
                )).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating cannot be removed"));
    }

}
