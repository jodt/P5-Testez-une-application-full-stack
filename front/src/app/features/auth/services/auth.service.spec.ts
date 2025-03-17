import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';

describe('AuthService', () => {
  let authService: AuthService;
  let httpTestingController: HttpTestingController;
  const pathService = 'api/auth';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    authService = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created AuthService', () => {
    expect(authService).toBeTruthy();
  });

  describe('register', () => {
    it('should register the user', () => {
      const registerRequest: RegisterRequest = {
        email: 'test@test.fr',
        firstName: 'testPrenom',
        lastName: 'testNom',
        password: 'password',
      };

      authService.register(registerRequest).subscribe((response) => {
        response;
      });

      const req = httpTestingController.expectOne(`${pathService}/register`);
      expect(req.request.method).toEqual('POST');
      req.flush(null);
    });
  });

  describe('register', () => {
    it('should login the user and retrieve the user session information', () => {
      let result: SessionInformation | undefined;

      const loginRequest: LoginRequest = {
        email: 'test@test.fr',
        password: 'password',
      };

      const sessionInformation: SessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'testPrenom',
        lastName: 'testNom',
        admin: false,
      };

      authService.login(loginRequest).subscribe((response) => {
        result = response;
      });

      const req = httpTestingController.expectOne(`${pathService}/login`);
      expect(req.request.method).toEqual('POST');
      req.flush(sessionInformation);
      expect(result).toEqual(sessionInformation);
    });
  });
});
