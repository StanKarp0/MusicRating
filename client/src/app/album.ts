export class Album {
    
    albumId?: number;
    title?: string;
    year?: number;
    decade?: number;
    average?: number;
    ratingsCount?: number;
    name?: string;
    performerId?: number;

    _links?: {
        album: {
            href: string;
        };
        decade: Object;
        performer: Object;
        ratings: Object;
        self: {
            href: string;
        };
        year: Object;
    };
    href?: string;
}

export class AlbumList {

    _embedded: {
        albums: Album[];
    };
    _links: Object;
    page: {
        size: number;
        totalElements: number;
        totalPages: number;
        number: number;
    };

} 