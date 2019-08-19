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

    private PagedResources<Resource<Rating>> handleNull(Page<Rating> ratings, String errorMsg,
                                                        PagedResourcesAssembler<Rating> assembler) {
        return Optional.ofNullable(ratings)
                .filter(page -> !page.isEmpty())
                .map(assembler::toResource)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg));
    }

    @GetMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public PagedResources<Resource<Rating>> all(@PageableDefault Pageable pageable,
                                                PagedResourcesAssembler<Rating> assembler) {
        return handleNull(ratingService.findAll(pageable), "No ratings at all", assembler);
    }

    @GetMapping(path={"user"}, produces = {"application/hal+json"})
    public PagedResources<Resource<Rating>> user(@RequestParam String user,
                                                 @PageableDefault Pageable pageable,
                                                 PagedResourcesAssembler<Rating> assembler) {
        return handleNull(ratingService.findByUser(user, pageable), "No ratings found for username.", assembler);
    }

    @GetMapping(path={"album"}, produces = {"application/hal+json"})
    public PagedResources<Resource<Rating>> album(@RequestParam long albumId,
                                                  @PageableDefault Pageable pageable,
                                                  PagedResourcesAssembler<Rating> assembler) {
        return handleNull(ratingService.findByAlbumId(albumId, pageable), "No ratings for album", assembler);
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
//        principal.getName()
        return ratingService.save(ratingForm)
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
