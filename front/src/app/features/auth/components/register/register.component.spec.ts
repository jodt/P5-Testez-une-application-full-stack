import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('RegisterComponent', () => {
  let registerComponent: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;
  let authService: AuthService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    registerComponent = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it('should create RegisterComponent', () => {
    expect(registerComponent).toBeTruthy();
  });

  it('should submit register form and navigate on success', () => {
    const registerRequest = {
      email: 'test@test.fr',
      firstName: 'testPrenom',
      lastName: 'testNom',
      password: 'test!1234',
    };

    registerComponent.form.setValue(registerRequest);

    const registerSPy = jest
      .spyOn(authService, 'register')
      .mockImplementation(() => of(undefined));
      
    const routerSpy = jest
      .spyOn(router, 'navigate')
      .mockImplementation(() => Promise.resolve(true));

    registerComponent.submit();

    expect(registerSPy).toHaveBeenCalledWith(registerRequest);
    expect(routerSpy).toHaveBeenCalledWith(['/login']);
    expect(registerComponent.onError).toBeFalsy();
  });

  it('should set error to true and display error message', () => {
    const registerRequest = {
      email: 'test@test.fr',
      firstName: 'te',
      lastName: 'testNom',
      password: 'test!1234',
    };

    registerComponent.form.setValue(registerRequest);

    const registerSPy = jest
      .spyOn(authService, 'register')
      .mockImplementation(() => throwError(() => new Error()));

    registerComponent.submit();
    fixture.detectChanges();

    expect(registerComponent.onError).toBeTruthy();

    const errorContainer = fixture.debugElement.query(By.css('.error'));
    expect(errorContainer.nativeElement.textContent).toBe('An error occurred');

    expect(authService.register(registerRequest)).toHaveBeenCalled;
    expect(router.navigate).not.toHaveBeenCalled;
  });
});
