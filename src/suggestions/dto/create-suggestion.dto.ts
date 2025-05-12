import {
  IsBoolean,
  IsEnum,
  IsObject,
  IsOptional,
  IsString,
  ValidateNested,
} from 'class-validator';
import { Type } from 'class-transformer';
import { GameCode, validGameCodes } from 'src/__types/gameCode';
import { FramedataPostDto } from 'src/framedata/dtos/framedataPostDto';

export class SuggestionTargetDto {
  @IsEnum(validGameCodes)
  game: GameCode;

  @IsString()
  character: string;

  @IsBoolean()
  @IsOptional()
  insertAbove: boolean;

  @IsOptional()
  insertionIndex: number;

  @IsString()
  input: string;
}

export class CreateSuggestionDto {
  @IsEnum(['modify', 'delete', 'create'])
  action: 'modify' | 'delete' | 'create';

  @ValidateNested()
  @Type(() => SuggestionTargetDto)
  target: SuggestionTargetDto;

  @IsOptional()
  @IsObject()
  @ValidateNested()
  @Type(() => FramedataPostDto)
  payload?: Partial<FramedataPostDto>;
}
