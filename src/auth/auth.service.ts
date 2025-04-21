import { Injectable, InternalServerErrorException } from '@nestjs/common';

@Injectable()
export class AuthService {
  googleLogin(req: any) {
    if (!req?.user) {
      throw new InternalServerErrorException(
        'No user object provided from Google.',
      );
    }

    return {
      user: req?.user,
    };
  }
}
