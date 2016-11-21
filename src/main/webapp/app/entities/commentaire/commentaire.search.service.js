(function() {
    'use strict';

    angular
        .module('shopApp')
        .factory('CommentaireSearch', CommentaireSearch);

    CommentaireSearch.$inject = ['$resource'];

    function CommentaireSearch($resource) {
        var resourceUrl =  'api/_search/commentaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
