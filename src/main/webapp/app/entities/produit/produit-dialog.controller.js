(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('ProduitDialogController', ProduitDialogController);

    ProduitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Produit', 'Categorie', 'Caracteristique', 'Marque', 'Commentaire', 'Couleur'];

    function ProduitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Produit, Categorie, Caracteristique, Marque, Commentaire, Couleur) {
        var vm = this;

        vm.produit = entity;
        vm.clear = clear;
        vm.save = save;
        vm.categories = Categorie.query();
        vm.caracteristiques = Caracteristique.query();
        vm.marques = Marque.query();
        vm.commentaires = Commentaire.query();
        vm.couleurs = Couleur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.produit.id !== null) {
                Produit.update(vm.produit, onSaveSuccess, onSaveError);
            } else {
                Produit.save(vm.produit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shopApp:produitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
