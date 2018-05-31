(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Agency', Agency);

    Agency.$inject = ['$resource'];

    function Agency ($resource) {
        var resourceUrl =  'api/agencies/:id';

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
