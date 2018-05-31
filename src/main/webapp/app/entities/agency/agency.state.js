(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agency', {
            parent: 'entity',
            url: '/agency',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.agency.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agency/agencies.html',
                    controller: 'AgencyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agency-detail', {
            parent: 'agency',
            url: '/agency/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.agency.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agency/agency-detail.html',
                    controller: 'AgencyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Agency', function($stateParams, Agency) {
                    return Agency.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agency',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agency-detail.edit', {
            parent: 'agency-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agency/agency-dialog.html',
                    controller: 'AgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Agency', function(Agency) {
                            return Agency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agency.new', {
            parent: 'agency',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agency/agency-dialog.html',
                    controller: 'AgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                pcc: null,
                                name: null,
                                tel: null,
                                email: null,
                                omNumber: null,
                                momoNumber: null,
                                solde: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('agency', null, { reload: 'agency' });
                }, function() {
                    $state.go('agency');
                });
            }]
        })
        .state('agency.edit', {
            parent: 'agency',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agency/agency-dialog.html',
                    controller: 'AgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Agency', function(Agency) {
                            return Agency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agency', null, { reload: 'agency' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agency.delete', {
            parent: 'agency',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agency/agency-delete-dialog.html',
                    controller: 'AgencyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Agency', function(Agency) {
                            return Agency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agency', null, { reload: 'agency' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
