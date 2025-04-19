import { Injectable } from '@nestjs/common';
import { CreateUserDto } from 'src/user/dto/create-user.dto';
import { User } from 'src/user/entities/user.entity';
import { UsersService } from 'src/user/users.service';

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
