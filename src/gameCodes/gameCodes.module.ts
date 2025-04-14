import { Module } from '@nestjs/common';
import { GameCodesService } from './gameCodes.service';
import { GameCodesRepository } from './gameCodes.repository';

@Module({
  providers: [GameCodesService, GameCodesRepository],
  exports: [GameCodesService],
})
export class GameCodesModule {}
