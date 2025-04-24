import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { FramedataService } from './framedata.service';
import { Framedata, FramedataSchema } from './schemas/framedata.schema';
import { FramedataRepository } from './framedata.repository';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Framedata.name, schema: FramedataSchema },
    ]),
  ],
  providers: [FramedataService, FramedataRepository],
  exports: [FramedataService],
})
export class FramedataModule {}
