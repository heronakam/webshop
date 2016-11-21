(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CategorieDetailController', CategorieDetailController);

    CategorieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Categorie', 'Produit'];

    function CategorieDetailController($scope, $rootScope, $stateParams, previousState, entity, Categorie, Produit) {
        var vm = this;

        vm.categorie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shopApp:categorieUpdate', function(event, result) {
            vm.categorie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
