(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('BookingController', BookingController);

    BookingController.$inject = ['Booking', 'BookingSearch'];

    function BookingController(Booking, BookingSearch) {

        var vm = this;

        vm.bookings = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Booking.query(function(result) {
                vm.bookings = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BookingSearch.query({query: vm.searchQuery}, function(result) {
                vm.bookings = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
