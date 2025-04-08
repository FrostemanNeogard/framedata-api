import { PipeTransform, Injectable, BadRequestException } from '@nestjs/common';
import { validGameCodes, GameCode } from 'src/__types/gameCode';

@Injectable()
export class GameCodeValidationPipe implements PipeTransform {
  transform(value: any): GameCode {
    if (!validGameCodes.includes(value)) {
      throw new BadRequestException(`Invalid game code: ${value}`);
    }
    return value as GameCode;
  }
}
