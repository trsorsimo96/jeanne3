(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .factory('CarSearch', CarSearch);

    CarSearch.$inject = ['$resource'];

    function CarSearch($resource) {
        var resourceUrl =  'api/_search/cars/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
