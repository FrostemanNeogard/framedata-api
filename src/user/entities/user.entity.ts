import { CreateUserDto } from '../dto/create-user.dto';

export class User {
  username: string;
  email: string;

  constructor(dto: CreateUserDto) {
    this.username = dto.username;
    this.email = dto.email;
  }
}
