import { registerDecorator, ValidationOptions } from 'class-validator';
import { IsGameCodeConstraint } from './isGameCode.validator';

export function IsGameCode(validationOptions?: ValidationOptions) {
  return function (object: unknown, propertyName: string) {
    registerDecorator({
      target: object.constructor,
      propertyName,
      options: validationOptions,
      constraints: [],
      validator: IsGameCodeConstraint,
    });
  };
}
