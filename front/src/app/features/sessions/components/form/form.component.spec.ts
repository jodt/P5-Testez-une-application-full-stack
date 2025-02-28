import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('FormComponent', () => {
  let formComponent: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let matSnackBar: MatSnackBar;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
      ],
      declarations: [FormComponent],
    }).compileComponents();

    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    sessionApiService = TestBed.inject(SessionApiService);

    fixture = TestBed.createComponent(FormComponent);
    formComponent = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create FormComponent', () => {
    expect(formComponent).toBeTruthy();
  });

  it('sould create session and redirect the user', () => {
    const session: Session = {
      id: 1,
      name: 'yoga',
      description: 'yoga for beginners',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    jest
      .spyOn(sessionApiService, 'create')
      .mockImplementation(() => of(session));
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open').mockImplementation();
    const routerSpy = jest
      .spyOn(router, 'navigate')
      .mockImplementation(() => Promise.resolve(true));

    formComponent.submit();

    expect(matSnackBarSpy).toHaveBeenCalledWith('Session created !', 'Close', {
      duration: 3000,
    });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('sould update session and redirect the user', () => {
    formComponent.onUpdate = true;

    const session: Session = {
      id: 1,
      name: 'yoga',
      description: 'yoga for beginners',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    jest
      .spyOn(sessionApiService, 'update')
      .mockImplementation(() => of(session));
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open').mockImplementation();
    const routerSpy = jest
      .spyOn(router, 'navigate')
      .mockImplementation(() => Promise.resolve(true));

    formComponent.submit();

    expect(matSnackBarSpy).toHaveBeenCalledWith('Session updated !', 'Close', {
      duration: 3000,
    });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should disable the Save button if the form has an error field', () => {
    const session = {
      name: '',
      description: 'yoga for beginners',
      date: new Date(),
      teacher_id: 1,
    };

    formComponent.sessionForm?.setValue(session);

    expect(formComponent.sessionForm?.invalid).toBeTruthy();

    const submitButton = fixture.debugElement.query(By.css('[type="submit"]'));
    expect(submitButton.nativeElement.disabled).toBeTruthy();
  });
});
