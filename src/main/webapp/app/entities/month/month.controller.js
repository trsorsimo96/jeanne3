(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('MonthController', MonthController);

    MonthController.$inject = ['Month', 'MonthSearch'];

    function MonthController(Month, MonthSearch) {

        var vm = this;

        vm.months = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Month.query(function(result) {
                vm.months = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MonthSearch.query({query: vm.searchQuery}, function(result) {
                vm.months = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
