(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('other-employees', {
            parent: 'entity',
            url: '/other-employees?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OtherEmployees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/other-employees/other-employees.html',
                    controller: 'OtherEmployeesController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('other-employees-detail', {
            parent: 'entity',
            url: '/other-employees/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OtherEmployees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/other-employees/other-employees-detail.html',
                    controller: 'OtherEmployeesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OtherEmployees', function($stateParams, OtherEmployees) {
                    return OtherEmployees.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('other-employees.new', {
            parent: 'other-employees',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/other-employees/other-employees-dialog.html',
                    controller: 'OtherEmployeesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fname: null,
                                lname: null,
                                dateOfBirth: null,
                                email: null,
                                phoneNumber: null,
                                position: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('other-employees', null, { reload: true });
                }, function() {
                    $state.go('other-employees');
                });
            }]
        })
        .state('other-employees.edit', {
            parent: 'other-employees',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/other-employees/other-employees-dialog.html',
                    controller: 'OtherEmployeesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OtherEmployees', function(OtherEmployees) {
                            return OtherEmployees.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('other-employees', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('other-employees.delete', {
            parent: 'other-employees',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/other-employees/other-employees-delete-dialog.html',
                    controller: 'OtherEmployeesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OtherEmployees', function(OtherEmployees) {
                            return OtherEmployees.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('other-employees', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
