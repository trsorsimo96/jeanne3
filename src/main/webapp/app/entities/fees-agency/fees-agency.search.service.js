(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .factory('FeesAgencySearch', FeesAgencySearch);

    FeesAgencySearch.$inject = ['$resource'];

    function FeesAgencySearch($resource) {
        var resourceUrl =  'api/_search/fees-agencies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
