package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.service.AlbumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/albums")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AlbumController {

    private AlbumService albumService;

    private RepositoryEntityLinks entityLinks;

    public AlbumController(AlbumService albumService, RepositoryEntityLinks entityLinks) {
        this.albumService = albumService;
        this.entityLinks = entityLinks;
    }

    @GetMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public PagedResources<Resource<Album>> all(@PageableDefault Pageable pageable,
                                                PagedResourcesAssembler<Album> assembler) {
        return addLinks(albumService.findAll(pageable), assembler);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public Resource<Album> save(@RequestBody AlbumForm albumForm) {
        return albumService.save(albumForm)
                .map(this::addLinks)
                .map(album -> new Resource<>(album))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Cannot create album. "));
    }

    private Album addLinks(Album album) {
        album.add(entityLinks.linkToSingleResource(AlbumRepository.class, album.getAlbumId()).withSelfRel());
        album.add(entityLinks.linkToSingleResource(AlbumRepository.class, album.getAlbumId()).withRel("album"));
        album.add(entityLinks.linkFor(AlbumRepository.class).slash(album.getAlbumId()).slash("performer").withRel("performer"));
        album.add(entityLinks.linkFor(AlbumRepository.class).slash(album.getAlbumId()).slash("ratings").withRel("ratings"));
        return album;
    }

    private PagedResources<Resource<Album>> addLinks(Page<Album> page, PagedResourcesAssembler<Album> assembler) {
        return assembler.toResource(page.map(this::addLinks));
    }
}
