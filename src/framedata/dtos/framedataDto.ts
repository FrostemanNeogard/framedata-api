import { IsString, IsArray } from 'class-validator';
import { FrameData } from 'src/__types/frameData';
import { TekkenMoveCategory } from 'src/__types/moveCategories';

export class FramedataDto implements FrameData {
  @IsString()
  input: string;

  @IsString()
  hitLevel: string;

  @IsString()
  damage: string;

  @IsString()
  startup: string;

  @IsString()
  block: string;

  @IsString()
  hit: string;

  @IsString()
  counter: string;

  @IsArray()
  @IsString({ each: true })
  notes: string[];

  @IsString()
  name: string;

  @IsArray()
  @IsString({ each: true })
  alternateInputs: string[];

  @IsArray()
  @IsString({ each: true })
  categories: TekkenMoveCategory[];
}
