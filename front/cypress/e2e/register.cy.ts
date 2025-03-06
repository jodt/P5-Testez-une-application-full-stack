describe('register spec', () => {

  it('register successfull', () => {

    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: { message: 'User registered successfully!' },
    });

    cy.get('input[formControlName=firstName]').type('userFirstName');
    cy.get('input[formControlName=lastName]').type('userLastName');
    cy.get('input[formControlName=email]').type('user@gmail.com');
    cy.get('input[formControlName=password]').type('password');
    cy.get('button[type=submit]').click();

    cy.url().should('include', '/login');
  });


  it('should disable Submit button if a required field is not filled', () => {

    cy.visit('/register');
    
    cy.get('input[formControlName=lastName]').type('userLastName');
    cy.get('input[formControlName=email]').type('user@gmail.com');
    cy.get('input[formControlName=password]').type('password');

    cy.get('button[type=submit]').should('be.disabled');
  });


  it('should display an error message if a field is on error', () => {

    const errorMessage = 'An error occurred';

    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {},
    });

    cy.get('input[formControlName=firstName]').type('us');
    cy.get('input[formControlName=lastName]').type('userLastName');
    cy.get('input[formControlName=email]').type('user@gmail.com');
    cy.get('input[formControlName=password]').type('password');
    cy.get('button[type=submit]').click();

    cy.url().should('include', '/register');
    cy.contains(errorMessage).should('be.visible');
  });

  it('should display an error message if a user is already registered', () => {

    const errorMessage = 'An error occurred';

    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: { message: 'Error: Email is already taken!' },
    });

    cy.get('input[formControlName=firstName]').type('userLastName');
    cy.get('input[formControlName=lastName]').type('userLastName');
    cy.get('input[formControlName=email]').type('user@gmail.com');
    cy.get('input[formControlName=password]').type('password');
    cy.get('button[type=submit]').click();

    cy.url().should('include', '/register');
    cy.contains(errorMessage).should('be.visible');
  });
});
