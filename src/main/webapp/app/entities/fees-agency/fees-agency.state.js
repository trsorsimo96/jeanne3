(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fees-agency', {
            parent: 'entity',
            url: '/fees-agency',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.feesAgency.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fees-agency/fees-agencies.html',
                    controller: 'FeesAgencyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('feesAgency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fees-agency-detail', {
            parent: 'fees-agency',
            url: '/fees-agency/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.feesAgency.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fees-agency/fees-agency-detail.html',
                    controller: 'FeesAgencyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('feesAgency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FeesAgency', function($stateParams, FeesAgency) {
                    return FeesAgency.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fees-agency',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fees-agency-detail.edit', {
            parent: 'fees-agency-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fees-agency/fees-agency-dialog.html',
                    controller: 'FeesAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FeesAgency', function(FeesAgency) {
                            return FeesAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fees-agency.new', {
            parent: 'fees-agency',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fees-agency/fees-agency-dialog.html',
                    controller: 'FeesAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fees: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fees-agency', null, { reload: 'fees-agency' });
                }, function() {
                    $state.go('fees-agency');
                });
            }]
        })
        .state('fees-agency.edit', {
            parent: 'fees-agency',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fees-agency/fees-agency-dialog.html',
                    controller: 'FeesAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FeesAgency', function(FeesAgency) {
                            return FeesAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fees-agency', null, { reload: 'fees-agency' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fees-agency.delete', {
            parent: 'fees-agency',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fees-agency/fees-agency-delete-dialog.html',
                    controller: 'FeesAgencyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FeesAgency', function(FeesAgency) {
                            return FeesAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fees-agency', null, { reload: 'fees-agency' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
