import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { By } from '@angular/platform-browser';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { Observable, of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';

describe('ListComponent', () => {
  let listComponent: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockSessions: Session[] = [
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
  ];

  const mockSessionApiService = {
    all: jest.fn(() => of(mockSessions)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        RouterTestingModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    listComponent = fixture.componentInstance;
    fixture.detectChanges();
    sessionApiService = TestBed.inject(SessionApiService);
  });

  it('should create ListComponent', () => {
    expect(listComponent).toBeTruthy();
  });

  it('should retrieve user session information', () => {
    const userSessionInfo = listComponent.user;

    expect(userSessionInfo).toEqual(mockSessionService.sessionInformation);
  });

  it('should display session', () => {
    const cardContainers = fixture.debugElement.queryAll(By.css('.item'));

    expect(cardContainers.length).toBe(1);
    expect(cardContainers[0].nativeElement.textContent).toContain('yoga');
  });

  it('should show edit button if user is admin', () => {
    mockSessionService.sessionInformation.admin = true;
    fixture.detectChanges();
    const buttonContainers = fixture.debugElement.queryAll(By.css('button'));
    expect(buttonContainers.length).toBe(3);
    expect(buttonContainers[0].nativeElement.textContent).toContain('Create');
    expect(buttonContainers[1].nativeElement.textContent).toContain('Detail');
    expect(buttonContainers[2].nativeElement.textContent).toContain('Edit');
  });

  it('should not show Create and Edit buttons if user is not an administrator', () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();
    const buttonContainers = fixture.debugElement.queryAll(By.css('button'));
    expect(buttonContainers.length).toBe(1);
    expect(buttonContainers[0].nativeElement.textContent).toContain('Detail');
  });
});
