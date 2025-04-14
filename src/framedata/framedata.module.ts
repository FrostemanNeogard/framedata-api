import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { FramedataService } from './framedata.service';
import { Framedata, FramedataSchema } from './schemas/framedata.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Framedata.name, schema: FramedataSchema },
    ]),
  ],
  providers: [FramedataService],
  exports: [FramedataService],
})
export class FramedataModule {}
