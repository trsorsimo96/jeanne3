(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .factory('FareSearch', FareSearch);

    FareSearch.$inject = ['$resource'];

    function FareSearch($resource) {
        var resourceUrl =  'api/_search/fares/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
