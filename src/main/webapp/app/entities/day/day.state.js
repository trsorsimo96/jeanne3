(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('day', {
            parent: 'entity',
            url: '/day',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.day.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/day/days.html',
                    controller: 'DayController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('day');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('day-detail', {
            parent: 'day',
            url: '/day/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.day.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/day/day-detail.html',
                    controller: 'DayDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('day');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Day', function($stateParams, Day) {
                    return Day.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'day',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('day-detail.edit', {
            parent: 'day-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day/day-dialog.html',
                    controller: 'DayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Day', function(Day) {
                            return Day.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('day.new', {
            parent: 'day',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day/day-dialog.html',
                    controller: 'DayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                text: null,
                                number: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('day', null, { reload: 'day' });
                }, function() {
                    $state.go('day');
                });
            }]
        })
        .state('day.edit', {
            parent: 'day',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day/day-dialog.html',
                    controller: 'DayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Day', function(Day) {
                            return Day.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('day', null, { reload: 'day' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('day.delete', {
            parent: 'day',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day/day-delete-dialog.html',
                    controller: 'DayDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Day', function(Day) {
                            return Day.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('day', null, { reload: 'day' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
