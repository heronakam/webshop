(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CommentaireDeleteController',CommentaireDeleteController);

    CommentaireDeleteController.$inject = ['$uibModalInstance', 'entity', 'Commentaire'];

    function CommentaireDeleteController($uibModalInstance, entity, Commentaire) {
        var vm = this;

        vm.commentaire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Commentaire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
