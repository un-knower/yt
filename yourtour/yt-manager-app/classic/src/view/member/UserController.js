/**
 * Created by john on 15-8-21.
 */
Ext.define('yt_manager_app.view.member.UserController', {
    extend: 'yt_manager_app.view.widget.GeneralCypherController',
    alias: 'controller.user',

    config: {
        popupWindowName: 'userPopupWindow',
        formWindowName: 'formWindow',
        name: '用户'
    },

    init: function() {
        var me = this,
            grid = me.lookupReference('user_crud_grid_paging');
        this.config.currentGrid = grid;
        var store = grid.getStore();
        store.load();
    },

    showPopWindow: function (title, data, editable) {
        var win = this.lookupReference(this.config.popupWindowName);
        if (!win) {
            win = new yt_manager_app.view.member.UserWindow();
            this.getView().add(win);
        }
        win.setTitle(title);

        var me = this,
            formWindow = me.lookupReference(this.config.formWindowName),
            baseInfo = me.lookupReference('baseInfo'),
            detailInfo = me.lookupReference('detailInfo'),
            extendInfo = me.lookupReference('extendInfo'),
            operateInfo = me.lookupReference('operatedInfo'),
            reset = me.lookupReference('reset'),
            save = me.lookupReference('save');
        // fill the data
        if (data) {
            formWindow.loadRecord(data);
        }

        // set editable
        var disabled = !editable;
        baseInfo.setDisabled(disabled);
        detailInfo.setDisabled(disabled);
        extendInfo.setDisabled(disabled);
        operateInfo.setDisabled(disabled);
        if (disabled) {
            detailInfo.expand();
            extendInfo.expand();
            operateInfo.expand();
            reset.hide();
            save.hide();
        } else {
            detailInfo.collapse();
            extendInfo.collapse();
            operateInfo.collapse();
            reset.show();
            save.show();
            baseInfo.focus();
        }

        // show
        win.show();
    },

    onTabChange: function (tabs, newTab, oldTab) {
        var me = this;
        var id = newTab.id;
        if (id == 'user-crud-tab') {
            // all member tab
            this.config.currentGrid = me.lookupReference('user_crud_grid_paging');
        } else if (id == 'user-cypher-tab') {
            // cyphere tab
            this.config.currentGrid = me.lookupReference('user_cypher_grid_paging');
        } else {
            // graph tab
            // TODO 图关系
        }
    }
});