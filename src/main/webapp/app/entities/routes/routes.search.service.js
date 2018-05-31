(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .factory('RoutesSearch', RoutesSearch);

    RoutesSearch.$inject = ['$resource'];

    function RoutesSearch($resource) {
        var resourceUrl =  'api/_search/routes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
