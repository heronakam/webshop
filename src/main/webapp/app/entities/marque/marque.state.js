(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('marque', {
            parent: 'entity',
            url: '/marque',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.marque.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/marque/marques.html',
                    controller: 'MarqueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('marque');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('marque-detail', {
            parent: 'entity',
            url: '/marque/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.marque.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/marque/marque-detail.html',
                    controller: 'MarqueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('marque');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Marque', function($stateParams, Marque) {
                    return Marque.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'marque',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('marque-detail.edit', {
            parent: 'marque-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/marque/marque-dialog.html',
                    controller: 'MarqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Marque', function(Marque) {
                            return Marque.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('marque.new', {
            parent: 'marque',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/marque/marque-dialog.html',
                    controller: 'MarqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                presentation: null,
                                logoUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('marque', null, { reload: 'marque' });
                }, function() {
                    $state.go('marque');
                });
            }]
        })
        .state('marque.edit', {
            parent: 'marque',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/marque/marque-dialog.html',
                    controller: 'MarqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Marque', function(Marque) {
                            return Marque.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('marque', null, { reload: 'marque' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('marque.delete', {
            parent: 'marque',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/marque/marque-delete-dialog.html',
                    controller: 'MarqueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Marque', function(Marque) {
                            return Marque.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('marque', null, { reload: 'marque' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
