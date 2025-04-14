import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Injectable } from '@nestjs/common';
import {
  CharacterCodes,
  CharacterCodesDocument,
} from './schemas/character-codes.schema';

@Injectable()
export class CharacterCodesRepository {
  constructor(
    @InjectModel(CharacterCodes.name)
    private readonly characterCodesModel: Model<CharacterCodesDocument>,
  ) {}

  async findByGame(game: string): Promise<CharacterCodesDocument | null> {
    return this.characterCodesModel.findOne({ game }).exec();
  }
}
