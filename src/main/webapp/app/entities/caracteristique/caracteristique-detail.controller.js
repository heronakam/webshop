(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CaracteristiqueDetailController', CaracteristiqueDetailController);

    CaracteristiqueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Caracteristique', 'Produit'];

    function CaracteristiqueDetailController($scope, $rootScope, $stateParams, previousState, entity, Caracteristique, Produit) {
        var vm = this;

        vm.caracteristique = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shopApp:caracteristiqueUpdate', function(event, result) {
            vm.caracteristique = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
