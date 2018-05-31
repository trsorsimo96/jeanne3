(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('booking', {
            parent: 'entity',
            url: '/booking',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.booking.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking/bookings.html',
                    controller: 'BookingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('booking');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('booking-detail', {
            parent: 'booking',
            url: '/booking/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.booking.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking/booking-detail.html',
                    controller: 'BookingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('booking');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Booking', function($stateParams, Booking) {
                    return Booking.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'booking',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('booking-detail.edit', {
            parent: 'booking-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-dialog.html',
                    controller: 'BookingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Booking', function(Booking) {
                            return Booking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('booking.new', {
            parent: 'booking',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-dialog.html',
                    controller: 'BookingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                pnr: null,
                                bookingDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: 'booking' });
                }, function() {
                    $state.go('booking');
                });
            }]
        })
        .state('booking.edit', {
            parent: 'booking',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-dialog.html',
                    controller: 'BookingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Booking', function(Booking) {
                            return Booking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: 'booking' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('booking.delete', {
            parent: 'booking',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-delete-dialog.html',
                    controller: 'BookingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Booking', function(Booking) {
                            return Booking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: 'booking' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
