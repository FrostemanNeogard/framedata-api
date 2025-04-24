import { Injectable } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
  constructor(private jwtService: JwtService) {}

  async loginWithGoogle(user: any) {
    const payload = { email: user.user.email };
    const token = this.jwtService.sign(payload, {
      secret: process.env.JWT_SECRET,
      expiresIn: '3d',
    });

    return {
      access_token: token,
    };
  }
}
