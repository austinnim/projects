
$.when( $.ready ).then(function() {
  var index = 1;
  var picArray = ["./assets/filler.jpeg", "./assets/IMG_4495.jpeg", "./assets/IMG_4524.jpeg",
                  "./assets/IMG_4400.jpeg", "./assets/IMG_4461.jpeg", "./assets/IMG_4469.jpeg",
                  "./assets/IMG_4483.jpeg", "./assets/IMG_4379.jpeg"];
var slides = $(".slides");
console.log(slides);
// sets up the slideshow
var leftSlide = slides[0];
var mainSlide = slides[1];
var rightSlide = slides[2];
  $(leftSlide).attr("src", picArray[index - 1]);
  console.log("left = " + leftSlide.src);
  $(mainSlide).attr("src", picArray[index]);
  console.log("main = " + mainSlide.src);
  $(rightSlide).attr("src", picArray[index + 1]);
  console.log("right = " + rightSlide.src);

/*

*/
$("#prev").click(function(){
    $(rightSlide).attr("src", picArray[index]);
    $(mainSlide).attr("src", picArray[--index]);
    if(index == 6){
      $("#rightSlide").css("visibility", "visible");
      $("#next").css("visibility", "visible");
    }
    if(index == 0){
          $("#leftSlide").css("visibility", "hidden");
          $("#prev").css("visibility", "hidden");
          console.log(mainSlide);
    } else {
        $(leftSlide).attr("src", picArray[index - 1]);
    }
    console.log("index = " + index);
  });

  $("#next").click(function(){
    $(leftSlide).attr("src", picArray[index]);
    $(mainSlide).attr("src", picArray[++index]);
    if(index == 1) {
      $("#leftSlide").css("visibility", "visible");
      $("#prev").css("visibility", "visible");
    }
    if(index == 7) {
      $("#rightSlide").css("visibility", "hidden");
      $("#next").css("visibility", "hidden");
    } else {
      $(rightSlide).attr("src", picArray[index + 1]);
    }
    console.log("index = " + index);

  });



});
