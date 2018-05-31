(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('CarController', CarController);

    CarController.$inject = ['Car', 'CarSearch'];

    function CarController(Car, CarSearch) {

        var vm = this;

        vm.cars = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Car.query(function(result) {
                vm.cars = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CarSearch.query({query: vm.searchQuery}, function(result) {
                vm.cars = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
