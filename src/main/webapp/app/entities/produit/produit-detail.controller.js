(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('ProduitDetailController', ProduitDetailController);

    ProduitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Produit', 'Categorie', 'Caracteristique', 'Marque', 'Commentaire', 'Couleur'];

    function ProduitDetailController($scope, $rootScope, $stateParams, previousState, entity, Produit, Categorie, Caracteristique, Marque, Commentaire, Couleur) {
        var vm = this;

        vm.produit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shopApp:produitUpdate', function(event, result) {
            vm.produit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
