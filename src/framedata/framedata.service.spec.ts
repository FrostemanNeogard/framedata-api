import { Test, TestingModule } from '@nestjs/testing';
import { FramedataService } from './framedata.service';
import { FramedataRepository } from './framedata.repository';
import { GameCode } from '../__types/gameCode';
import { FrameData } from '../__types/frameData';
import {
  BadRequestException,
  NotFoundException,
  ConflictException,
} from '@nestjs/common';

describe('FramedataService', () => {
  let service: FramedataService;
  let repository: FramedataRepository;

  const mockFrameData: FrameData[] = [
    {
      name: 'One Two Punch',
      input: '1,2',
      hitLevel: 'h,h',
      damage: '5,8',
      startup: ',i10',
      block: '-1',
      hit: '+8',
      counter: '',
      alternateInputs: ['1,2'],
      categories: [],
      notes: [
        'Jails',
        'Combo from 1st hit',
        'Combo can be delayed 12F from 1st hit',
      ],
    },
    {
      input: 'df+2',
      hitLevel: 'm',
      damage: '20',
      startup: '15',
      block: '-12',
      hit: 'KND',
      counter: 'KND',
      notes: ['Launcher'],
      name: 'Electric Wind God Fist',
      alternateInputs: ['df+2', 'ewgf'],
      categories: [],
    },
    {
      input: 'df+3',
      hitLevel: 'm',
      damage: '20',
      startup: '15',
      block: '-12',
      hit: 'KND',
      counter: 'KND',
      notes: [],
      name: 'Knee',
      alternateInputs: ['df+3'],
      categories: [],
    },
  ];

  const mockRepository = {
    findByCharacter: jest.fn(),
    saveCharacter: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        FramedataService,
        {
          provide: FramedataRepository,
          useValue: mockRepository,
        },
      ],
    }).compile();

    service = module.get<FramedataService>(FramedataService);
    repository = module.get<FramedataRepository>(FramedataRepository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('getCharacterFrameData', () => {
    it('should return frame data for a character', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      mockRepository.findByCharacter.mockResolvedValue({
        moves: mockFrameData,
      });

      const result = await service.getCharacterFrameData(characterCode, game);

      expect(result).toEqual(mockFrameData);
      expect(repository.findByCharacter).toHaveBeenCalledWith(
        game,
        characterCode,
      );
    });

    it('should throw BadRequestException when no frame data is found', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      mockRepository.findByCharacter.mockResolvedValue(null);

      await expect(
        service.getCharacterFrameData(characterCode, game),
      ).rejects.toThrow(BadRequestException);
    });
  });

  describe('getSingleMoveFrameDataOrSimilarMoves', () => {
    it('should return exact match for a move', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      const notation = '1,2';
      mockRepository.findByCharacter.mockResolvedValue({
        moves: mockFrameData,
      });

      let result;
      try {
        result = await service.getSingleMoveFrameDataOrSimilarMoves(
          characterCode,
          game,
          notation,
        );
      } catch {}

      expect(result).toEqual([mockFrameData[0]]);
    });

    it('should return similar moves when exact match is not found', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      const notation = '1,2,3';
      mockRepository.findByCharacter.mockResolvedValue({
        moves: mockFrameData,
      });

      let result;
      try {
        result = await service.getSingleMoveFrameDataOrSimilarMoves(
          characterCode,
          game,
          notation,
        );
      } catch {}

      expect(result.length).toBeGreaterThan(0);
      expect(result[0].input).toBeDefined();
    });
  });

  describe('saveCharacterFrameData', () => {
    it('should save frame data successfully', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      mockRepository.saveCharacter.mockResolvedValue(undefined);

      await expect(
        service.saveCharacterFrameData(characterCode, game, mockFrameData),
      ).resolves.not.toThrow();
      expect(repository.saveCharacter).toHaveBeenCalledWith(
        game,
        characterCode,
        mockFrameData,
      );
    });

    it('should throw BadRequestException when save fails', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      mockRepository.saveCharacter.mockRejectedValue(new Error('Save failed'));

      await expect(
        service.saveCharacterFrameData(characterCode, game, mockFrameData),
      ).rejects.toThrow(BadRequestException);
    });
  });

  describe('deleteCharacterFramedata', () => {
    it('should delete a move successfully', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      const input = '1,2';
      mockRepository.findByCharacter.mockResolvedValue({
        moves: mockFrameData,
      });
      mockRepository.saveCharacter.mockResolvedValue(undefined);

      await expect(
        service.deleteCharacterFramedata(characterCode, game, input),
      ).resolves.not.toThrow();
    });

    it('should throw NotFoundException when move is not found', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      const input = 'non_existent_move';
      mockRepository.findByCharacter.mockResolvedValue({
        moves: mockFrameData,
      });

      await expect(
        service.deleteCharacterFramedata(characterCode, game, input),
      ).rejects.toThrow(NotFoundException);
    });
  });

  describe('addCharacterFramedata', () => {
    it('should add new move data successfully', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      const newMove: FrameData = {
        input: 'uf+4,4',
        hitLevel: 'm, l',
        damage: '',
        startup: '18',
        block: '',
        hit: '',
        counter: 'LNC',
        notes: ['Launcher'],
        name: 'Rising Sun',
        alternateInputs: ['uf+4,4'],
        categories: [],
      };
      mockRepository.findByCharacter.mockResolvedValue({
        moves: mockFrameData,
      });
      mockRepository.saveCharacter.mockResolvedValue(undefined);

      await expect(
        service.addCharacterFramedata(characterCode, game, newMove),
      ).resolves.not.toThrow();
    });

    it('should throw ConflictException when move already exists', async () => {
      const characterCode = 'kaz';
      const game: GameCode = 'tekken8';
      mockRepository.findByCharacter.mockResolvedValue({
        moves: mockFrameData,
      });

      await expect(
        service.addCharacterFramedata(characterCode, game, mockFrameData[0]),
      ).rejects.toThrow(ConflictException);
    });
  });
});
