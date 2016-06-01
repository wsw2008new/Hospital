(function() {
    'use strict';
    angular
        .module('hospitalApp')
        .factory('Doctor', Doctor);

    Doctor.$inject = ['$resource', 'DateUtils'];

    function Doctor ($resource, DateUtils) {
        var resourceUrl =  'api/doctors/:id';

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
