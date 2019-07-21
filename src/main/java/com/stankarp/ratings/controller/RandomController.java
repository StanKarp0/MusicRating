package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RandomController {

    private AlbumService albumService;

    private RepositoryEntityLinks entityLinks;

    public RandomController(AlbumService albumService, RepositoryEntityLinks entityLinks) {
        this.albumService = albumService;
        this.entityLinks = entityLinks;
    }

    //
    @GetMapping(path={"", "/"}, produces = {"application/hal+json"})
    public Resources<Album> random() {
        return addLinks(albumService.findRandom(YearRangeHelper.allYears()));
    }

    @GetMapping(path = "year/{year:[1-2][0-9]{3}}", produces = {"application/hal+json"})
    public Resources<Album> year(@PathVariable Integer year) {
        return addLinks(albumService.findRandom(YearRangeHelper.fromYear(year)), year / 10);
    }

    @GetMapping(path = "decade/{decade:[1-2][0-9]{2}}", produces = {"application/hal+json"})
    public Resources<Album> decade(@PathVariable Integer decade) {
        return addLinks(albumService.findRandom(YearRangeHelper.fromDecade(decade)), decade);
    }

    @GetMapping(path = "years/{year1:[1-2][0-9]{3}}/{year2:[1-2][0-9]{3}}", produces = {"application/hal+json"})
    public Resources<Album> years(@PathVariable Integer year1, @PathVariable Integer year2) {
        return addLinks(albumService.findRandom(YearRangeHelper.fromRange(year1, year2)));
    }

    private Album addLinks(Album album) {
        album.add(entityLinks.linkToSingleResource(AlbumRepository.class, album.getAlbumId()).withSelfRel());
        album.add(entityLinks.linkToSingleResource(AlbumRepository.class, album.getAlbumId()).withRel("album"));
        album.add(entityLinks.linkFor(AlbumRepository.class).slash(album.getAlbumId()).slash("performer").withRel("performer"));
        album.add(entityLinks.linkFor(AlbumRepository.class).slash(album.getAlbumId()).slash("ratings").withRel("ratings"));
        album.add(linkTo(methodOn(RandomController.class).year(album.getYear())).withRel("year"));
        album.add(linkTo(methodOn(RandomController.class).decade(album.getDecade())).withRel("decade"));
        return album;
    }

    private Resources<Album> addLinks(Collection<Album> albums) {
        Resources<Album> resources = new Resources<>(albums.stream().map(this::addLinks).collect(Collectors.toList()));

        for (Integer decade: albumService.findDecades())
            resources.add(linkTo(methodOn(RandomController.class).decade(decade)).withRel("decade_" + decade));

        return resources;
    }

    private Resources<Album> addLinks(Collection<Album> albums, Integer decade) {

        Resources<Album> resource = addLinks(albums);

        for(Integer year: albumService.findYears(decade))
            resource.add(linkTo(methodOn(RandomController.class).year(year)).withRel("year_" + year));

        return resource;
    }


}
