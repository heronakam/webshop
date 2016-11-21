(function() {
    'use strict';
    angular
        .module('shopApp')
        .factory('Produit', Produit);

    Produit.$inject = ['$resource'];

    function Produit ($resource) {
        var resourceUrl =  'api/produits/:id';

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
