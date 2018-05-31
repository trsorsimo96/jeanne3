(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Email', Email);

    Email.$inject = ['$resource'];

    function Email ($resource) {
        var resourceUrl =  'api/emails/:id';

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
