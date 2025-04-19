import { Injectable, Logger } from '@nestjs/common';
import { UsersRepository } from './users.repository';
import { CreateUserDto } from './dto/create-user.dto';
import { User } from './entities/user.entity';

@Injectable()
export class UsersService {
  private readonly logger = new Logger(UsersService.name);

  constructor(private readonly repo: UsersRepository) {}

  async create(createUserDto: CreateUserDto): Promise<User> {
    const user = new User(createUserDto);
    const createdUser = this.repo.create(user);
    return createdUser;
  }

  async findByEmail(email: string): Promise<User> {
    return await this.repo.findByEmail(email);
  }
}
