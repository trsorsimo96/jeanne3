(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('segment', {
            parent: 'entity',
            url: '/segment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.segment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/segment/segments.html',
                    controller: 'SegmentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('segment');
                    $translatePartialLoader.addPart('statusSegment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('segment-detail', {
            parent: 'segment',
            url: '/segment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jeanne3App.segment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/segment/segment-detail.html',
                    controller: 'SegmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('segment');
                    $translatePartialLoader.addPart('statusSegment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Segment', function($stateParams, Segment) {
                    return Segment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'segment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('segment-detail.edit', {
            parent: 'segment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/segment/segment-dialog.html',
                    controller: 'SegmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Segment', function(Segment) {
                            return Segment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('segment.new', {
            parent: 'segment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/segment/segment-dialog.html',
                    controller: 'SegmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('segment', null, { reload: 'segment' });
                }, function() {
                    $state.go('segment');
                });
            }]
        })
        .state('segment.edit', {
            parent: 'segment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/segment/segment-dialog.html',
                    controller: 'SegmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Segment', function(Segment) {
                            return Segment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('segment', null, { reload: 'segment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('segment.delete', {
            parent: 'segment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/segment/segment-delete-dialog.html',
                    controller: 'SegmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Segment', function(Segment) {
                            return Segment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('segment', null, { reload: 'segment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
