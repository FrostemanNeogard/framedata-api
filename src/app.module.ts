import { Module } from '@nestjs/common';
import { FramedataController } from './framedata/framedata.controller';
import { FramedataModule } from './framedata/framedata.module';
import { CharacterCodesController } from './characterCodes/characterCodes.controller';
import { CharacterCodesModule } from './characterCodes/characterCodes.module';
import { GameCodesModule } from './gameCodes/gameCodes.module';
import { GameCodesController } from './gameCodes/gameCodes.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigModule } from '@nestjs/config';

@Module({
  imports: [
    ConfigModule.forRoot(),
    FramedataModule,
    CharacterCodesModule,
    GameCodesModule,
    MongooseModule.forRoot(process.env.MONGODB_URI),
  ],
  controllers: [
    FramedataController,
    CharacterCodesController,
    GameCodesController,
  ],
  providers: [],
})
export class AppModule {}
