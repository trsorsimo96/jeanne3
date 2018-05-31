(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('CityController', CityController);

    CityController.$inject = ['City', 'CitySearch'];

    function CityController(City, CitySearch) {

        var vm = this;

        vm.cities = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            City.query(function(result) {
                vm.cities = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CitySearch.query({query: vm.searchQuery}, function(result) {
                vm.cities = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
