describe('Login spec', () => {
  it('Login successfull', () => {

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
  });


  it('should disable Submit button if a required field is not filled', () => {
    cy.visit('/login');

    cy.get('input[formControlName=password]').type(`${'test!1234'}`);

    cy.get('button[type=submit]').should('be.disabled');
  });
  

  it('should display an error message if the credentials are wrong ', () => {
    const errorMessage = 'An error occurred';

    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 400,
      body: {},
    });

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );
    cy.get('button[type=submit]').click();

    cy.url().should('include', '/login');
    cy.contains(errorMessage).should('be.visible');
  });
});
