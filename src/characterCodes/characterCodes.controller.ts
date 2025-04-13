import {
  Controller,
  Get,
  Logger,
  NotFoundException,
  Param,
} from '@nestjs/common';
import { CharacterCodesService } from './characterCodes.service';
import { GameCode } from 'src/__types/gameCode';
import { CharacterCodeDto } from './dtos/characterCodeDto';
import { GameCodesService } from 'src/gameCodes/gameCodes.service';

@Controller('charactercodes')
export class CharacterCodesController {
  private readonly logger: Logger;

  constructor(
    private characterCodesService: CharacterCodesService,
    private gameCodesService: GameCodesService,
  ) {
    this.logger = new Logger();
  }

  @Get(':gameName/:characterName')
  public async formatCharacterName(
    @Param('gameName') gameName: string,
    @Param('characterName') characterName: string,
  ): Promise<CharacterCodeDto> {
    this.logger.log(
      `Attempting to get characterCode for: ${characterName} in game: ${gameName}`,
    );

    const gameCode: GameCode | null =
      this.gameCodesService.getGameCode(gameName);

    if (gameCode == null) {
      throw new NotFoundException("Couldn't find the given game.");
    }

    const characterCode = await this.characterCodesService.getCharacterCode(
      characterName,
      gameCode,
    );

    if (characterCode == null) {
      throw new NotFoundException(
        "Couldn't find the given character for the given game.",
      );
    }

    return new CharacterCodeDto(characterCode);
  }
}
