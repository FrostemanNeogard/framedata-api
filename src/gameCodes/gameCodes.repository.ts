import { Injectable } from '@nestjs/common';
import { InjectConnection } from '@nestjs/mongoose';
import { Connection } from 'mongoose';

@Injectable()
export class GameCodesRepository {
  constructor(@InjectConnection() private readonly connection: Connection) {}

  async findAllGameCodes(): Promise<string[]> {
    const collections = await this.connection.db.listCollections().toArray();
    return collections.map((col) => col.name);
  }

  async exists(gameCode: string): Promise<boolean> {
    const collections = await this.findAllGameCodes();
    return collections.includes(gameCode);
  }
}
