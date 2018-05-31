(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('DayController', DayController);

    DayController.$inject = ['Day', 'DaySearch'];

    function DayController(Day, DaySearch) {

        var vm = this;

        vm.days = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Day.query(function(result) {
                vm.days = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DaySearch.query({query: vm.searchQuery}, function(result) {
                vm.days = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
