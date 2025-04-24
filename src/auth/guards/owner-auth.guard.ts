import {
  CanActivate,
  ExecutionContext,
  Injectable,
  Logger,
  UnauthorizedException,
} from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class OwnerAuthGuard implements CanActivate {
  private readonly logger = new Logger(OwnerAuthGuard.name);

  constructor(private readonly jwtService: JwtService) {}

  async canActivate(context: ExecutionContext): Promise<boolean> {
    const request = context.switchToHttp().getRequest();
    const authHeader = request?.headers?.authorization;

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      throw new UnauthorizedException(
        'Missing or invalid Authorization header',
      );
    }

    const token = authHeader.split(' ')[1];

    try {
      const payload = await this.jwtService.verifyAsync(token, {
        secret: process.env.JWT_SECRET,
      });

      const ownerEmail = process.env.OWNER_EMAIL;

      if (payload.email === ownerEmail) {
        return true;
      }

      this.logger.warn(`Unauthorized access attempt by: ${payload.email}`);
      throw new UnauthorizedException('Not authorized as owner');
    } catch (err) {
      this.logger.error('JWT verification failed:', err);
      throw new UnauthorizedException('Invalid token');
    }
  }
}
