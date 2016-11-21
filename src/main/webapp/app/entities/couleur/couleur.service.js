(function() {
    'use strict';
    angular
        .module('shopApp')
        .factory('Couleur', Couleur);

    Couleur.$inject = ['$resource'];

    function Couleur ($resource) {
        var resourceUrl =  'api/couleurs/:id';

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
