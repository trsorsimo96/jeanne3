(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('passenger', {
            parent: 'entity',
            url: '/passenger',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.passenger.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/passenger/passengers.html',
                    controller: 'PassengerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('passenger');
                    $translatePartialLoader.addPart('typePassenger');
                    $translatePartialLoader.addPart('titlePassenger');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('passenger-detail', {
            parent: 'passenger',
            url: '/passenger/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.passenger.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/passenger/passenger-detail.html',
                    controller: 'PassengerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('passenger');
                    $translatePartialLoader.addPart('typePassenger');
                    $translatePartialLoader.addPart('titlePassenger');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Passenger', function($stateParams, Passenger) {
                    return Passenger.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'passenger',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('passenger-detail.edit', {
            parent: 'passenger-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/passenger/passenger-dialog.html',
                    controller: 'PassengerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Passenger', function(Passenger) {
                            return Passenger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('passenger.new', {
            parent: 'passenger',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/passenger/passenger-dialog.html',
                    controller: 'PassengerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                surname: null,
                                cni: null,
                                type: null,
                                title: null,
                                ticketNumber: null,
                                seatNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('passenger', null, { reload: 'passenger' });
                }, function() {
                    $state.go('passenger');
                });
            }]
        })
        .state('passenger.edit', {
            parent: 'passenger',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/passenger/passenger-dialog.html',
                    controller: 'PassengerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Passenger', function(Passenger) {
                            return Passenger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('passenger', null, { reload: 'passenger' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('passenger.delete', {
            parent: 'passenger',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/passenger/passenger-delete-dialog.html',
                    controller: 'PassengerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Passenger', function(Passenger) {
                            return Passenger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('passenger', null, { reload: 'passenger' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
