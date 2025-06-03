import { Controller, Get } from '@nestjs/common';
import { GameCodesService } from './gameCodes.service';

@Controller('gamecodes')
export class GameCodesController {
  constructor(private readonly gameCodesService: GameCodesService) {}

  @Get()
  public async getAllGameCodes(): Promise<string[]> {
    return this.gameCodesService.getAllGameCodes();
  }
}
