describe('me spec', () => {

  it('should display user information', () => {
    const user = {
      id: 1,
      email: 'user@mail.com',
      lastName: 'lastName',
      firstName: 'firstName',
      admin: false,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    );

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      user
    ).as('user');

    cy.get('span[routerLink="me" ]').click();

    cy.contains(`${user.firstName} ${user.lastName.toUpperCase()}`).should('be.visible');
    cy.contains(`${user.email}`).should('be.visible');
    cy.contains('delete').should('be.visible');
  });
});
