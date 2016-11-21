(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('panier', {
            parent: 'entity',
            url: '/panier',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.panier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/panier/paniers.html',
                    controller: 'PanierController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('panier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('panier-detail', {
            parent: 'entity',
            url: '/panier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.panier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/panier/panier-detail.html',
                    controller: 'PanierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('panier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Panier', function($stateParams, Panier) {
                    return Panier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'panier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('panier-detail.edit', {
            parent: 'panier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/panier/panier-dialog.html',
                    controller: 'PanierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Panier', function(Panier) {
                            return Panier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('panier.new', {
            parent: 'panier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/panier/panier-dialog.html',
                    controller: 'PanierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('panier', null, { reload: 'panier' });
                }, function() {
                    $state.go('panier');
                });
            }]
        })
        .state('panier.edit', {
            parent: 'panier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/panier/panier-dialog.html',
                    controller: 'PanierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Panier', function(Panier) {
                            return Panier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('panier', null, { reload: 'panier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('panier.delete', {
            parent: 'panier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/panier/panier-delete-dialog.html',
                    controller: 'PanierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Panier', function(Panier) {
                            return Panier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('panier', null, { reload: 'panier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
