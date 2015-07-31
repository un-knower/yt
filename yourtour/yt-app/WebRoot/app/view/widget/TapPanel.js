Ext.define('YourTour.view.widget.TapLabel', {
    extend: 'Ext.Label',
    xtype: 'TapLabel',
    
    initialize : function(){
    	this.callParent(arguments);
    	
    	var me = this;
    	me.element.on({
			scope : me,
			tap : function(e, t) {
				me.fireEvent('tap', me, e, t);
			}
		});
    }
});
