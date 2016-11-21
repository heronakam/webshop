(function() {
    'use strict';
    angular
        .module('shopApp')
        .factory('Marque', Marque);

    Marque.$inject = ['$resource'];

    function Marque ($resource) {
        var resourceUrl =  'api/marques/:id';

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
