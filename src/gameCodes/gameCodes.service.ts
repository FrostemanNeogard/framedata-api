import { BadRequestException, Injectable, Logger } from '@nestjs/common';
import { GameCodesRepository } from './gameCodes.repository';
import { GameCode } from 'src/__types/gameCode';

@Injectable()
export class GameCodesService {
  private readonly logger = new Logger(GameCodesService.name);

  constructor(private readonly repo: GameCodesRepository) {}

  public getAllGameCodes(): GameCode[] {
    return this.repo.findAllGameCodes() as GameCode[];
  }

  public getGameCode(gameName: string): GameCode | null {
    const codes = this.repo.findAllGameCodes();

    if (!codes.includes(gameName)) {
      this.logger.warn(`Invalid gameCode: ${gameName}`);
      throw new BadRequestException(`Invalid gameCode: ${gameName}`);
    }

    return gameName as GameCode;
  }

  public isValidGameCode(gameCode: string): boolean {
    return this.repo.findAllGameCodes().includes(gameCode);
  }
}
