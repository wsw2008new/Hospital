(function() {
    'use strict';

    angular
        .module('hospitalApp')
        .controller('ExaminationsDialogController', ExaminationsDialogController);

    ExaminationsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Examinations', 'Doctor', 'Patient'];

    function ExaminationsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Examinations, Doctor, Patient) {
        var vm = this;

        vm.examinations = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.doctors = Doctor.query();
        vm.patients = Patient.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.examinations.id !== null) {
                Examinations.update(vm.examinations, onSaveSuccess, onSaveError);
            } else {
                Examinations.save(vm.examinations, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hospitalApp:examinationsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
