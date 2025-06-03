import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import { FrameData } from 'src/__types/frameData';

@Schema()
export class Framedata {
  @Prop({ required: true, unique: true })
  character: string;

  @Prop({ type: [Object], required: true })
  moves: FrameData[];
}

export type FramedataDocument = Framedata & Document;
export const FramedataSchema = SchemaFactory.createForClass(Framedata);
