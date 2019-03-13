export class User {

    username?: string;

    _links?: {
        roles: Object;
        user: Object;
        ratings: Object;
        self: {
            href: string;
        };
    };

}