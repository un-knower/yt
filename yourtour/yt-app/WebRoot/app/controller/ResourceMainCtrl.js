Ext.define('YourTour.controller.ResourceMainCtrl', {
    extend: 'YourTour.controller.BaseCtrl',
    config: {
       refs:{
    	   	resourceSelectionView:'#ResourceSelectionView',
    	   	resourceCategory:'#ResourceSelectionView #resourceCategory',
    	   	
    	   	resourcePlayListView:'#ResourceSelectionView #ResourcePlayListView #ResourceList',
    	   	resourceFoodListView:'#ResourceSelectionView #ResourceFoodListView #ResourceList',
    	   	
    	   	resourceDetailView:'#ResourceDetailView',
    	   	btnAddResource:'#ResourceDetailView #btnAdd'
       },
       
       control:{
    	   resourceCategory:{
     		   activeitemchange:'onActiveItemChange'
     	   },
     	   
     	  resourcePlayListView:{
     		  itemtap : 'onPlayItemTap'
     	  },
     	  
     	 btnAddResource:{
     		 tap:'onAddTap'
     	 }
       },
       
       routes:{
        	'/resource/selection':'showPage',
        	'/resource/:resourceType/:resourceId':'showResourcePage',
       },
       
       playStore:null,
       foodStore:null,
       
       record : null,
    },
    
    init: function(){
    },
    
    showPage:function(){
    	Ext.ComponentManager.get('MainView').push(Ext.create('YourTour.view.resource.ResourceSelectionView'));
    	
    	if(this.playStore != null){
    		this.playStore.setData('');
    		this.playStore.loaded = false;
    	}
    	
    	if(this.foodStore != null){
    		this.foodStore.setData('');
    		this.foodStore.loaded = false;
    	}
    	
    	this.getResourceCategory().fireEvent('activeitemchange',this.getResourceCategory(), this.getResourceCategory().getAt(0),this.getResourceCategory().getAt(0));
    },
    
    showResourcePage:function(resourceType, resourceId){
    	Ext.ComponentManager.get('MainView').push(Ext.create('YourTour.view.resource.ResourceDetailView'));
		
		var resourceDetailView = this.getResourceDetailView();
		var panel = resourceDetailView.down('#detailview');
		panel.removeAll(true,false);
		
		var toolbar = resourceDetailView.down('#toolbar');
		toolbar.getAt(1).hide();
		
		if(resourceType == 'SCENE'){
    		this.loadSceneResource(resourceId, panel);
    	}
    },
    
    onActiveItemChange:function(tabBar, newTab, oldTab){
    	if(newTab.getItemId() == 'ResourcePlayListView'){
    		this.loadPlayResource(newTab);
    	}else if(newTab.getItemId() == 'ResourceFoodListView'){
    		this.loadFoodResource(newTab);
    	}
    },
    
    /**
     * 查询游玩资源
     */
    loadPlayResource:function(view){
    	var me = this;
    	var resourceList = view.down('#ResourceList');
    	
    	var store = resourceList.getStore();
    	if(store == null){
    		me.playStore = Ext.create('YourTour.store.ResourcePlayStore');
    		store = me.playStore;
		}
		
		if(! store.loaded){
			var proxy = store.getProxy();
			proxy.setUrl(YourTour.util.Context.getContext('/scenes/query'));
			proxy.extraParams={placeId:'1111', name:'九寨沟'};
			store.load(function(){
				resourceList.setStore(store);
			});
		}
    },
    
    /**
     * 获取餐饮资源
     */
    loadFoodResource:function(view){
    	var me = this;
    	var resourceList = view.down('#ResourceList');
    	
    	var store = resourceList.getStore();
    	if(store == null){
    		me.foodStore = Ext.create('YourTour.store.ResourceFoodStore');
    		store = me.foodStore;
		}
		
		if(! store.loaded){
			var proxy = store.getProxy();
			proxy.setUrl(YourTour.util.Context.getContext('/restaurants/query'));
			proxy.extraParams={placeId:'1111', name:'九寨沟'};
			store.load(function(){
				resourceList.setStore(store);
			});
		}
    },
    
    onAddTap:function(){
    	var planController = this.getApplication().getController('route.RouteSchedulePlanCtrl');
    	planController.addScheduleActivity(this.record);
    },
    
	onPlayItemTap:function(dataview, index, item, record,e){
		this.record = record;
		
		Ext.ComponentManager.get('MainView').push(Ext.create('YourTour.view.resource.ResourceDetailView'));
		
		var resourceDetailView = this.getResourceDetailView();
		var panel = resourceDetailView.down('#detailview');
		panel.removeAll(true,false);
		
		var playView = Ext.create('YourTour.view.resource.ResourceSceneView',{record:record});
		panel.add(playView);
    },
    
    loadSceneResource:function(resourceId, panel){
    	var resourceDetailView = this.getResourceDetailView();
    	
    	var store = Ext.create('YourTour.store.AjaxStore', {model: 'YourTour.model.ResourceModel'});
    	store.getProxy().setUrl(YourTour.util.Context.getContext('/scenes/' + resourceId));
    	store.load(function(){
    		var resource = store.first();
    		
    		var headerbar = resourceDetailView.down('#headerbar');
    		headerbar.setTitle(resource.get('name'));
    		
    		var playView = Ext.create('YourTour.view.resource.ResourceSceneView',{record:resource});
    		panel.add(playView);
    	});
    }
});