(function() {
    'use strict';

    angular
        .module('shopApp')
        .factory('CouleurSearch', CouleurSearch);

    CouleurSearch.$inject = ['$resource'];

    function CouleurSearch($resource) {
        var resourceUrl =  'api/_search/couleurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
