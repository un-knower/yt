Ext.define('YourTour.view.place.PlaceAlongItem', {
	extend: 'Ext.Container',
    xtype: 'PlaceAlongItem',
    requires:['YourTour.view.widget.grid.XGridView','YourTour.view.widget.grid.XDataView','YourTour.view.place.PlaceExpertGridItem'],
    config: {
        layout:'vbox',
      	items:[
            {
                xtype: 'xspacer'
            },
            {
                xtype: 'xlabel',
                itemId:'placeMoreAlongs',
                cls: 'underline x-xlabel-normal',
                indicator:'nav-arrow',
                html: '一起旅游'
            },

            {
                xtype: 'xdataview',
                itemId: 'placeAlongList',
                defaultType: 'RouteScheduleListDataItem'
            }
        ]
    }
});
