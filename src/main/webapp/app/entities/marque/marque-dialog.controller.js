(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('MarqueDialogController', MarqueDialogController);

    MarqueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Marque', 'Produit'];

    function MarqueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Marque, Produit) {
        var vm = this;

        vm.marque = entity;
        vm.clear = clear;
        vm.save = save;
        vm.produits = Produit.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.marque.id !== null) {
                Marque.update(vm.marque, onSaveSuccess, onSaveError);
            } else {
                Marque.save(vm.marque, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shopApp:marqueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
