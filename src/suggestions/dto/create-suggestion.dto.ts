import {
  IsBoolean,
  IsEnum,
  IsNumber,
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

  @IsString()
  input: string;

  @IsBoolean()
  @IsOptional()
  insertAbove: boolean;

  @IsOptional()
  @IsNumber()
  insertionIndex: number;
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
  payload?: FramedataPostDto;
}
