Ext.define('YourTour.model.RouteMain', {
    extend: 'Ext.data.Model',
    xtype:'routemain',
    config:{
	    fields:[{name:'id', type:'string'},
	            {name:'imgUrl', type:'string'},
	    		{name:'name', type:'string'},
	    		{name:'lineName', type:'string'},
	    		{name:'feeling', type:'string'}
	    ]
    },
});
