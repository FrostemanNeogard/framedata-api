import { Injectable, Logger } from '@nestjs/common';
import { GameCodesRepository } from './gameCodes.repository';

@Injectable()
export class GameCodesService {
  private readonly logger = new Logger(GameCodesService.name);

  constructor(private readonly gameCodesRepo: GameCodesRepository) {}

  public async getAllGameCodes(): Promise<string[]> {
    const codes = await this.gameCodesRepo.findAllGameCodes();
    this.logger.log(`Found gameCodes: ${codes.join(', ')}`);
    return codes;
  }

  public async getGameCode(gameName: string): Promise<string | null> {
    const exists = await this.gameCodesRepo.exists(gameName);

    if (!exists) {
      this.logger.warn(`Couldn't find gameCode for gameName: ${gameName}`);
      return null;
    }

    return gameName;
  }
}
