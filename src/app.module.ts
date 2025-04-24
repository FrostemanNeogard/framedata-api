import { Module } from '@nestjs/common';
import { FramedataController } from './framedata/framedata.controller';
import { FramedataService } from './framedata/framedata.service';
import { FramedataModule } from './framedata/framedata.module';
import { CharacterCodesController } from './characterCodes/characterCodes.controller';
import { CharacterCodesService } from './characterCodes/characterCodes.service';
import { CharacterCodesModule } from './characterCodes/characterCodes.module';
import { GameCodesModule } from './gameCodes/gameCodes.module';
import { GameCodesService } from './gameCodes/gameCodes.service';
import { GameCodesController } from './gameCodes/gameCodes.controller';
import { ConfigModule } from '@nestjs/config';
import configuration from './__config/configuration';

@Module({
  imports: [
    FramedataModule,
    CharacterCodesModule,
    GameCodesModule,
    ConfigModule.forRoot({ load: [configuration] }),
  ],
  controllers: [
    FramedataController,
    CharacterCodesController,
    GameCodesController,
  ],
  providers: [FramedataService, CharacterCodesService, GameCodesService],
})
export class AppModule {}
