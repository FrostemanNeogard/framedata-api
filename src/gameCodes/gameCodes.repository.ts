import { Injectable } from '@nestjs/common';
import { validGameCodes } from 'src/__types/gameCode';

@Injectable()
export class GameCodesRepository {
  public findAllGameCodes(): string[] {
    return [...validGameCodes];
  }
}
