// suggestion.schema.ts
import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import { FramedataPostDto } from 'src/framedata/dtos/framedataPostDto';
import { GameCode, validGameCodes } from 'src/__types/gameCode';

@Schema()
export class Suggestion extends Document {
  @Prop({ enum: ['modify', 'delete', 'create'], required: true })
  action: 'modify' | 'delete' | 'create';

  @Prop({
    type: {
      game: { type: String, enum: validGameCodes, required: true },
      character: { type: String, required: true },
      input: { type: String, required: true },
    },
    required: true,
  })
  target: {
    game: GameCode;
    character: string;
    input: string;
  };

  @Prop({ type: Object })
  payload?: Partial<FramedataPostDto>;
}

export const SuggestionSchema = SchemaFactory.createForClass(Suggestion);
