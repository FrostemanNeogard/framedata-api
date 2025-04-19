import { Injectable } from '@nestjs/common';
import { CreateUserDto } from './user/dto/create-user.dto';
import { UsersService } from './user/users.service';
import { User } from './user/entities/user.entity';

@Injectable()
export class AuthService {
  constructor(private readonly usersService: UsersService) {}

  async validateGoogleUser(googleUser: CreateUserDto): Promise<User> {
    const user = await this.usersService.findByEmail(googleUser.email);

    if (user) {
      return user;
    }

    return await this.usersService.create(googleUser);
  }

  async validateUser(email: string, password: string) {}

  async login(userId: number) {
    const { accessToken, refreshToken } = await this.generateTokens(userId);
    const hashedRefreshToken = await argon2;
  }
}
