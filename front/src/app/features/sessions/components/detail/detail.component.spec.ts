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
import { ActivatedRoute, Router } from '@angular/router';

describe('DetailComponent', () => {
  let detailComponent: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let router: Router;
  let activatedRoute: ActivatedRoute;
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

  const mockApiSessionService = {
    detail: jest.fn(() => of(mockSession)),
    delete: jest.fn(() => of(undefined)),
    participate: jest.fn(() => of(undefined)),
    unParticipate: jest.fn(() => of(undefined)),
  };

  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'lastName',
    firstName: 'firstName',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockTeacherService = {
    detail: jest.fn(() => of(mockSession)),
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
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockApiSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
      ],
    }).compileComponents();

    service = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    activatedRoute = TestBed.inject(ActivatedRoute);

    fixture = TestBed.createComponent(DetailComponent);
    detailComponent = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create DetailComponent', () => {
    expect(detailComponent).toBeTruthy();
  });

  it('should participate', () => {
    const activatedRouteSpy = jest
      .spyOn(activatedRoute.snapshot.paramMap, 'get')
      .mockImplementation(() => '1');

    detailComponent.participate();

    expect(sessionApiService.participate).toHaveBeenCalledWith(
      detailComponent.sessionId,
      detailComponent.userId
    );
    expect(detailComponent.isParticipate).toBe(true);
  });

  it('should unParticipate', () => {
    mockSession.users = [41];

    const activatedRouteSpy = jest
      .spyOn(activatedRoute.snapshot.paramMap, 'get')
      .mockImplementation(() => '1');

    detailComponent.unParticipate();

    expect(sessionApiService.participate).toHaveBeenCalledWith(
      detailComponent.sessionId,
      detailComponent.userId
    );
    expect(detailComponent.isParticipate).toBe(false);
  });

  it('should delete the session', () => {
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
  });

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
