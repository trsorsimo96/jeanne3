(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('month', {
            parent: 'entity',
            url: '/month',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.month.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/month/months.html',
                    controller: 'MonthController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('month');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('month-detail', {
            parent: 'month',
            url: '/month/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.month.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/month/month-detail.html',
                    controller: 'MonthDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('month');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Month', function($stateParams, Month) {
                    return Month.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'month',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('month-detail.edit', {
            parent: 'month-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/month/month-dialog.html',
                    controller: 'MonthDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Month', function(Month) {
                            return Month.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('month.new', {
            parent: 'month',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/month/month-dialog.html',
                    controller: 'MonthDialogController',
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
                    $state.go('month', null, { reload: 'month' });
                }, function() {
                    $state.go('month');
                });
            }]
        })
        .state('month.edit', {
            parent: 'month',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/month/month-dialog.html',
                    controller: 'MonthDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Month', function(Month) {
                            return Month.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('month', null, { reload: 'month' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('month.delete', {
            parent: 'month',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/month/month-delete-dialog.html',
                    controller: 'MonthDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Month', function(Month) {
                            return Month.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('month', null, { reload: 'month' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
