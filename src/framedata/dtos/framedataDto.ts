import { IsString, IsArray, IsOptional } from 'class-validator';
import { FrameData } from 'src/__types/frameData';
import { TekkenMoveCategory } from 'src/__types/moveCategories';

export class FramedataDto implements FrameData {
  @IsString()
  @IsOptional()
  input?: string;

  @IsString()
  @IsOptional()
  hitLevel?: string;

  @IsString()
  @IsOptional()
  damage?: string;

  @IsString()
  @IsOptional()
  startup?: string;

  @IsString()
  @IsOptional()
  block?: string;

  @IsString()
  @IsOptional()
  hit?: string;

  @IsString()
  @IsOptional()
  counter?: string;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  notes?: string[];

  @IsString()
  @IsOptional()
  name?: string;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  alternateInputs?: string[];

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  categories?: TekkenMoveCategory[];
}
