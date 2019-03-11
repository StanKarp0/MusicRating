export class Rating {

    date?: string;
    rate?: number;
    description?: string;
    userName?: string;
    name?: string;
    title?: string;
    performerId?: number;
    albumId?: number;

    _links?: {
        self: {
            href: string;
        }
        rating: {
            href: string;
        }
        album: {
            href: string;
        }
        user: {
            href: string;
        }
    }

}

export class RatingList {

    _embedded: {
        ratings: Rating[];
    };
    _links: {
        self: {
            href: string;
        }
        prev?: {
            href: string;
        }
        next?: {
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