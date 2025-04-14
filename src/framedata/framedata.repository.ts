import { Injectable } from '@nestjs/common';
import { InjectConnection } from '@nestjs/mongoose';
import { FrameData } from 'src/__types/frameData';
import { Connection } from 'mongoose';
import { GameCode } from 'src/__types/gameCode';

@Injectable()
export class FramedataRepository {
  constructor(@InjectConnection() private readonly connection: Connection) {}

  getCollection(game: string) {
    return this.connection.collection(game.toLowerCase());
  }

  async findByCharacter(game: GameCode, character: string) {
    const collection = this.getCollection(game);
    return collection.findOne({ character });
  }

  async saveCharacter(game: string, character: string, moves: FrameData[]) {
    const collection = this.getCollection(game);
    return collection.updateOne(
      { character },
      { $set: { moves } },
      { upsert: true },
    );
  }
}
