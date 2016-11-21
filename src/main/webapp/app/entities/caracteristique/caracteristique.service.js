(function() {
    'use strict';
    angular
        .module('shopApp')
        .factory('Caracteristique', Caracteristique);

    Caracteristique.$inject = ['$resource'];

    function Caracteristique ($resource) {
        var resourceUrl =  'api/caracteristiques/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
