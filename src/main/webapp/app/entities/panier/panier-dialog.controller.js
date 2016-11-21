(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('PanierDialogController', PanierDialogController);

    PanierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Panier', 'Produit', 'User'];

    function PanierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Panier, Produit, User) {
        var vm = this;

        vm.panier = entity;
        vm.clear = clear;
        vm.save = save;
        vm.produits = Produit.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.panier.id !== null) {
                Panier.update(vm.panier, onSaveSuccess, onSaveError);
            } else {
                Panier.save(vm.panier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shopApp:panierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
