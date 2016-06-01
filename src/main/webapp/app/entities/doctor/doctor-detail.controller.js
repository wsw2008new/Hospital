(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('DoctorDetailController', DoctorDetailController);

    DoctorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Doctor', 'Department', 'Patient', 'Examinations'];

    function DoctorDetailController($scope, $rootScope, $stateParams, entity, Doctor, Department, Patient, Examinations) {
        var vm = this;

        vm.doctor = entity;

        var unsubscribe = $rootScope.$on('hospitalApp:doctorUpdate', function(event, result) {
            vm.doctor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
