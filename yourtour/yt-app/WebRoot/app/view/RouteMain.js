Ext.define('YourTour.view.RouteMain', {
    extend: 'Ext.Panel',
    xtype: 'routemain',
    requires:['Ext.Carousel', 'Ext.TitleBar', 'Ext.Button'],
    
    config: {
    	itemId:'routemain',
    	fullscreen: true,
    	layout:'vbox',
        items: [
            {
            	xtype: 'titlebar',
                docked: 'top',
                title: '行程',
                items:[{
                	xtype: "button", 
                    ui: "normal", 
                	text:'管理',
                	align:'left'
                },
                {
                	xtype: "button", 
                    ui: "normal", 
                	text:'新建',
                	itemId:'btnRouteNew',
                	align:'right'
                }]
            },
            {
            	xtype: 'carousel',
            	itemId:'routeCarousel',
            	style:'height:100%;weight:100%',
            }
        ]
    }
});
