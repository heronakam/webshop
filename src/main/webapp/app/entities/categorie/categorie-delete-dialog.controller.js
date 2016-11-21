(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CategorieDeleteController',CategorieDeleteController);

    CategorieDeleteController.$inject = ['$uibModalInstance', 'entity', 'Categorie'];

    function CategorieDeleteController($uibModalInstance, entity, Categorie) {
        var vm = this;

        vm.categorie = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Categorie.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
