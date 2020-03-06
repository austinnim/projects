/*
Author: Austin Nim
Filename: todolist.js
Description: The main user interactions are handled within this file. Utilizes two arrays to
store the todo list and accomplished task, which are stored via localstorage as
strings.
*/


// once the browser and html content have been loaded add the remaining elements
document.addEventListener("DOMContentLoaded", function (){
  var todoArray = JSON.parse(localStorage.getItem("list"));
  var finishedTask = JSON.parse(localStorage.getItem("completed"));
  var overdue = [];
  var newDay = false;

  // load date
  var currDate = new Date();
  var day, month;
  month = currDate.getMonth() + 1;
  day = currDate.getDate();
  currDate = month + "/" + day;
  document.getElementById("today").innerText = currDate;

  // load completed task
  if(finishedTask != null && finishedTask[0] == currDate){
    for(var i=1; i < finishedTask.length; i++){
      var newListItem = document.createElement("li");
      newListItem.innerText = finishedTask[i];
      var liNode = document.getElementById("completed").appendChild(newListItem);
    }
  } else {
    finishedTask = [];
    finishedTask[0] = currDate;
    newDay = true;
    localStorage.setItem("completed", JSON.stringify(finishedTask));
  }
  if(todoArray != null && todoArray.length != 0){
    loadTodoList();
    if(newDay){
      overdue = todoArray.slice(0, todoArray.length-1);
      for(let i=0; i<overdue.length;i++){
        document.querySelectorAll("li")[i].style.backgroundColor = orange;
      }
    }
  } else {
    todoArray = [];
  }

  // load unfinished task

  // set up submit button for users to submit new task
  document.getElementById("submitTask").addEventListener("click", function(){
    // create new li node to be inserted
    var newListItem = document.createElement("li");
    var addTask = document.getElementById("newTask").value;
    newListItem.innerText = addTask;
    document.getElementById("todolist").appendChild(newListItem);
    newListItem = document.querySelectorAll("li")[[(todoArray.push(addTask))-1]];
    document.getElementById("newTask").value= "";
    // creates a click event to mark task as complete
    newListItem.addEventListener("click", function(){
      // creates a new item for completed list
      var item = document.createElement("li");
      item.innerText = this.innerText;
      document.getElementById("completed").appendChild(item);
      finishedTask.push(this.innerText);
      // remove the task from the todo array
      todoArray.splice(todoArray.indexOf[this.innerText], 1);
      this.remove();
      // replace the previous todo and completed arrays
      localStorage.setItem("list", JSON.stringify(todoArray));
      localStorage.setItem("completed", JSON.stringify(finishedTask));
    });
    localStorage.setItem("list", JSON.stringify(todoArray));
  });

  function loadTodoList (){
    // loop through todo array and create li items for each item in the array.
    // create event listeners to mark task as complete
    for(let i=0; i < todoArray.length; i++){
      var newListItem = document.createElement("li");
      newListItem.innerText = todoArray[i];
      newListItem.className = "notcompleted";
      var liNode = document.getElementById("todolist").appendChild(newListItem);
      liNode.addEventListener("click", function(){
        todoArray.splice(todoArray.indexOf[this.innerText], 1);
        var item = document.createElement("li");
        item.innerText = this.innerText;
        document.getElementById("completed").appendChild(item);
        finishedTask.push(this.innerText);
        this.remove();
        localStorage.setItem("list", JSON.stringify(todoArray));
        localStorage.setItem("completed", JSON.stringify(finishedTask));
      });
    }
  }
});
