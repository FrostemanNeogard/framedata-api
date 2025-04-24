import { IsNotEmpty, Validate } from 'class-validator';
import { IsGameCode } from 'src/__validators/isGameCode.decorator';
import { IsCharacterCodeForGame } from 'src/__validators/isCharacterCode.validator';

export class ValidateCharacterCodeDto {
  @IsNotEmpty()
  @IsGameCode()
  game: string;

  @IsNotEmpty()
  @Validate(IsCharacterCodeForGame)
  character: string;
}
