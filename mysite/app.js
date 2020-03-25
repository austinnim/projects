document.addEventListener("DOMContentLoaded", function(){
  var ajaxRequest;

  function createRequest(){
    try {
      ajexRequest = new XMLHttpRequest();
    } catch (e) {
      try {
        ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
      } catch(e) {
        try {
          ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e) {
          alert("Your browser broke!");
          return false;
        }
      }
    }
  }

  function buildEmail(){
    createRequest();

  }
});
