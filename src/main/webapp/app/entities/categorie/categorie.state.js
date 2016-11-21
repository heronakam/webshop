(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('categorie', {
            parent: 'entity',
            url: '/categorie',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.categorie.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/categorie/categories.html',
                    controller: 'CategorieController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('categorie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('categorie-detail', {
            parent: 'entity',
            url: '/categorie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.categorie.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/categorie/categorie-detail.html',
                    controller: 'CategorieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('categorie');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Categorie', function($stateParams, Categorie) {
                    return Categorie.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'categorie',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('categorie-detail.edit', {
            parent: 'categorie-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorie/categorie-dialog.html',
                    controller: 'CategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Categorie', function(Categorie) {
                            return Categorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('categorie.new', {
            parent: 'categorie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorie/categorie-dialog.html',
                    controller: 'CategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                iconUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('categorie', null, { reload: 'categorie' });
                }, function() {
                    $state.go('categorie');
                });
            }]
        })
        .state('categorie.edit', {
            parent: 'categorie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorie/categorie-dialog.html',
                    controller: 'CategorieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Categorie', function(Categorie) {
                            return Categorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('categorie', null, { reload: 'categorie' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('categorie.delete', {
            parent: 'categorie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorie/categorie-delete-dialog.html',
                    controller: 'CategorieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Categorie', function(Categorie) {
                            return Categorie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('categorie', null, { reload: 'categorie' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
