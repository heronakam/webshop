(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CaracteristiqueDeleteController',CaracteristiqueDeleteController);

    CaracteristiqueDeleteController.$inject = ['$uibModalInstance', 'entity', 'Caracteristique'];

    function CaracteristiqueDeleteController($uibModalInstance, entity, Caracteristique) {
        var vm = this;

        vm.caracteristique = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Caracteristique.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
