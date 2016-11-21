(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('caracteristique', {
            parent: 'entity',
            url: '/caracteristique',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.caracteristique.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/caracteristique/caracteristiques.html',
                    controller: 'CaracteristiqueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('caracteristique');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('caracteristique-detail', {
            parent: 'entity',
            url: '/caracteristique/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.caracteristique.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/caracteristique/caracteristique-detail.html',
                    controller: 'CaracteristiqueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('caracteristique');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Caracteristique', function($stateParams, Caracteristique) {
                    return Caracteristique.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'caracteristique',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('caracteristique-detail.edit', {
            parent: 'caracteristique-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caracteristique/caracteristique-dialog.html',
                    controller: 'CaracteristiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Caracteristique', function(Caracteristique) {
                            return Caracteristique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('caracteristique.new', {
            parent: 'caracteristique',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caracteristique/caracteristique-dialog.html',
                    controller: 'CaracteristiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('caracteristique', null, { reload: 'caracteristique' });
                }, function() {
                    $state.go('caracteristique');
                });
            }]
        })
        .state('caracteristique.edit', {
            parent: 'caracteristique',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caracteristique/caracteristique-dialog.html',
                    controller: 'CaracteristiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Caracteristique', function(Caracteristique) {
                            return Caracteristique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('caracteristique', null, { reload: 'caracteristique' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('caracteristique.delete', {
            parent: 'caracteristique',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caracteristique/caracteristique-delete-dialog.html',
                    controller: 'CaracteristiqueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Caracteristique', function(Caracteristique) {
                            return Caracteristique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('caracteristique', null, { reload: 'caracteristique' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
