(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CouleurDialogController', CouleurDialogController);

    CouleurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Couleur', 'Produit'];

    function CouleurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Couleur, Produit) {
        var vm = this;

        vm.couleur = entity;
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
            if (vm.couleur.id !== null) {
                Couleur.update(vm.couleur, onSaveSuccess, onSaveError);
            } else {
                Couleur.save(vm.couleur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shopApp:couleurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
