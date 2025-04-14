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

    return new CharacterCodeDto(characterCode);
  }
}
