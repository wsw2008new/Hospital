(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('OtherEmployeesDialogController', OtherEmployeesDialogController);

    OtherEmployeesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OtherEmployees'];

    function OtherEmployeesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OtherEmployees) {
        var vm = this;

        vm.otherEmployees = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.otherEmployees.id !== null) {
                OtherEmployees.update(vm.otherEmployees, onSaveSuccess, onSaveError);
            } else {
                OtherEmployees.save(vm.otherEmployees, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hospitalApp:otherEmployeesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateOfBirth = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
