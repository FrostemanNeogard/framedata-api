import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import {
  CharacterCodes,
  CharacterCodesDocument,
} from './schemas/character-codes.schema';
import { Injectable } from '@nestjs/common';

@Injectable()
export class CharacterCodesService {
  constructor(
    @InjectModel(CharacterCodes.name)
    private readonly characterCodesModel: Model<CharacterCodesDocument>,
  ) {}

  async getCharacterCode(alias: string, game: string): Promise<string | null> {
    const record = await this.characterCodesModel.findOne({ game }).exec();

    if (!record) return null;

    for (const [code, aliases] of record.characters.entries()) {
      if (aliases.map((a) => a.toLowerCase()).includes(alias.toLowerCase())) {
        return code;
      }
    }

    return null;
  }
}
