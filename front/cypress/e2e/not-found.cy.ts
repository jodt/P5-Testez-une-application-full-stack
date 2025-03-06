describe('not-found spec', () => {

  it('shoud display not found page', () => {

    cy.visit('/not found');

    cy.contains('Page not found !').should('be.visible');
  });
});
