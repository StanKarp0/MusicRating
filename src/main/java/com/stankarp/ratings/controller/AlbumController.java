package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.service.AlbumService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/albums")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AlbumController {

    private AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public PagedResources<Resource<Album>> all(@PageableDefault Pageable pageable,
                                                PagedResourcesAssembler<Album> assembler) {
        return assembler.toResource(albumService.findAll(pageable));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public Resource<Album> save(@RequestBody AlbumForm albumForm) {
        Optional<Album> optionalAlbum = albumService.save(albumForm);
        return optionalAlbum
                .map(album -> new Resource<>(album))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create album."));
    }
}
