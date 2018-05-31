(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .factory('ItineraireSearch', ItineraireSearch);

    ItineraireSearch.$inject = ['$resource'];

    function ItineraireSearch($resource) {
        var resourceUrl =  'api/_search/itineraires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
