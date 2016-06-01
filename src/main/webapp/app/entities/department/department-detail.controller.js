(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('DepartmentDetailController', DepartmentDetailController);

    DepartmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Department', 'Doctor'];

    function DepartmentDetailController($scope, $rootScope, $stateParams, entity, Department, Doctor) {
        var vm = this;

        vm.department = entity;

        var unsubscribe = $rootScope.$on('hospitalApp:departmentUpdate', function(event, result) {
            vm.department = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
