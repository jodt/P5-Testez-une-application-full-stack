import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { By } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';

describe('DetailComponent', () => {
  let detailComponent: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  let mockSession = {
    id: 1,
    name: 'Yoga',
    description: 'Yoga',
    date: new Date(),
    teacher_id: 1,
    users: [1],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'lastName',
    firstName: 'firstName',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule,
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    }).compileComponents();

    service = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);

    jest
      .spyOn(sessionApiService, 'detail')
      .mockImplementation(() => of(mockSession));
    jest
      .spyOn(teacherService, 'detail')
      .mockImplementation(() => of(mockTeacher));

    fixture = TestBed.createComponent(DetailComponent);
    detailComponent = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create DetailComponent', () => {
    expect(detailComponent).toBeTruthy();
  });

  it('should participate', () => {
    jest
      .spyOn(sessionApiService, 'participate')
      .mockImplementation(() => of(undefined));

    detailComponent.participate();

    expect(detailComponent.isParticipate).toBe(true);
  });

  it('should unParticipate', () => {
    mockSession.users = [41];

    jest
      .spyOn(sessionApiService, 'unParticipate')
      .mockImplementation(() => of(undefined));

    detailComponent.unParticipate();

    expect(detailComponent.isParticipate).toBe(false);
  });

  it('should delete the session', () => {
    jest
      .spyOn(sessionApiService, 'delete')
      .mockImplementation(() => of(undefined));

    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open').mockImplementation();

    const routerSpy = jest
      .spyOn(router, 'navigate')
      .mockImplementation(() => Promise.resolve(true));

    detailComponent.delete();

    expect(matSnackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', {
      duration: 3000,
    });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('sould display session information', () => {
    const sessionTitleContainer = fixture.debugElement.query(By.css('h1'));

    expect(sessionTitleContainer).toBeTruthy();
    expect(sessionTitleContainer.nativeElement.textContent).toContain('Yoga');
  })

  it('should display delete button if user is admin', () => {
    const deleteButtonContainers = fixture.debugElement.queryAll(
      By.css('button')
    );

    expect(deleteButtonContainers[1]).toBeTruthy();
    expect(deleteButtonContainers[1].nativeElement.textContent).toContain(
      'delete'
    );
  });
});
