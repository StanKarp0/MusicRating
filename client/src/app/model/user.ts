export class User {

    username?: string;
    ratings?: number;
    average?: number;

    _links?: {
        roles: Object;
        user: Object;
        ratings: Object;
        self: {
            href: string;
        };
    };

}

export class UserList {

    _embedded: {
        users: User[];
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