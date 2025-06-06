import {
  Controller,
  Get,
  Logger,
  Param,
  UsePipes,
  ValidationPipe,
} from '@nestjs/common';
import { CharacterCodesService } from './characterCodes.service';
import { CharacterCodeDto } from './dtos/characterCodeDto';
import { GameCodesService } from 'src/gameCodes/gameCodes.service';
import { GameCode } from 'src/__types/gameCode';
import { ValidateCharacterCodeDto } from './dtos/validateCharacterCodeDto';

@Controller('charactercodes')
export class CharacterCodesController {
  private readonly logger = new Logger(CharacterCodesController.name);

  constructor(
    private characterCodesService: CharacterCodesService,
    private gameCodesService: GameCodesService,
  ) {}

  @Get(':gameName')
  @UsePipes(new ValidationPipe({ transform: true }))
  public async getAllCharacterCodesForGame(
    @Param('gameName') gameName: string,
  ): Promise<string[]> {
    const gameCode: GameCode | null =
      this.gameCodesService.getGameCode(gameName);

    const characters =
      await this.characterCodesService.getAllCharacterCodesForGame(gameCode);

    return characters;
  }

  @Get(':gameName/:characterName')
  @UsePipes(new ValidationPipe({ transform: true }))
  public async formatCharacterName(
    @Param('gameName') gameName: string,
    @Param('characterName') characterName: string,
  ): Promise<CharacterCodeDto> {
    const dto = new ValidateCharacterCodeDto();
    dto.game = gameName;
    dto.character = characterName;

    const gameCode: GameCode | null = this.gameCodesService.getGameCode(
      dto.game,
    );

    const characterCode = await this.characterCodesService.getCharacterCode(
      dto.character,
      gameCode,
    );

    this.logger.log(
      `Found characterCode for "${characterName}" in "${gameName}": ${characterCode}`,
    );

    return new CharacterCodeDto(characterCode);
  }
}
