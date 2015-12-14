Ext.define('YourTour.view.common.MessageMainView', {
	extend: 'YourTour.view.widget.XPage',
	requires:['Ext.Panel','YourTour.view.common.MessageDataItemView'],
	xtype:'MessageMainView',
    config: {
    	id:'MessageMainView',
    	layout:'card',
    	items:[
			{    
				xtype: 'xheaderbar',
				itemId:'headerbar',
				title:'目的地',
				items:[
			       {
			    	   xtype:'toolbutton',
			    	   itemId:'group',
			    	   text:'设置',
			    	   align:'right'
			       }
				]	
			},
			
			{
    			itemId:'MessageList',
    			scrollable:null,
    			style:'background-color:#F2F2F2',
    			flex:1,
		        useComponents: true,
		        defaultType: 'MessageDataItemView'
    		},
			
			{    
				xtype: 'panel',
				itemId:'messagetool',
				height:50,
				layout:'hbox',
				docked:'bottom',
				items:[
					{  
						xtype : 'image',
						mode : 'tag',
						src:'resources/icons/icon_voice.png',
					  	itemId:'voice'  
					},
					
					{
						xtype:'label',
						flex:1
					},
					
					{  
						xtype : 'image',
	    				mode : 'tag',
	    				src:'resources/icons/icon_add_48.png',
		              	itemId:'btnAdd'  
					},
				]	
			} 
    	]
    }
});