(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('produit', {
            parent: 'entity',
            url: '/produit',
            data: {
                // authorities: ['ROLE_USER'],

                pageTitle: 'shopApp.produit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/produit/produits.html',
                    controller: 'ProduitController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('produit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('produit-detail', {
            parent: 'entity',
            url: '/produit/{id}',
            data: {
                authorities: [],
                pageTitle: 'shopApp.produit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/produit/produit-detail.html',
                    controller: 'ProduitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('produit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Produit', function($stateParams, Produit) {
                    return Produit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'produit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('produit-detail.edit', {
            parent: 'produit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produit/produit-dialog.html',
                    controller: 'ProduitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Produit', function(Produit) {
                            return Produit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('produit.new', {
            parent: 'produit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produit/produit-dialog.html',
                    controller: 'ProduitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                imageUrl: null,
                                prixUnitaire: null,
                                solde: null,
                                qteEnStock: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('produit', null, { reload: 'produit' });
                }, function() {
                    $state.go('produit');
                });
            }]
        })
        .state('produit.edit', {
            parent: 'produit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produit/produit-dialog.html',
                    controller: 'ProduitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Produit', function(Produit) {
                            return Produit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('produit', null, { reload: 'produit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('produit.delete', {
            parent: 'produit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produit/produit-delete-dialog.html',
                    controller: 'ProduitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Produit', function(Produit) {
                            return Produit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('produit', null, { reload: 'produit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
