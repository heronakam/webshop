(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('CommentaireDetailController', CommentaireDetailController);

    CommentaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Commentaire', 'User', 'Produit'];

    function CommentaireDetailController($scope, $rootScope, $stateParams, previousState, entity, Commentaire, User, Produit) {
        var vm = this;

        vm.commentaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shopApp:commentaireUpdate', function(event, result) {
            vm.commentaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
