(function() {
    'use strict';
    angular
        .module('hospitalApp')
        .factory('Patient', Patient);

    Patient.$inject = ['$resource', 'DateUtils'];

    function Patient ($resource, DateUtils) {
        var resourceUrl =  'api/patients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOfBirth = DateUtils.convertLocalDateFromServer(data.dateOfBirth);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateOfBirth = DateUtils.convertLocalDateToServer(data.dateOfBirth);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateOfBirth = DateUtils.convertLocalDateToServer(data.dateOfBirth);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
