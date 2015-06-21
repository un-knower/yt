/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when it performs code generation tasks such as generating new
    models, controllers or views and when running "sencha app upgrade".

    Ideally changes to this file would be limited and most work would be done
    in other places (such as Controllers). If Sencha Cmd cannot merge your
    changes and its generated code, it will produce a "merge conflict" that you
    will need to resolve manually.
*/

Ext.application({
    name: 'YourTour',

    requires: [
        'Ext.MessageBox'
    ],

    views: [
        'Main','Launch','Welcome','RouteMain','HomeMain','SearchMain','RouteNew'
    ],
    
    controllers: [
        'Welcome', 'Main', 'RouteMain','RouteNew'
    ],
    
    models:[
        'RouteMain'
    ],
    
    stores:[
        'RouteMain'
    ],
        
    icon: {
        '57': 'resources/icons/Icon.png',
        '72': 'resources/icons/Icon~ipad.png',
        '114': 'resources/icons/Icon@2x.png',
        '144': 'resources/icons/Icon~ipad@2x.png'
    },
    
    isIconPrecomposed: true,

    startupImage: {
        '320x460': 'resources/startup/launch.jpg',
        '640x920': 'resources/startup/launch.png',
        '768x1004': 'resources/startup/launch.png',
        '748x1024': 'resources/startup/launch.png',
        '1536x2008': 'resources/startup/launch.png',
        '1496x2048': 'resources/startup/launch.png'
    },

    launch: function() {
        Ext.fly('appLoadingIndicator').destroy();
        
        // Initialize the main view
        Ext.Viewport.add(Ext.create('YourTour.view.Launch'));
        
        Ext.defer(function () { 
        	Ext.Viewport.setActiveItem(Ext.create('YourTour.view.Main'));
        },2000);
    },

    onUpdated: function() {
        Ext.Msg.confirm(
            "Application Update",
            "This application has just successfully been updated to the latest version. Reload now?",
            function(buttonId) {
                if (buttonId === 'yes') {
                    window.location.reload();
                }
            }
        );
    }
});