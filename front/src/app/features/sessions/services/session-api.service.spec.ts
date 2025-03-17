import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let sessionService: SessionApiService;
  let httpTestingController: HttpTestingController;
  const pathService = 'api/session';

  const sessions: Session[] = [
    {
      id: 1,
      name: 'yoga',
      description: 'yoga for beginners',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      name: 'hypnose',
      description: 'hypnose for beginners',
      date: new Date(),
      teacher_id: 2,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    sessionService = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created SessionsService', () => {
    expect(sessionService).toBeTruthy();
  });

  describe('all', () => {
    it('should get all sessions', () => {
      let result: Session[] | undefined;

      sessionService.all().subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}`);
      req.flush(sessions);
      expect(req.request.method).toBe('GET');
      expect(result).toEqual(sessions);
    });
  });

  describe('detail', () => {
    let result: Session | undefined;

    it('should get a session by its id', () => {
      sessionService.detail('1').subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}/1`);
      req.flush(sessions[0]);
      expect(req.request.method).toBe('GET');
      expect(result).toEqual(sessions[0]);
    });
  });

  describe('delete', () => {
    let result: Session | undefined;

    it('should delete a session by its id', () => {
      sessionService.delete('1').subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}/1`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });

  describe('create', () => {
    let result: Session | undefined;

    it('should create a session', () => {
      sessionService.create(sessions[0]).subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}`);
      req.flush(sessions[0]);
      expect(req.request.method).toBe('POST');
      expect(result).toEqual(sessions[0]);
    });
  });

  describe('update', () => {
    const sessionUpdated: Session = {
      id: 1,
      name: 'yoga',
      description: 'yoga',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    let result: Session | undefined;

    it('should update a session', () => {
      sessionService.update('1', sessionUpdated).subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}/1`);
      req.flush(sessionUpdated);
      expect(req.request.method).toBe('PUT');
      expect(result).toEqual(sessionUpdated);
    });
  });

  describe('participate', () => {
    it('should participate in a session', () => {
      sessionService.participate('1', '1').subscribe((response) => {
        response;
      });

      const req = httpTestingController.expectOne(
        `${pathService}/1/participate/1`
      );
      req.flush(null);
      expect(req.request.method).toBe('POST');
    });
  });

  describe('unParticipate', () => {
    it('should unparticipate in a session', () => {
      sessionService.unParticipate('1', '1').subscribe((response) => {
        response;
      });

      const req = httpTestingController.expectOne(
        `${pathService}/1/participate/1`
      );
      req.flush(null);
      expect(req.request.method).toBe('DELETE');
    });
  });
});
