import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { provideRouter, Router } from '@angular/router';
import { SessionService } from './services/session.service';


describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>
  let app: AppComponent;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);

  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('should logout', () => {
    router.navigate = jest.fn();
    sessionService.logOut= jest.fn();
    app.logout();
    expect(sessionService.logOut).toBeCalled();
    expect(router.navigate).toHaveBeenLastCalledWith([""]);
  })
});
