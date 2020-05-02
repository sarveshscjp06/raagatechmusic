// JavaScript Document

var xmlhttp;

function GetXmlHttpObject() {
    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        return new XMLHttpRequest();
    }
    if (window.ActiveXObject) {
        // code for IE6, IE5
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
    return null;
}

function submitnewsletter() {
    var newsletteremail = document.getElementById('newsletter_email').value;

    var email = document.getElementById('newsletter_email');
    if (trim(email.value) == '') {
        alert("Please enter your email");
        email.value = '';
        email.focus();
        return false;
    }
    else {
        emailadd = email.value;
        if (emailadd.indexOf(".") < 0 || emailadd.indexOf("@") < 0) {
            alert("Please enter a valid email");
            email.focus();
            return false;
        }
    }

    xmlhttp = GetXmlHttpObject();
    if (xmlhttp == null) {
        alert("Browser does not support HTTP Request");
        return;
    }
    var url = "templates/excelsoft/newsletter.php";
    url = url + "?getvalues=yes";
    url = url + "&newsletteremail=" + newsletteremail;
    url = url + "&sid=" + Math.random();
    xmlhttp.onreadystatechange = stateChanged;
    xmlhttp.open("GET", url, true);
    xmlhttp.send(null);
}

function trim(str) {
    return str.replace(/^\s+|\s+$/g, '');
}

function stateChanged() {
    if (xmlhttp.readyState == 4) {
        document.getElementById("display_message").innerHTML = xmlhttp.responseText;
    }
    else {
        document.getElementById("display_message").innerHTML = '<img src="templates/excelsoft/images/images/loader2.gif"> Loading..';
    }

}
