(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('MarqueDetailController', MarqueDetailController);

    MarqueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Marque', 'Produit'];

    function MarqueDetailController($scope, $rootScope, $stateParams, previousState, entity, Marque, Produit) {
        var vm = this;

        vm.marque = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shopApp:marqueUpdate', function(event, result) {
            vm.marque = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
