(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('ModelTrain', ModelTrain);

    ModelTrain.$inject = ['$resource'];

    function ModelTrain ($resource) {
        var resourceUrl =  'api/model-trains/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
