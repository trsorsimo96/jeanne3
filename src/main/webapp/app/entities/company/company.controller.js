(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['DataUtils', 'Company', 'CompanySearch'];

    function CompanyController(DataUtils, Company, CompanySearch) {

        var vm = this;

        vm.companies = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Company.query(function(result) {
                vm.companies = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompanySearch.query({query: vm.searchQuery}, function(result) {
                vm.companies = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
