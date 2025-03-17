function heade_ajax(){
      $.ajax({
          url　: 'header.html',
          dataType : 'html',
          success　: function(data){
              $('#header2').html(data);
          },
          error: function(data){
              $('#header2').html(data);
          }
      });
    }

async function header(title, user) {
  const url = "header.html";
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`レスポンスステータス: ${response.status}`);
    }
	let html = await response.text(); 
  	//$("#header").html(html);
  	document.getElementById("header").innerHTML = html;
    let element_title = document.getElementById("title");	
	element_title.textContent = title;
    let element_user = document.getElementById("user");	
	element_user.textContent = user;
  } catch (error) {
    console.error(error.message);
  }
}

async function logout() {
  const url = "logout.html";
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`レスポンスステータス: ${response.status}`);
    }
	let html = await response.text(); 
  	//$("#footer").html(html);
  	document.getElementById("logout").innerHTML = html;
  } catch (error) {
    console.error(error.message);
  }
}

async function footer() {
  const url = "footer.html";
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`レスポンスステータス: ${response.status}`);
    }
	let html = await response.text(); 
  	//$("#footer").html(html);
  	document.getElementById("footer").innerHTML = html;
  } catch (error) {
    console.error(error.message);
  }
}

function logoutclick_main() {
	let result = confirm('ログアウトしますか？');
	if (result){
		console.log("ログ開始");

		//var csrfToken = document.getElementById("csrf");
		var csrfToken = document.querySelector("#csrf input").value;
		console.log(csrfToken.innerHTML);

		console.log("ログ終了");

		
    	//console.log(document.getElementById('_csrf').value);
		console.log(/*[[${_csrf.token}]]*/);
		console.log("${_csrf.token}");
		console.log('[[${_csrf.token}]]');
		//console.log(document.querySelector("#csrf").value);
		

    	let form = document.createElement("FORM");
    	form.method="post";
    	form.action="/logout";
		// CSRF-Token
    	let input = document.createElement("input");
    	input.setAttribute("type", "hidden");
    	input.setAttribute("name", "${_csrf.parameterName}");
    	input.setAttribute("value", "${_csrf.token}"); 
    	
    	//input.setAttribute("id", "csrf");
    	form.appendChild(input);

    	document.body.appendChild(form);
    	//form.submit();
    	
    	
    	/*
    	let form = document.getElementById("logout");
    	form.submit();
    	*/		    			
	}
}



