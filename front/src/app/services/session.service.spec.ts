import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let sessionService: SessionService;

  const user: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'username',
    firstName: 'userFirstName',
    lastName: 'userLastName',
    admin: false,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sessionService = TestBed.inject(SessionService);
  });

  it('should be created SessionService', () => {
    expect(sessionService).toBeTruthy();
  });

  describe('logIn', () => {
    it('should set session information and islogged boolean', () => {
      sessionService.logIn(user);
      expect(sessionService.isLogged).toBe(true);
      expect(sessionService.sessionInformation).toEqual(user);
    });
  });

  describe('logOut', () => {
    it('should remove session information and set islogged boolean to false', () => {
      sessionService.logOut();
      expect(sessionService.isLogged).toBe(false);
      expect(sessionService.sessionInformation).toEqual(undefined);
    });
  });
});
