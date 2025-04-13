import { Module } from '@nestjs/common';
import { FramedataController } from './framedata/framedata.controller';
import { FramedataService } from './framedata/framedata.service';
import { FramedataModule } from './framedata/framedata.module';
import { CharacterCodesController } from './characterCodes/characterCodes.controller';
import { CharacterCodesModule } from './characterCodes/characterCodes.module';
import { GameCodesModule } from './gameCodes/gameCodes.module';
import { GameCodesService } from './gameCodes/gameCodes.service';
import { GameCodesController } from './gameCodes/gameCodes.controller';
import { MongooseModule } from '@nestjs/mongoose';

@Module({
  imports: [
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
  providers: [FramedataService, GameCodesService],
})
export class AppModule {}
