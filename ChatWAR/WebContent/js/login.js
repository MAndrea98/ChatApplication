$(document).ready(function(){
    $('.login-info-box').fadeOut();
    $('.login-show').addClass('show-log-panel');
    $('.login-reg-panel input[type="radio"]').on('change', function() {
	    if($('#log-login-show').is(':checked')) {
	        $('.register-info-box').fadeOut(); 
	        $('.login-info-box').fadeIn();
	        
	        $('.white-panel').addClass('right-log');
	        $('.register-show').addClass('show-log-panel');
	        $('.login-show').removeClass('show-log-panel');
	        
	    }
	    else if($('#log-reg-show').is(':checked')) {
	        $('.register-info-box').fadeIn();
	        $('.login-info-box').fadeOut();
	        
	        $('.white-panel').removeClass('right-log');
	        
	        $('.login-show').addClass('show-log-panel');
	        $('.register-show').removeClass('show-log-panel');
	    }
    });
    start();
});

function login() {
	let username = $('#username').val();
	let password = $('#password').val();
	$.ajax({
		url: "rest/chat/login",
		type: "POST",
		data: JSON.stringify({username, password}),
		contentType: "application/json",
		success: function(user) {
			if (user === undefined) 
				alert('Error');
			else {
				localStorage.setItem('user', JSON.stringify(user));
				window.location = './profile.html';
			}
		}
	});
}


function register() {
	let username = $('#new_username').val();
	let password = $('#new_password').val();
	let confirm = $('#new_confirm').val();
	$.ajax({
		url: "rest/chat/register/" + confirm,
		type: "POST",
		data: JSON.stringify({username, password}),
		contentType: "application/json",
		success: function(data) {
			if (data === undefined)
				alert('Error');
			else 
				alert('Successfully registered!!!');
		}
	});
}

function start() {
	$.ajax({
		url: "rest/start",
		type: "GET",
		success: function(data) {
			console.log(data);
		},
		error: function() {
			alert('Error');
		}
	});
}



