Ext.define('YourTour.view.route.RouteRecommendIntroductionView', {
    extend: 'YourTour.view.widget.XPage',
    requires:['Ext.Panel','Ext.Img','Ext.DataView','YourTour.view.route.RouteRecommendScheduleItem', 'YourTour.view.route.RouteRecommendIntroductionItem','YourTour.view.expert.ExpertRecommendIntroItem','YourTour.view.widget.XCarousel','YourTour.view.widget.XPanel', 'YourTour.view.widget.XHeaderBar'],
    config: {
    	id:'RouteRecommendIntroductionView',
      	layout:'vbox',
        items: [
        	{    
				xtype: 'xheaderbar',
				title:'行程介绍'
			},

			{
				xtype: 'xcarousel',
				itemId:'recommendCarousel',
				flex:1,
				items:[

				]
			}
        ]
    }
});
