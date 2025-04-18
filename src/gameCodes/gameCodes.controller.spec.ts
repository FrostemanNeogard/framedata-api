import { Test, TestingModule } from '@nestjs/testing';
import { GameCodesController } from './gameCodes.controller';
import { GameCodesService } from './gameCodes.service';
import { GameCode } from '../__types/gameCode';

describe('GameCodesController', () => {
  let controller: GameCodesController;
  let service: GameCodesService;

  const mockGameCodes = [
    'tekken6',
    'tekkentag2',
    'tekken7',
    'tekken8',
  ] as GameCode[];

  const mockService = {
    getAllGameCodes: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [GameCodesController],
      providers: [
        {
          provide: GameCodesService,
          useValue: mockService,
        },
      ],
    }).compile();

    controller = module.get<GameCodesController>(GameCodesController);
    service = module.get<GameCodesService>(GameCodesService);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('getAllGameCodes', () => {
    it('should return all game codes', async () => {
      mockService.getAllGameCodes.mockReturnValue(mockGameCodes);

      const result = await controller.getAllGameCodes();

      expect(result).toEqual(mockGameCodes);
      expect(service.getAllGameCodes).toHaveBeenCalled();
    });
  });
});
