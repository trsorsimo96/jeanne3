(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .factory('MonthSearch', MonthSearch);

    MonthSearch.$inject = ['$resource'];

    function MonthSearch($resource) {
        var resourceUrl =  'api/_search/months/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
