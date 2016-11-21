(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('PanierDeleteController',PanierDeleteController);

    PanierDeleteController.$inject = ['$uibModalInstance', 'entity', 'Panier'];

    function PanierDeleteController($uibModalInstance, entity, Panier) {
        var vm = this;

        vm.panier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Panier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
