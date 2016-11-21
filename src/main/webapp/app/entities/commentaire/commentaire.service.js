(function() {
    'use strict';
    angular
        .module('shopApp')
        .factory('Commentaire', Commentaire);

    Commentaire.$inject = ['$resource'];

    function Commentaire ($resource) {
        var resourceUrl =  'api/commentaires/:id';

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
