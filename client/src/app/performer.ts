export class Performer {
    
    performerId?: number;
    name?: string;
    average?: number;
    ratingsCount?: number;
    albumCount?: number;
    _links?: {
        self: {
            href: string;
        }
        performer: {
            href: string;
        }
        albums: {
            href: string;
        }
    }
    href?: string;
}

export class PerformerList {

    _embedded: {
        performers: Performer[];
    };
    _links: {
        self: {
            href: string;
        }
    }
    page: {
        size: number;
        totalElements: number;
        totalPages: number;
        number: number;
    };

}