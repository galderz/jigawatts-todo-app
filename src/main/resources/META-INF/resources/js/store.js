/*jshint unused:false */

(function (exports) {

    'use strict';

    var serverUrl = 'api/';

    exports.todoStorage = {
        fetch: async function () {
            const response = await axios.get(serverUrl);
            console.log(response.data);
            return response.data;
        },
        add : async function(item) {
          console.log("Adding todo item " + item.title);
          return (await axios.post(serverUrl, item)).data;
        },
        save: async function (item) {
            console.log("save called with", item);
            await axios.patch(serverUrl + item.id, item);
        },
        delete: async function(item) {
            console.log("delete called with", item);
            await axios.delete(serverUrl + item.id);
        },
        deleteCompleted: async function() {
            await axios.delete(serverUrl);
        },
        checkpoint: async function() {
            await axios.post(serverUrl + "checkpoint");
        }
    };

})(window);
