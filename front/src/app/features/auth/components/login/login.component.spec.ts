import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { By } from '@angular/platform-browser';

describe('LoginComponent', () => {
  let loginComponent: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    loginComponent = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  it('should create LoginComponent', () => {
    expect(loginComponent).toBeTruthy();
  });

  it('should login the user and navigate on success', () => {
    const loginRequest: LoginRequest = {
      email: 'user@mail.fr',
      password: 'password',
    };

    loginComponent.form.setValue(loginRequest);

    const sessionInformation: SessionInformation = {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastaName',
      admin: false,
    };

    const loginSPy = jest
      .spyOn(authService, 'login')
      .mockImplementation(() => of(sessionInformation));

    const routerSpy = jest
      .spyOn(router, 'navigate')
      .mockImplementation(() => Promise.resolve(true));

    const sessionSpy = jest.spyOn(sessionService, 'logIn');

    loginComponent.submit();
    fixture.detectChanges();

    expect(loginSPy).toHaveBeenCalledWith(loginRequest);
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
    expect(sessionSpy).toHaveBeenCalledWith(sessionInformation);
    expect(loginComponent.onError).toBeFalsy();
  });

  it('should not login the user and display error message', () => {
    const loginRequest: LoginRequest = {
      email: 'user@mail.fr',
      password: 'password',
    };

    loginComponent.form.setValue(loginRequest);

    const loginSPy = jest
      .spyOn(authService, 'login')
      .mockImplementation(() => throwError(() => new Error()));

    loginComponent.submit();
    fixture.detectChanges();

    expect(loginComponent.onError).toBeTruthy();

    const errorContainer = fixture.debugElement.query(By.css('.error'));
    expect(errorContainer.nativeElement.textContent).toBe('An error occurred');

    expect(loginSPy).toHaveBeenCalledWith(loginRequest);
    expect(router.navigate).not.toHaveBeenCalled;
  });

  it('should disable the submit button if the form has an error field', () => {
    const loginRequest: LoginRequest = {
      email: '',
      password: 'paswword',
    };

    loginComponent.form.setValue(loginRequest);

    expect(loginComponent.form.invalid).toBeTruthy();

    const submitButton = fixture.debugElement.query(By.css('[type="submit"]'));
    expect(submitButton.nativeElement.disabled).toBeTruthy();
  });
});
