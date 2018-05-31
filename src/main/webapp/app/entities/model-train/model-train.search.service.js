(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .factory('ModelTrainSearch', ModelTrainSearch);

    ModelTrainSearch.$inject = ['$resource'];

    function ModelTrainSearch($resource) {
        var resourceUrl =  'api/_search/model-trains/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
