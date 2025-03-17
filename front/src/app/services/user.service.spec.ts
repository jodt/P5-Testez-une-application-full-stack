import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let userService: UserService;
  let httpTestingController: HttpTestingController;
  const pathService = 'api/user';

  const user: User = {
    id: 1,
    email: 'user@mail.fr',
    lastName: 'userLastName',
    firstName: 'userFirstName',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    userService = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created UserService', () => {
    expect(userService).toBeTruthy();
  });

  describe('getById', () => {
    it('should get user by its id', () => {
      let result: User | undefined;

      userService.getById('1').subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}/1`);
      req.flush(user);
      expect(req.request.method).toEqual('GET');
      expect(result).toEqual(user);
    });
  });

  describe('delete', () => {
    it('delete user by its id', () => {
      userService.delete('1').subscribe((response) => {
        response;
      });

      const req = httpTestingController.expectOne(`${pathService}/1`);
      req.flush(user);
      expect(req.request.method).toEqual('DELETE');
    });
  });
});
