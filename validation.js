function validation(event){
    event.preventDefault();
    var name=document.getElementById("name").value;
    var email=document.getElementById("email").value;
    var password=document.getElementById("pass").value;
    var confirmpassword=document.getElementById("cpass").value;
    
      

    var words=/^[a-zA-Z]+$/;
    var symbol=/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    var pass=/^[a-zA-Z0-9/\\<>()!@#$%^&*_+,."':;]{7,14}$/;
    
    if(name==""){
        alert('please enter your name');
    }
    else if(!words.test(name)){
        alert('please enter your name in a valid format')
    }
    else if(email==""){
        alert('enter your email');
    }
    else if(!symbol.test(email)){
        alert('enter your email in valid format');
    }
    else if(password==""){
        alert('please enter your password');
    }
    else if(!pass.test(password)){
        alert('please enter your password in a valid format')
    }
    else if(confirmpassword!=password){
        alert('your password is not matched retype it');
    }
    else{
        alert('thanks for your registeration');
    }




}