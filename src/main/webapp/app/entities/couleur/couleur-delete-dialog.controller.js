(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CouleurDeleteController',CouleurDeleteController);

    CouleurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Couleur'];

    function CouleurDeleteController($uibModalInstance, entity, Couleur) {
        var vm = this;

        vm.couleur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Couleur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
