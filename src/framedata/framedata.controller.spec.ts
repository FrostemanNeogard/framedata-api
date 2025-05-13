import { Test, TestingModule } from '@nestjs/testing';
import { FramedataController } from './framedata.controller';
import { FramedataService } from './framedata.service';
import { CharacterCodesService } from '../characterCodes/characterCodes.service';
import { GameCode } from '../__types/gameCode';
import { FrameData } from '../__types/frameData';
import { NotFoundException, BadRequestException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';

describe('FramedataController', () => {
  let controller: FramedataController;
  let framedataService: FramedataService;
  let characterCodesService: CharacterCodesService;

  const mockFrameData: FrameData[] = [
    {
      input: '1,2',
      hit_level: 'h,h',
      damage: '10,12',
      startup: '10',
      block: '-1',
      hit: '+8',
      counter: 'KND',
      notes: ['Natural combo'],
      name: 'Jab, Straight',
      alternateInputs: ['1,2', 'jab, straight'],
      categories: ['punisher'],
    },
    {
      input: '2,1,1',
      hit_level: 'h,h',
      damage: '10,12',
      startup: '10',
      block: '-1',
      hit: '+8',
      counter: 'KND',
      notes: ['Natural combo'],
      name: 'Jab, Straight',
      alternateInputs: ['1,2', 'jab, straight'],
      categories: ['punisher'],
    },
    {
      input: '1,1,2',
      hit_level: 'h,h,m',
      damage: '10,12,20',
      startup: '10',
      block: '-17',
      hit: 'KND',
      counter: 'KND',
      notes: ['Natural combo'],
      name: 'Flash punch combo',
      alternateInputs: ['1,1,2', 'flashpunch', 'definitely nonexistent'],
      categories: ['punisher'],
    },
    {
      input: 'existent',
      hit_level: 'm',
      damage: '20',
      startup: '9',
      block: '-17',
      hit: 'KND',
      counter: 'KND',
      notes: ['Natural combo'],
      name: 'Flash punch combo',
      alternateInputs: ['existent'],
      categories: ['punisher'],
    },
  ];

  const mockFramedataService = {
    getCharacterFrameData: jest.fn(),
    getSingleMoveFrameDataOrSimilarMoves: jest.fn(),
    saveCharacterFrameData: jest.fn(),
    deleteCharacterFramedata: jest.fn(),
    addCharacterFramedata: jest.fn(),
  };

  const mockCharacterCodesService = {
    getCharacterCode: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [FramedataController],
      providers: [
        JwtService,
        {
          provide: FramedataService,
          useValue: mockFramedataService,
        },
        {
          provide: CharacterCodesService,
          useValue: mockCharacterCodesService,
        },
      ],
    }).compile();

    controller = module.get<FramedataController>(FramedataController);
    framedataService = module.get<FramedataService>(FramedataService);
    characterCodesService = module.get<CharacterCodesService>(
      CharacterCodesService,
    );
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('getFramedataForCharacter', () => {
    it('should return frame data for a character', async () => {
      const gameCode: GameCode = 'tekken8';
      const characterName = 'kaz';
      const characterCode = 'kaz';
      mockCharacterCodesService.getCharacterCode.mockResolvedValue(
        characterCode,
      );
      mockFramedataService.getCharacterFrameData.mockResolvedValue(
        mockFrameData,
      );

      const result = await controller.getFramedataForCharacter(
        gameCode,
        characterName,
      );

      expect(result).toEqual(mockFrameData);
      expect(characterCodesService.getCharacterCode).toHaveBeenCalledWith(
        characterName,
        gameCode,
      );
      expect(framedataService.getCharacterFrameData).toHaveBeenCalledWith(
        characterCode,
        gameCode,
      );
    });
  });

  describe('getFrameDataSingle', () => {
    it('should return frame data for a specific move', async () => {
      const gameCode: GameCode = 'tekken8';
      const characterName = 'kaz';
      const characterCode = 'kaz';
      const input = '1,2';
      mockCharacterCodesService.getCharacterCode.mockResolvedValue(
        characterCode,
      );
      mockFramedataService.getSingleMoveFrameDataOrSimilarMoves.mockResolvedValue(
        [mockFrameData[0]],
      );

      const result = await controller.getFrameDataSingle(
        gameCode,
        characterName,
        input,
      );

      expect(result).toEqual([mockFrameData[0]]);
      expect(characterCodesService.getCharacterCode).toHaveBeenCalledWith(
        characterName,
        gameCode,
      );
      expect(
        framedataService.getSingleMoveFrameDataOrSimilarMoves,
      ).toHaveBeenCalledWith(characterCode, gameCode, input);
    });

    it('should throw NotFoundException when move is not found', async () => {
      const gameCode: GameCode = 'tekken8';
      const characterName = 'kaz';
      const characterCode = 'kaz';
      const input = '2,1';
      mockCharacterCodesService.getCharacterCode.mockResolvedValue(
        characterCode,
      );
      mockFramedataService.getSingleMoveFrameDataOrSimilarMoves.mockResolvedValue(
        [{}, {}],
      );

      await expect(
        controller.getFrameDataSingle(gameCode, characterName, input),
      ).rejects.toThrow(NotFoundException);
    });
  });

  describe('updateMoveData', () => {
    it('should update move data successfully', async () => {
      const gameCode: GameCode = 'tekken8';
      const characterCode = 'kaz';
      const input = '1,2';
      const updates = { damage: '15,15' };
      mockFramedataService.getCharacterFrameData.mockResolvedValue(
        mockFrameData,
      );
      mockFramedataService.saveCharacterFrameData.mockResolvedValue(undefined);

      await expect(
        controller.updateMoveData(gameCode, characterCode, input, updates),
      ).resolves.not.toThrow();
    });

    it('should throw NotFoundException when move is not found', async () => {
      const gameCode: GameCode = 'tekken8';
      const characterCode = 'kaz';
      const input = 'nonexistent';
      const updates = { damage: '15,15' };
      mockFramedataService.getCharacterFrameData.mockResolvedValue(
        mockFrameData,
      );

      await expect(
        controller.updateMoveData(gameCode, characterCode, input, updates),
      ).rejects.toThrow(NotFoundException);
    });
  });

  describe('deleteMoveData', () => {
    it('should delete move data successfully', async () => {
      const gameCode: GameCode = 'tekken8';
      const characterCode = 'kaz';
      const input = '1,2';
      mockFramedataService.deleteCharacterFramedata.mockResolvedValue(
        undefined,
      );

      await expect(
        controller.deleteMoveData(gameCode, characterCode, input),
      ).resolves.not.toThrow();
    });

    it('should throw BadRequestException when deletion fails', async () => {
      const gameCode: GameCode = 'tekken8';
      const characterCode = 'kaz';
      const input = '1,2';
      mockFramedataService.deleteCharacterFramedata.mockRejectedValue(
        new BadRequestException(),
      );

      await expect(
        controller.deleteMoveData(gameCode, characterCode, input),
      ).rejects.toThrow(BadRequestException);
    });
  });
});
