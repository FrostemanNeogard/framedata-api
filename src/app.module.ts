import { Module } from '@nestjs/common';
import { FramedataController } from './framedata/framedata.controller';
import { FramedataModule } from './framedata/framedata.module';
import { CharacterCodesController } from './characterCodes/characterCodes.controller';
import { CharacterCodesModule } from './characterCodes/characterCodes.module';
import { GameCodesModule } from './gameCodes/gameCodes.module';
import { GameCodesController } from './gameCodes/gameCodes.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigModule } from '@nestjs/config';
import { SuggestionsModule } from './suggestions/suggestions.module';
import { SuggestionsController } from './suggestions/suggestions.controller';
import { AuthModule } from './auth/auth.module';
import { JwtService } from '@nestjs/jwt';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
    }),
    FramedataModule,
    CharacterCodesModule,
    GameCodesModule,
    MongooseModule.forRoot(process.env.MONGODB_URI),
    SuggestionsModule,
    AuthModule,
  ],
  controllers: [
    FramedataController,
    CharacterCodesController,
    GameCodesController,
    SuggestionsController,
  ],
  providers: [JwtService],
})
export class AppModule {}
