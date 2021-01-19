$.when( $.ready ).then(function() {

const all = ["../assets/rimmed/IMG_4445.jpeg", "../assets/rimmed/IMG_4446.jpeg",
                 "../assets/rimmed/IMG_4465.jpeg", "../assets/rimmed/IMG_4469.jpeg",
                 "../assets/rimmed/IMG_4471.jpeg","../assets/rimmed/IMG_4473.jpeg",
                 "../assets/rimmed/IMG_4524.jpeg","../assets/rimmed/IMG_4532.jpeg",
                 "../assets/rimmed/IMG_4535.jpeg","../assets/rimmed/IMG_4543.jpeg",
                 "../assets/rimless/IMG_4031.jpeg", "../assets/rimless/IMG_4032.jpeg",
                 "../assets/rimless/IMG_4255.jpeg", "../assets/rimless/IMG_4276.jpeg",
                 "../assets/rimless/IMG_4401.jpeg","../assets/rimless/IMG_4462.jpeg",
                 "../assets/rimless/IMG_4485.jpeg","../assets/rimless/IMG_4486.jpeg"];
const rimmed = ["../assets/rimmed/IMG_4445.jpeg", "../assets/rimmed/IMG_4446.jpeg",
                 "../assets/rimmed/IMG_4465.jpeg", "../assets/rimmed/IMG_4469.jpeg",
                 "../assets/rimmed/IMG_4471.jpeg","../assets/rimmed/IMG_4473.jpeg",
                 "../assets/rimmed/IMG_4524.jpeg","../assets/rimmed/IMG_4532.jpeg",
                 "../assets/rimmed/IMG_4535.jpeg","../assets/rimmed/IMG_4543.jpeg"];
const cube = [];
const rimless = ["../assets/rimless/IMG_4031.jpeg", "../assets/rimless/IMG_4032.jpeg",
                 "../assets/rimless/IMG_4255.jpeg", "../assets/rimless/IMG_4276.jpeg",
                 "../assets/rimless/IMG_4401.jpeg","../assets/rimless/IMG_4462.jpeg",
                 "../assets/rimless/IMG_4485.jpeg","../assets/rimless/IMG_4486.jpeg"];
const nano = ["../assets/rimless/IMG_4494.jpeg", "../assets/rimless/IMG_4495.jpeg",
              "../assets/rimless/IMG_4496.jpeg"];
const shallow = [];

  $("#filterList").children("li.filterOpts:first-child").toggleClass("selected");
  var prevFilter = $("#filterList").children("li.filterOpts:first-child");

  $("#filterList").click(function(event) {
    var target = $(event.target);
    if(target.is("li")){
      prevFilter.toggleClass("selected");
      prevFilter = target;
      target.toggleClass("selected");
    }

  var currFilter;
  switch(target.attr("id")) {
  case "allTanks":
    currFilter = all;
    break;
  case "rimmedTank":
    currFilter = rimmed;
    break;
  case "cubedTank":
    currFilter = cube;
    break;
  case "rimlessTank":
    currFilter = rimless;
    break;
  case "nanoTank":
    currFilter = nano;
    break;
  case "shallowTank":
    currFilter = shallow;
    break;
  default:
    currFilter = all;
  }
  replaceImgs(currFilter);
  });

  function replaceImgs(tank){
    var imgArray = $("div.column > img");
    for(var i=0; i<tank.length; i++){
      imgArray[i].src = tank[i];
      $(imgArray[i]).show();
    }
    if(tank.length < imgArray.length){
      for(var i=tank.length; i<imgArray.length; i++){
        imgArray[i].src = "";
        $(imgArray[i]).hide();

        $($(".votingBtns")[i]).hide();
      }
    }
  }

});
