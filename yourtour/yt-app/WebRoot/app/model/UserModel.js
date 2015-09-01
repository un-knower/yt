Ext.define('YourTour.model.UserModel', {
    extend: 'Ext.data.Model',
    config:{
    	idProperty:'rowKey',
    	
	    fields:[
            {name:'rowKey', type:'string'},
            {name:'imageUrl', type:'string'},
    		{name:'nickname', type:'string'},
    		{name:'rank', type:'string'},
    		{name:'slogan', type:'string'},
    		{name:'sex', type:'string'},
    		{name:'role', type:'string'}
	    ]
    }
});
