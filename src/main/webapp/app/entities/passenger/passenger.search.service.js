(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .factory('PassengerSearch', PassengerSearch);

    PassengerSearch.$inject = ['$resource'];

    function PassengerSearch($resource) {
        var resourceUrl =  'api/_search/passengers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
