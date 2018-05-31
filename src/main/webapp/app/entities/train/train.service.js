(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Train', Train);

    Train.$inject = ['$resource'];

    function Train ($resource) {
        var resourceUrl =  'api/trains/:id';

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
