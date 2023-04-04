

  function validate(event) {
    event.preventDefault();
    var webname= document.getElementById("wname").value;
    var password = document.getElementById("pass").value;

    var nameRegex = /^(https?:\/\/)?[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;
    var passRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

    if (wname == "") {
      alert("Please enter your website name.");
    } else if (!nameRegex.test(webname)) {
      alert("Please enter your website name in a valid format.");
    } else if (pass == "") {
      alert("Please enter your password.");
    } else if (!passRegex.test(password)) {
      alert("Please enter your password in a valid format.");
    }
    else{
        encryptCredentials();
    }
  }
  function encryptCredentials() {
    
  
   
  
    // Validate the inputs here...
  
    // Call the encrypt function from your Java program using Ajax
    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8080/encryptCredentials"; // Change this to your actual URL
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        // Handle the response from the server
        var encryptedCredentials = xhr.responseText;
        console.log("Encrypted credentials: " + encryptedCredentials);
      }
    };
    var data = JSON.stringify({
      webname: webname,
      password: password
    });
    xhr.send(data);
  }
  
