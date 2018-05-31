(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('SegmentController', SegmentController);

    SegmentController.$inject = ['Segment', 'SegmentSearch'];

    function SegmentController(Segment, SegmentSearch) {

        var vm = this;

        vm.segments = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Segment.query(function(result) {
                vm.segments = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SegmentSearch.query({query: vm.searchQuery}, function(result) {
                vm.segments = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
