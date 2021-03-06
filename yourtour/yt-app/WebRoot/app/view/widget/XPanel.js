Ext.define('YourTour.view.widget.XPanel', {
	extend: 'YourTour.view.widget.XTappable',
    xtype: 'xpanel',
    
    config:{
    	tappable:false,
		xtyle:'background-color:white'
    },
    
    initialize : function(){
    	this.callParent(arguments);
    	
    	if(this.tappable){
	    	var me = this;
	    	me.element.on({
				scope : me,
				tap : function(e, t) {
					me.fireEvent('tap', me, e, t);
				}
			});
    	}
    },
    
    setTappable:function(tappable){
    	this.tappable = tappable;
    }
});

