(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('email', {
            parent: 'entity',
            url: '/email',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.email.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/email/emails.html',
                    controller: 'EmailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('email');
                    $translatePartialLoader.addPart('typeEmail');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('email-detail', {
            parent: 'email',
            url: '/email/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.email.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/email/email-detail.html',
                    controller: 'EmailDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('email');
                    $translatePartialLoader.addPart('typeEmail');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Email', function($stateParams, Email) {
                    return Email.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'email',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('email-detail.edit', {
            parent: 'email-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email/email-dialog.html',
                    controller: 'EmailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Email', function(Email) {
                            return Email.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('email.new', {
            parent: 'email',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email/email-dialog.html',
                    controller: 'EmailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                email: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('email', null, { reload: 'email' });
                }, function() {
                    $state.go('email');
                });
            }]
        })
        .state('email.edit', {
            parent: 'email',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email/email-dialog.html',
                    controller: 'EmailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Email', function(Email) {
                            return Email.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('email', null, { reload: 'email' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('email.delete', {
            parent: 'email',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email/email-delete-dialog.html',
                    controller: 'EmailDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Email', function(Email) {
                            return Email.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('email', null, { reload: 'email' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
