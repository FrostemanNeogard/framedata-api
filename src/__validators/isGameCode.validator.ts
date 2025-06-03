import { Injectable } from '@nestjs/common';
import { GameCodesService } from 'src/gameCodes/gameCodes.service';
import {
  ValidationArguments,
  ValidatorConstraint,
  ValidatorConstraintInterface,
} from 'class-validator';

@ValidatorConstraint({ async: true })
@Injectable()
export class IsGameCodeConstraint implements ValidatorConstraintInterface {
  constructor(private readonly gameCodesService: GameCodesService) {}

  async validate(value: string): Promise<boolean> {
    return this.gameCodesService.isValidGameCode(value);
  }

  defaultMessage(args: ValidationArguments) {
    return `"${args.value}" is not a valid game code.`;
  }
}
