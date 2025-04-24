import {
  ValidatorConstraint,
  ValidatorConstraintInterface,
  ValidationArguments,
} from 'class-validator';
import { Injectable } from '@nestjs/common';
import { CharacterCodesService } from 'src/characterCodes/characterCodes.service';

@ValidatorConstraint({ name: 'IsCharacterCodeForGame', async: true })
@Injectable()
export class IsCharacterCodeForGame implements ValidatorConstraintInterface {
  constructor(private readonly characterCodesService: CharacterCodesService) {}

  async validate(
    character: string,
    args: ValidationArguments,
  ): Promise<boolean> {
    const game = (args.object as any).game;
    if (!game || !character) return false;

    const code = await this.characterCodesService.getCharacterCode(
      character,
      game,
    );
    return !!code;
  }

  defaultMessage(args: ValidationArguments): string {
    return `Character "${args.value}" is not valid for game "${(args.object as any).game}"`;
  }
}
