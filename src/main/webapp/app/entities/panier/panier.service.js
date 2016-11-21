(function() {
    'use strict';
    angular
        .module('shopApp')
        .factory('Panier', Panier);

    Panier.$inject = ['$resource'];

    function Panier ($resource) {
        var resourceUrl =  'api/paniers/:id';

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
