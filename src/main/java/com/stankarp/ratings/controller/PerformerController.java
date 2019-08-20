package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.PerformerUpdateForm;
import com.stankarp.ratings.message.response.ResponseMessage;
import com.stankarp.ratings.service.PerformerService;
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
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/performers")
public class PerformerController {

    private PerformerService performerService;

    public PerformerController(PerformerService performerService) {
        this.performerService = performerService;
    }

    @GetMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public PagedResources<Resource<Performer>> findAll(@PageableDefault Pageable pageable,
                                                       PagedResourcesAssembler<Performer> assembler) {
        return Optional.ofNullable(performerService.findAll(pageable))
                .filter(page -> !page.isEmpty())
                .map(assembler::toResource)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No performers found"));
    }

    @GetMapping(path = "{performerId:[0-9]+}", produces = {"application/hal+json"})
    public Resource<Performer> findById(@PathVariable long performerId) {
        return performerService.findById(performerId)
                .map(performer -> new Resource<>(performer))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find performer."));
    }

    @GetMapping(path={"query"}, produces = {"application/hal+json"})
    public PagedResources<Resource<Performer>> query(@RequestParam String query,
                                                     @PageableDefault Pageable pageable,
                                                     PagedResourcesAssembler<Performer> assembler) {
        return Optional.ofNullable(performerService.findByQuery(query, pageable))
                .filter(page -> !page.isEmpty())
                .map(assembler::toResource)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No performers found"));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "", produces = {"application/json"})
    public Resource<Performer> save(@RequestBody @Valid PerformerForm performerForm) {
        return new Resource<>(performerService.save(performerForm));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(path = "", produces = {"application/json"})
    public Resource<Performer> update(@RequestBody @Valid PerformerUpdateForm performerForm) {
        return performerService.update(performerForm).map(performer -> new Resource<>(performer)
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update performer"));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public ResponseEntity<?> delete(@RequestParam Long performerId) {
        return performerService.delete(performerId).map(performer ->
                ResponseEntity.ok(new ResponseMessage("Performer removed")
                )).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Performer cannot be removed"));
    }

}
