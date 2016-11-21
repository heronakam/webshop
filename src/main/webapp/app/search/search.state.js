(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('search', {
                parent: 'app',
                url: '/search',
                data: {
                    authorities: []
                },
                view:{
                    'content@': {
                        templateUrl: 'app/search/search.html',
                        controller: 'SearchController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('search');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    }

})();
