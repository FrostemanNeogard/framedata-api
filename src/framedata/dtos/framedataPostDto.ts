import { IsNumber, IsOptional, ValidateNested } from 'class-validator';
import { FramedataDto } from './framedataDto';
import { Type } from 'class-transformer';

export class FramedataPostDto {
  @ValidateNested()
  @Type(() => FramedataDto)
  data: FramedataDto;

  @IsNumber()
  @IsOptional()
  index?: number;
}
