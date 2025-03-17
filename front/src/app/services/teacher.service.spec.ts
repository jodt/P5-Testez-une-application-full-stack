import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let teacherService: TeacherService;
  let httpTestingController: HttpTestingController;
  const pathService = 'api/teacher';

  const teachers: Teacher[] = [
    {
      id: 1,
      lastName: 'teacherLastName',
      firstName: 'teacherFirstName',
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      lastName: 'otherTeacherLastName',
      firstName: 'otherTeacherFirstName',
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    teacherService = TestBed.inject(TeacherService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created TeacherService', () => {
    expect(teacherService).toBeTruthy();
  });

  describe('all', () => {
    it('should retrieve all teachers', () => {
      let result: Teacher[] | undefined;

      teacherService.all().subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}`);
      req.flush(teachers);
      expect(req.request.method).toEqual('GET');
      expect(result).toEqual(teachers);
    });
  });

  describe('details', () => {
    it('should retrieve a teacher by its id', () => {
      let result: Teacher | undefined;

      teacherService.detail('1').subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}/1`);
      req.flush(teachers[0]);
      expect(req.request.method).toEqual('GET');
      expect(result).toEqual(teachers[0]);
    });
  });
});
