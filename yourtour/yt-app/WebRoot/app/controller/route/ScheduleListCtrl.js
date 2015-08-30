Ext.define('YourTour.controller.route.ScheduleListCtrl', {
    extend: 'YourTour.controller.BaseCtrl',
    requires:['YourTour.store.RouteStore', 'YourTour.model.RouteModel','YourTour.view.route.schedule.ScheduleListItem'],
    
    config: {
       refs: {
       		page:'#ScheduleListView',
       		sceneSchedulePage:'SceneScheduleView',
       		foodSchedulePage:'FoodScheduleView',
       		hotelSchedulePage:'HotelScheduleView',
       		
       		scheduleList:'#ScheduleListView #scheduleList'
       },
       
       control:{
    	   newRoute:{
    		   tap:'newRoute'
    	   },
    	   
    	   scheduleList:{
    	   	   itemtap:'onItemTap'
    	   },
    	   
    	   '#ScheduleListView #toolbar':{
    	   	   tap:'onBackTap'
    	   },
    	   
    	   '#FoodScheduleView #toolbar':{
    	   	   tap:'onBackTap'
    	   },
    	   
    	   '#HotelScheduleView #toolbar':{
    	   	   tap:'onBackTap'
    	   },
    	   
    	   '#SceneScheduleView #toolbar':{
    	   	   tap:'onBackTap'
    	   }
       },
       
       routes:{
        	'/route/schedule/:routeId':'showPage'
       },
       
       store:null
    },
    
    init:function(){
		this.store = Ext.create('YourTour.store.RouteStore', {storeId:'ScheduleListStore'});	    	
    },
    
    onBackTap:function(){
    	this.show('LineRecommendView','YourTour.view.line.LineRecommendView');
    },
    
    onEdit:function(dataview, record){
		var type = record.get('type');
		if(type == 'prepare'){
			this.redirectTo('/schedule/prepar');
		}else{
			this.redirectTo('/schedule/resource/selection');
		}
    },
    
    onItemTap:function(dataview, index, item, record,e){
    	if(record.get('type') == 'scene'){
    		this.redirectTo('/route/schedule/scene/' + index);
    	}else if(record.get('type') == 'food'){
    		this.redirectTo('/route/schedule/food/' + index);
    	}else if(record.get('type') == 'hotel'){
    		this.redirectTo('/route/schedule/hotel/' + index);
    	}
    },
    
    showPage:function(routeId){
    	this.show('ScheduleListView','YourTour.view.route.schedule.ScheduleListView');
		
    	var store = this.store, page=this.getPage();
    	var showView=function(){
    		var record = store.getAt(0);
	    	var imageUrlEl = page.down('#imageUrl');
	    	imageUrlEl.setHtml("<img src='" + record.get('imageUrl') + "' style='width:100%; max-height:150px'>");
    	
	    	var nameEl = page.down('#name');
	    	nameEl.setHtml(record.get('name'));
	    	
	    	var scheduleList = page.down('#scheduleList');
	 	   	var schedules = [];
	 	   	record.schedules().each(function(schedule){
	 	   		schedules.push(schedule.data);
	 	   	});
	 	   	
	 	   	var scheduleStore = Ext.create('Ext.data.Store',{model:'YourTour.model.ScheduleModel', data:schedules});
	 	   	scheduleList.setStore(scheduleStore);
    	};
 	   	
 	   	store.load(showView,this);
    }
});