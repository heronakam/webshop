(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('couleur', {
            parent: 'entity',
            url: '/couleur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.couleur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/couleur/couleurs.html',
                    controller: 'CouleurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('couleur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('couleur-detail', {
            parent: 'entity',
            url: '/couleur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.couleur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/couleur/couleur-detail.html',
                    controller: 'CouleurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('couleur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Couleur', function($stateParams, Couleur) {
                    return Couleur.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'couleur',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('couleur-detail.edit', {
            parent: 'couleur-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/couleur/couleur-dialog.html',
                    controller: 'CouleurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Couleur', function(Couleur) {
                            return Couleur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('couleur.new', {
            parent: 'couleur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/couleur/couleur-dialog.html',
                    controller: 'CouleurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                codeHexadecimal: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('couleur', null, { reload: 'couleur' });
                }, function() {
                    $state.go('couleur');
                });
            }]
        })
        .state('couleur.edit', {
            parent: 'couleur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/couleur/couleur-dialog.html',
                    controller: 'CouleurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Couleur', function(Couleur) {
                            return Couleur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('couleur', null, { reload: 'couleur' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('couleur.delete', {
            parent: 'couleur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/couleur/couleur-delete-dialog.html',
                    controller: 'CouleurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Couleur', function(Couleur) {
                            return Couleur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('couleur', null, { reload: 'couleur' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
