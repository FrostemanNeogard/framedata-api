import { Controller, Get, UseGuards } from '@nestjs/common';
import { GameCodesService } from './gameCodes.service';
import { JwtAuthGuard } from 'src/auth/guards/jwt-auth.guard';

@Controller('gamecodes')
export class GameCodesController {
  constructor(private readonly gameCodesService: GameCodesService) {}

  @Get()
  @UseGuards(JwtAuthGuard)
  public async getAllGameCodes(): Promise<string[]> {
    return this.gameCodesService.getAllGameCodes();
  }
}
