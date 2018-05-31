(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('PassengerController', PassengerController);

    PassengerController.$inject = ['Passenger', 'PassengerSearch'];

    function PassengerController(Passenger, PassengerSearch) {

        var vm = this;

        vm.passengers = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Passenger.query(function(result) {
                vm.passengers = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PassengerSearch.query({query: vm.searchQuery}, function(result) {
                vm.passengers = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
