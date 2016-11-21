(function() {
    'use strict';

    angular
        .module('shopApp')
        .factory('PanierSearch', PanierSearch);

    PanierSearch.$inject = ['$resource'];

    function PanierSearch($resource) {
        var resourceUrl =  'api/_search/paniers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
