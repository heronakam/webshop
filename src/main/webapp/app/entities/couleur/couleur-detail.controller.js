(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CouleurDetailController', CouleurDetailController);

    CouleurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Couleur', 'Produit'];

    function CouleurDetailController($scope, $rootScope, $stateParams, previousState, entity, Couleur, Produit) {
        var vm = this;

        vm.couleur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shopApp:couleurUpdate', function(event, result) {
            vm.couleur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
