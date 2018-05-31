(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Wagon', Wagon);

    Wagon.$inject = ['$resource'];

    function Wagon ($resource) {
        var resourceUrl =  'api/wagons/:id';

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
