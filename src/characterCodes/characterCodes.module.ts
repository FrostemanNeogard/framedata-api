import { MongooseModule } from '@nestjs/mongoose';
import {
  CharacterCodes,
  CharacterCodesSchema,
} from './schemas/character-codes.schema';
import { Module } from '@nestjs/common';
import { CharacterCodesService } from './characterCodes.service';
import { CharacterCodesRepository } from './characterCodes.repository';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: CharacterCodes.name, schema: CharacterCodesSchema },
    ]),
  ],
  providers: [CharacterCodesService, CharacterCodesRepository],
  exports: [CharacterCodesService],
})
export class CharacterCodesModule {}
