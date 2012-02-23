var me ;
var myid ;
var access_token ;
var nominee ;
var nomineeName ; 

function getNomineeName() {
	FB.api ( '/' + nominee, function (response) {
		nomineeName = response.first_name;		
	} ) ;
} ;

function vote () {
	if ( nominee == null )
		setnominee();
	FB.ui({
          method: 'send',
          name: me + ' has voted for you ',
          link: 'http://avgjoe-ajs.appspot.com/test1.html',
          to: nominee
          }, function (response){window.location="tu.html"});
}

function setnominee(){
	var QueryString = function () {
		  // This function is anonymous, is executed immediately and 
		  // the return value is assigned to QueryString!
		  var query_string = {};
		  var query = window.location.search.substring(1);
		  var vars = query.split("&");
		  for (var i=0;i<vars.length;i++) {
		    var pair = vars[i].split("=");
		        // If first entry with this name
		    if (typeof query_string[pair[0]] === "undefined") {
		      query_string[pair[0]] = pair[1];
		        // If second entry with this name
		    } else if (typeof query_string[pair[0]] === "string") {
		      var arr = [ query_string[pair[0]], pair[1] ];
		      query_string[pair[0]] = arr;
		        // If third or later entry with this name
		    } else {
		      query_string[pair[0]].push(pair[1]);
		    }
		  } 
		    return query_string;
	} ();
	
	nominee = QueryString.nominee ;
	
}
function loginCallback (response){
	
  	if (response.authResponse) {
	   access_token = response.authResponse.accessToken;
	   myid = response.authResponse.userID ;
	   getme();
  	} 
  	else {
    	document.getElementById("hello").innerHTML = "Hello Nobody" ;
  	}
		 
}

function getme (){
	FB.api('/me', function(response) {
		//TO-DO: change this
		if ( response.error ){
			FB.login(function(response){loginCallback(response);},{scope: 'publish_stream'} );
		}
		else{
			document.getElementById("hello").innerHTML = "Hello " + response.first_name ;
			me = response.first_name ;
			myid = response.id ;
			getImage(myid,document.getElementById("myimage"));
			
			if ( nominee!=null){
				console.log("Setting the Nominee Image " + nominee ) ;
				getImage(nominee,document.getElementById("nomineeimage"));
			}
		}
	});
}

function getImage (id, img){
	FB.api('/'+id + '/picture', function(response) {
		console.log( "Picture Response "  +response);
		console.log ("Image Element " + img ) ;
		img.src = response ;
		console.log("Setting the src for Image " + response );
		return response ;
	});
}

function nominate (){
    var obj = {
            method: 'feed',
            link: 'http://avgjoe-ajs.appspot.com/test1.html?nominee=' + myid ,
            picture: 'http://fbrell.com/f8.jpg',
            name: 'Feed Test',
            caption: 'Ur friend nominated himself',
            description: 'Ur friend nominated himself'
      };

     function callback(response) {
    	 window.location = "tu.html" ;
     }

     FB.ui(obj, callback);
	
}

function populateFriends (){
	FB.api ('/me/friends' , function(response){
		friendsarray = response.data ;
		str = "" ;
		for(i=0;i<friendsarray.length;i++){
			friend = friendsarray[i] ;
			str = str + friend.name + "<BR>" ;
		}
		document.getElementById("myfriends").innerHTML = str ;
	}) ;
}