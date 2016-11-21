(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commentaire', {
            parent: 'entity',
            url: '/commentaire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.commentaire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commentaire/commentaires.html',
                    controller: 'CommentaireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commentaire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('commentaire-detail', {
            parent: 'entity',
            url: '/commentaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shopApp.commentaire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commentaire/commentaire-detail.html',
                    controller: 'CommentaireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commentaire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Commentaire', function($stateParams, Commentaire) {
                    return Commentaire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'commentaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('commentaire-detail.edit', {
            parent: 'commentaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentaire/commentaire-dialog.html',
                    controller: 'CommentaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commentaire', function(Commentaire) {
                            return Commentaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commentaire.new', {
            parent: 'commentaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentaire/commentaire-dialog.html',
                    controller: 'CommentaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                note: null,
                                avis: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('commentaire', null, { reload: 'commentaire' });
                }, function() {
                    $state.go('commentaire');
                });
            }]
        })
        .state('commentaire.edit', {
            parent: 'commentaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentaire/commentaire-dialog.html',
                    controller: 'CommentaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commentaire', function(Commentaire) {
                            return Commentaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commentaire', null, { reload: 'commentaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commentaire.delete', {
            parent: 'commentaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commentaire/commentaire-delete-dialog.html',
                    controller: 'CommentaireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Commentaire', function(Commentaire) {
                            return Commentaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commentaire', null, { reload: 'commentaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
