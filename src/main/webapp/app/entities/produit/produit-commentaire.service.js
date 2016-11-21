/**
 * Created by HERONAKAM on 21/11/2016.
 */

(function() {
    'use strict';

    angular
        .module('shopApp')
        .factory('ProduitCommentaire', ProduitCommentaire);

    ProduitCommentaire.$inject = ['$resource'];

    function ProduitCommentaire($resource) {
        var resourceUrl =  'api/produit/commentaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }

})();
