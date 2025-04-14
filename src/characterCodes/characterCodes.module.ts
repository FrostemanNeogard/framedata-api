import { MongooseModule } from '@nestjs/mongoose';
import {
  CharacterCodes,
  CharacterCodesSchema,
} from './schemas/character-codes.schema';
import { Module } from '@nestjs/common';
import { CharacterCodesService } from './characterCodes.service';
import { CharacterCodesRepository } from './characterCodes.repository';
import { IsCharacterCodeForGame } from 'src/__validators/isCharacterCode.validator';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: CharacterCodes.name, schema: CharacterCodesSchema },
    ]),
  ],
  providers: [
    CharacterCodesService,
    CharacterCodesRepository,
    IsCharacterCodeForGame,
  ],
  exports: [CharacterCodesService],
})
export class CharacterCodesModule {}
