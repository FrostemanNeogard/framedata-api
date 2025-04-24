import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type CharacterCodesDocument = CharacterCodes & Document;

@Schema()
export class CharacterCodes {
  @Prop({ required: true })
  game: string;

  @Prop({ type: Map, of: [String] })
  characters: Map<string, string[]>;
}

export const CharacterCodesSchema =
  SchemaFactory.createForClass(CharacterCodes);
