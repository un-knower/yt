/**
 * Created by john on 15-8-24.
 */
Ext.define('yt_manager_app.view.route.LineWindow', {
    extend: 'yt_manager_app.view.widget.GeneralFormWindow',
    xtype: 'lineWindow',
    reference: 'linePopupWindow',

    config: {
        base: {
            items: [{
                fieldLabel: '代码',
                reference: 'code',
                allowBlank: false,
                name: 'code',
                emptyText: '线路唯一代码'
            }, {
                fieldLabel: '名称',
                reference: 'name',
                allowBlank: false,
                name: 'name',
                emptyText: '线路名称'
            }, {
                xtype: 'fieldcontainer',
                fieldLabel: '目的地',
                reference: 'placeParent',
                layout: 'hbox',
                items: [{
                    xtype: 'textfield',
                    reference: 'place',
                    allowBlank: false,
                    editable: false,
                    margin: '0 0 0 0',
                    name: 'place',
                    flex: 1,
                    emptyText: '线路目的地',
                    listeners: {
                        change: 'onPlaceChanged'
                    }
                }, {
                    xtype: 'button',
                    text: '选择',
                    margin: '0 0 0 0',
                    handler: 'onPopupDivisionSelectWindow'
                }]
            }],
            hidden: false
        },
        detail: {
            items: [{
                fieldLabel: '照片',
                reference: 'imageUrl',
                name: 'imageUrl',
                emptyText: 'Image URL'
            }, {
                xtype: 'numberfield',
                minValue: 0,
                value: 0,
                fieldLabel: '人数',
                reference: 'arriveNum',
                name: 'arriveNum',
                emptyText: '到达人数'
            }, {
                xtype: 'tagfield',
                name: 'tags',
                fieldLabel: '分类',
                store: {
                    data: [{
                        abbr: 'QZY', tag: '亲子游'
                    }, {
                        abbr: 'QLY', tag: '情侣游'
                    }, {
                        abbr: 'JTY', tag: '家庭游'
                    }, {
                        abbr: 'ZRSS', tag: '自然山水'
                    }, {
                        abbr: 'LSRW', tag: '历史人文'
                    }, {
                        abbr: 'MS', tag: '美食'
                    }, {
                        abbr: 'GW', tag: '购物'
                    }]
                },
                reference: 'tags',
                displayField: 'tag',
                valueField: 'abbr',
                filterPickList: true,
                queryMode: 'local',
                publishes: 'value'
            }, {
                xtype: 'tagfield',
                name: 'scenes',
                fieldLabel: '景点',
                store: {
                    data: null
                },
                reference: 'scenes',
                displayField: 'name',
                valueField: 'abbr',
                filterPickList: true,
                queryMode: 'local',
                publishes: 'value'
            }, {
                xtype: 'tagfield',
                name: 'hotels',
                fieldLabel: '宾馆',
                store: {
                    data: null
                },
                reference: 'hotels',
                displayField: 'name',
                valueField: 'abbr',
                filterPickList: true,
                queryMode: 'local',
                publishes: 'value'
            }, {
                xtype: 'tagfield',
                name: 'restaurants',
                fieldLabel: '饭店',
                store: {
                    data: null
                },
                reference: 'restaurants',
                displayField: 'name',
                valueField: 'abbr',
                filterPickList: true,
                queryMode: 'local',
                publishes: 'value'
            }, {
                xtype: 'combobox',
                store: {
                    data: [{
                        abbr: 'VALIDATED', name: '有效'
                    }, {
                        abbr: 'ACTIVED', name: '激活'
                    }, {
                        abbr: 'FROZEN', name: '冻结'
                    }, {
                        abbr: 'CLOSED', name: '关闭'
                    }]
                },
                displayField: 'name',
                valueField: 'abbr',
                queryMode: 'local',
                editable: false,
                fieldLabel: '状态',
                reference: 'status',
                name: 'status',
                emptyText: '状态'
            }],
            hidden: false
        },
        extend: {
            items: [{
                fieldLabel: '简介',
                reference: 'intro',
                name: 'intro',
                emptyText: '行程简介'
            }, {
                fieldLabel: '特点',
                reference: 'feature',
                name: 'feature',
                emptyText: '行程特质'
            }, {
                fieldLabel: '推荐理由',
                reference: 'reason',
                name: 'reason',
                emptyText: '行程推荐理由'
            }],
            hidden: false
        },
        operate: {
            hidden: false
        }
    }
});