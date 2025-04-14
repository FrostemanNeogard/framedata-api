import { Injectable, Logger } from '@nestjs/common';
import { GameCode, validGameCodes } from 'src/__types/gameCode';

@Injectable()
export class GameCodesService {
  private readonly logger = new Logger(GameCodesService.name);

  public getAllGameCodes(): GameCode[] {
    return [...validGameCodes];
  }

  public getGameCode(gameName: string): GameCode | null {
    if (![...validGameCodes].includes(gameName as GameCode)) {
      this.logger.log(`Couldn't find gameCode for gameName: ${gameName}`);
      return null;
    }

    return validGameCodes[validGameCodes.indexOf(gameName as GameCode)];
  }
}
