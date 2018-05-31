(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('ModelCar', ModelCar);

    ModelCar.$inject = ['$resource'];

    function ModelCar ($resource) {
        var resourceUrl =  'api/model-cars/:id';

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
