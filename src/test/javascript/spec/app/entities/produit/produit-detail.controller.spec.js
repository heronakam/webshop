'use strict';

describe('Controller Tests', function() {

    describe('Produit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProduit, MockCategorie, MockCaracteristique, MockMarque, MockCommentaire, MockCouleur, MockPanier;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProduit = jasmine.createSpy('MockProduit');
            MockCategorie = jasmine.createSpy('MockCategorie');
            MockCaracteristique = jasmine.createSpy('MockCaracteristique');
            MockMarque = jasmine.createSpy('MockMarque');
            MockCommentaire = jasmine.createSpy('MockCommentaire');
            MockCouleur = jasmine.createSpy('MockCouleur');
            MockPanier = jasmine.createSpy('MockPanier');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Produit': MockProduit,
                'Categorie': MockCategorie,
                'Caracteristique': MockCaracteristique,
                'Marque': MockMarque,
                'Commentaire': MockCommentaire,
                'Couleur': MockCouleur,
                'Panier': MockPanier
            };
            createController = function() {
                $injector.get('$controller')("ProduitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shopApp:produitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
