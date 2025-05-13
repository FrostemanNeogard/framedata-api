import { IsOptional, IsString, IsArray } from 'class-validator';
import { FrameData } from 'src/__types/frameData';
import { TekkenMoveCategory } from 'src/__types/moveCategories';

export class FramedataPatchDto implements Partial<FrameData> {
  @IsOptional()
  @IsString()
  input?: string;

  @IsOptional()
  @IsString()
  hitLevel?: string;

  @IsOptional()
  @IsString()
  damage?: string;

  @IsOptional()
  @IsString()
  startup?: string;

  @IsOptional()
  @IsString()
  block?: string;

  @IsOptional()
  @IsString()
  hit?: string;

  @IsOptional()
  @IsString()
  counter?: string;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  notes?: string[];

  @IsOptional()
  @IsString()
  name?: string;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  alternateInputs?: string[];

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  categories?: TekkenMoveCategory[];
}
