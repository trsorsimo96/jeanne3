(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Booking', Booking);

    Booking.$inject = ['$resource', 'DateUtils'];

    function Booking ($resource, DateUtils) {
        var resourceUrl =  'api/bookings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.bookingDate = DateUtils.convertDateTimeFromServer(data.bookingDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
