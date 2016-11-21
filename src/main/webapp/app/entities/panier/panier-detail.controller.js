(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('PanierDetailController', PanierDetailController);

    PanierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Panier', 'Produit', 'User'];

    function PanierDetailController($scope, $rootScope, $stateParams, previousState, entity, Panier, Produit, User) {
        var vm = this;

        vm.panier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shopApp:panierUpdate', function(event, result) {
            vm.panier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
