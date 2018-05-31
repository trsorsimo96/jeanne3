(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('deposit', {
            parent: 'entity',
            url: '/deposit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.deposit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deposit/deposits.html',
                    controller: 'DepositController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deposit');
                    $translatePartialLoader.addPart('modePayment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('deposit-detail', {
            parent: 'deposit',
            url: '/deposit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.deposit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deposit/deposit-detail.html',
                    controller: 'DepositDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deposit');
                    $translatePartialLoader.addPart('modePayment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Deposit', function($stateParams, Deposit) {
                    return Deposit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'deposit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('deposit-detail.edit', {
            parent: 'deposit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deposit/deposit-dialog.html',
                    controller: 'DepositDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deposit', function(Deposit) {
                            return Deposit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deposit.new', {
            parent: 'deposit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deposit/deposit-dialog.html',
                    controller: 'DepositDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                date: null,
                                modePayment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('deposit', null, { reload: 'deposit' });
                }, function() {
                    $state.go('deposit');
                });
            }]
        })
        .state('deposit.edit', {
            parent: 'deposit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deposit/deposit-dialog.html',
                    controller: 'DepositDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deposit', function(Deposit) {
                            return Deposit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deposit', null, { reload: 'deposit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deposit.delete', {
            parent: 'deposit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deposit/deposit-delete-dialog.html',
                    controller: 'DepositDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Deposit', function(Deposit) {
                            return Deposit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deposit', null, { reload: 'deposit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
