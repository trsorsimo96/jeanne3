(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Segment', Segment);

    Segment.$inject = ['$resource'];

    function Segment ($resource) {
        var resourceUrl =  'api/segments/:id';

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
