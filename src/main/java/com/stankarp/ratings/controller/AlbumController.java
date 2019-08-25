package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.message.response.ResponseMessage;
import com.stankarp.ratings.service.AlbumService;
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
import java.util.Optional;

@RestController
@RequestMapping("/albums")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AlbumController {

    private AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    private PagedResources<?> handleNull(Page<Album> albums, PagedResourcesAssembler<Album> assembler) {

        return Optional.ofNullable(albums)
                .map(page -> page.isEmpty() ? assembler.toEmptyResource(page, Resource.class) : assembler.toResource(page))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No albums found"));
    }

    @GetMapping(path = "{albumId:[0-9]+}", produces = {"application/hal+json"})
    public Resource<Album> findById(@PathVariable long albumId) {
        return albumService.findById(albumId)
                .map(album -> new Resource<>(album))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find album."));
    }

    @GetMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public PagedResources<?> all(@PageableDefault Pageable pageable,
                                                PagedResourcesAssembler<Album> assembler) {
        return handleNull(albumService.findAll(pageable), assembler);
    }

    @GetMapping(path={"query"}, produces = {"application/hal+json"})
    public PagedResources<?> query(@RequestParam String query,
                                                 @PageableDefault Pageable pageable,
                                                 PagedResourcesAssembler<Album> assembler) {
        return handleNull(albumService.findByQuery(query, pageable), assembler);
    }

    @GetMapping(path={"performer"}, produces = {"application/hal+json"})
    public PagedResources<?> performer(@RequestParam long performerId,
                                                     @PageableDefault Pageable pageable,
                                                     PagedResourcesAssembler<Album> assembler) {
        return handleNull(albumService.findByPerformerId(performerId, pageable), assembler);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public Resource<Album> save(@RequestBody @Valid AlbumForm albumForm) {
        Optional<Album> optionalAlbum = albumService.save(albumForm);
        return optionalAlbum
                .map(album -> new Resource<>(album))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create album."));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public ResponseEntity<?> delete(@RequestParam Long albumId) {
        return albumService.delete(albumId).map(album ->
                ResponseEntity.ok(new ResponseMessage("Album removed")
                )).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album cannot be removed"));
    }
}
