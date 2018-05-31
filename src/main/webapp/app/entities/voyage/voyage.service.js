(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Voyage', Voyage);

    Voyage.$inject = ['$resource', 'DateUtils'];

    function Voyage ($resource, DateUtils) {
        var resourceUrl =  'api/voyages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datedepart = DateUtils.convertDateTimeFromServer(data.datedepart);
                        data.datearrive = DateUtils.convertDateTimeFromServer(data.datearrive);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
