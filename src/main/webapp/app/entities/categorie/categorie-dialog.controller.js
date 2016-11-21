(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CategorieDialogController', CategorieDialogController);

    CategorieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Categorie', 'Produit'];

    function CategorieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Categorie, Produit) {
        var vm = this;

        vm.categorie = entity;
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
            if (vm.categorie.id !== null) {
                Categorie.update(vm.categorie, onSaveSuccess, onSaveError);
            } else {
                Categorie.save(vm.categorie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shopApp:categorieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
