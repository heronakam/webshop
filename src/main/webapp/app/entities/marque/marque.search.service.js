(function() {
    'use strict';

    angular
        .module('shopApp')
        .factory('MarqueSearch', MarqueSearch);

    MarqueSearch.$inject = ['$resource'];

    function MarqueSearch($resource) {
        var resourceUrl =  'api/_search/marques/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
