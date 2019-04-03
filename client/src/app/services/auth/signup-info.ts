export class SignUpInfo {
    username: string;
    role: string[];
    password: string;
 
    constructor(username: string, password: string) {
        this.username = username;
        this.password = password;
        this.role = ['user'];
    }
}