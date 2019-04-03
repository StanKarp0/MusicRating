package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/albums")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AlbumController {

    private AlbumService albumService;

    private RepositoryEntityLinks entityLinks;

    static private int PageLimit = 20;

    private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);

    public AlbumController(AlbumService albumService, RepositoryEntityLinks entityLinks) {
        this.albumService = albumService;
        this.entityLinks = entityLinks;
    }

    @GetMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public PagedResources<Resource<Album>> all(@PageableDefault Pageable pageable,
                                                PagedResourcesAssembler<Album> assembler) {
        return addLinks(albumService.findAll(pageable, YearRangeHelper.allYears()), assembler);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path = {"", "/"}, produces = {"application/hal+json"})
    public Resource<Album> save(@RequestBody AlbumForm albumForm, PagedResourcesAssembler<Album> assembler) {
        logger.info(albumForm.toString());
        Resource<Album> res = albumService.save(albumForm)
                .map(album -> addLinks(album, assembler))
                .map(album -> new Resource<>(album))
                .orElse(new Resource<>(new Album()));
        logger.info(res.toString());
        return res;
    }

    @GetMapping(path = "year/{year:[1-2][0-9]{3}}", produces = {"application/hal+json"})
    public PagedResources<Resource<Album>> year(@PathVariable Integer year, @PageableDefault Pageable pageable,
                                                PagedResourcesAssembler<Album> assembler) {
        return addLinks(albumService.findAll(pageable, YearRangeHelper.fromYear(year)), assembler, year / 10);
    }

    @GetMapping(path = "decade/{decade:[1-2][0-9]{2}}", produces = {"application/hal+json"})
    public PagedResources<Resource<Album>> decade(@PathVariable Integer decade, @PageableDefault Pageable pageable,
                                                  PagedResourcesAssembler<Album> assembler) {
        return addLinks(albumService.findAll(pageable, YearRangeHelper.fromDecade(decade)), assembler, decade);
    }

    @GetMapping(path = "years/{year1:[1-2][0-9]{3}}/{year2:[1-2][0-9]{3}}", produces = {"application/hal+json"})
    public PagedResources<Resource<Album>> years(@PathVariable Integer year1, @PathVariable Integer year2,
                                                 @PageableDefault Pageable pageable,
                                                 PagedResourcesAssembler<Album> assembler) {
        return addLinks(albumService.findAll(pageable, YearRangeHelper.fromRange(year1, year2)), assembler);
    }

    private Album addLinks(Album album, PagedResourcesAssembler<Album> assembler) {
        Pageable pageable = PageRequest.of(0, AlbumController.PageLimit).first();
        album.add(entityLinks.linkToSingleResource(AlbumRepository.class, album.getAlbumId()).withSelfRel());
        album.add(entityLinks.linkToSingleResource(AlbumRepository.class, album.getAlbumId()).withRel("album"));
        album.add(entityLinks.linkFor(AlbumRepository.class).slash(album.getAlbumId()).slash("performer").withRel("performer"));
        album.add(entityLinks.linkFor(AlbumRepository.class).slash(album.getAlbumId()).slash("ratings").withRel("ratings"));
        album.add(linkTo(methodOn(AlbumController.class).year(album.getYear(), pageable, assembler)).withRel("year"));
        album.add(linkTo(methodOn(AlbumController.class).decade(album.getDecade(), pageable, assembler)).withRel("decade"));
        return album;
    }

    private PagedResources<Resource<Album>> addLinks(Page<Album> page,
                                                     PagedResourcesAssembler<Album> assembler) {

        PagedResources<Resource<Album>> resources = assembler.toResource(page.map(album -> addLinks(album, assembler)));

        Pageable pageable = PageRequest.of(0, AlbumController.PageLimit).first();
        for (Integer decade: albumService.findDecades())
            resources.add(linkTo(methodOn(AlbumController.class).decade(decade, pageable, assembler)).withRel("decade_" + decade));

        return resources;
    }

    private PagedResources<Resource<Album>> addLinks(Page<Album> page,
                                                     PagedResourcesAssembler<Album> assembler,
                                                     Integer decade) {
        PagedResources<Resource<Album>> resource = addLinks(page, assembler);
        Pageable pageable = PageRequest.of(0, AlbumController.PageLimit).first();

        for(Integer year: albumService.findYears(decade))
            resource.add(linkTo(methodOn(AlbumController.class).year(year, pageable, assembler)).withRel("year_" + year));

        return resource;
    }
}
