document.addEventListener("DOMContentLoaded", function() {
    var userType = new URLSearchParams(window.location.search).get('user');

    var hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "userType";
    hiddenInput.value = userType;
    document.getElementById('loginForm').appendChild(hiddenInput);

    sessionStorage.removeItem('userType');

    if (userType === "doctor") {
        document.getElementById("doctorFields").style.display = "block";
        sessionStorage.setItem('userType', "doctor");
    }

    if (userType === "patient") {
        document.getElementById("patientFields").style.display = "block";
        sessionStorage.setItem('userType', "patient");
    }
});
