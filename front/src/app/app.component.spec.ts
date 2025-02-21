import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import { SessionService } from './services/session.service';
import { of } from 'rxjs';


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
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);

  });

  it('should create appComponent', () => {
    expect(app).toBeTruthy();
  });

  it('should logged', (done) => {
    const loggedSpy = jest.spyOn(sessionService, '$isLogged').mockImplementation(() => of((true)));
    app.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      expect(loggedSpy).toHaveBeenCalledTimes(1);
      done();
    })
  })

  it('should logout', () => {
    const logoutSpy = jest.spyOn(sessionService, 'logOut');
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
    app.logout();
    expect(logoutSpy).toHaveBeenCalled;
    expect(routerSpy).toHaveBeenLastCalledWith([""]);
  })
});


