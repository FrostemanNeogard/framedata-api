import { Controller, Get, Req, Res, UseGuards } from '@nestjs/common';
import { AuthService } from './auth.service';
import { GoogleAuthGuard } from './guards/google-auth/google-auth.guard';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  // TODO: Add @Login decorator (make this yourself?)
  @UseGuards(GoogleAuthGuard)
  @Get('google/login')
  googleLogin() {}

  // TODO: Add @Login decorator (make this yourself?)
  @UseGuards(GoogleAuthGuard)
  @Get('google/callback')
  googleCallback(@Req() req, @Res() res) {
    const response = await this.authService.login(req.user.id);
    res.redirect(`http://localhost:5173?token=${response.accessToken}`);
  }
}
