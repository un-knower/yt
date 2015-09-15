/**
 * Created by john on 15-8-25.
 */
Ext.define('yt_manager_app.store.resource.Scene', {
    extend: 'Ext.data.Store',
    alias: 'store.scene',

    model: 'yt_manager_app.model.resource.Scene',

    pageSize: 20,

    proxy: {
        type: 'rest',
        format: 'json',
        api: {
            create: 'http://localhost:8080/yt-web/rest/scenes/save',
            read: 'http://localhost:8080/yt-web/rest/scenes/loadPage',
            update: 'http://localhost:8080/yt-web/rest/scenes/save',
            destroy: 'http://localhost:8080/yt-web/rest/scenes/remove'
        },
        actionMethods: {
            create: 'POST',
            read: 'GET',
            update: 'POST',
            destroy: 'GET'
        },
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'totalCount'
        },
        writer: {
            type: 'json',
            writeAllFields: true
        }
    }
});