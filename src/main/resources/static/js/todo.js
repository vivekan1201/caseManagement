const todoList = document.querySelector(".todos");
const dayTitle = document.querySelector("#dayName");

// Fetch staff name
async function fetchStaffName() {
  try {
    const response = await fetch("http://localhost:8080/api/getUsername");
    if (!response.ok) {
      throw new Error("Failed to fetch staff name");
    }
    const staffName = await response.text(); // Response is plain text
    console.log("Staff name:", staffName);
    fetchTasks(staffName); // Use staff name to fetch tasks
  } catch (error) {
    console.error("Error fetching staff name:", error);
  }
}

// Fetch tasks for the staff
async function fetchTasks(staffName) {
  try {
    const response = await fetch(`http://localhost:8080/api/staffstatus?staff=${staffName}&status=Pending`);
    if (!response.ok) {
      throw new Error("Failed to fetch tasks");
    }
    const tasks = await response.json();
    displayTasks(tasks); // Display tasks in the to-do list
  } catch (error) {
    console.error("Error fetching tasks:", error);
  }
}

// Display tasks in the to-do list
function displayTasks(tasks) {
  todoList.innerHTML = ""; // Clear any existing tasks
  tasks.forEach((task, index) => {
    const html = `
      <li>
        <input type="checkbox" id="todo_${index}" data-task-id="${task.taskId}">
        <label for="todo_${index}">
          <span class="check"></span>
          ${task.taskName}
        </label>
        <i class="far fa-trash-alt delete"></i>
      </li>`;
    todoList.innerHTML += html;
  });
}

// Update task status when a checkbox is clicked
async function updateTaskStatus(taskId) {
  try {
    const response = await fetch(`http://localhost:8080/api/updateStatus?taskId=${taskId}&newStatus=Completed`, {
      method: "GET",
    });
    if (!response.ok) {
      throw new Error("Failed to update task status");
    }
    console.log(`Task ${taskId} marked as completed.`);
  } catch (error) {
    console.error("Error updating task status:", error);
  }
}

// Event listener for checkbox click
todoList.addEventListener("change", (e) => {
  if (e.target.type === "checkbox") {
    const taskId = e.target.dataset.taskId;
    if (e.target.checked) {
      updateTaskStatus(taskId); // Mark the task as completed
    }
  }
});

// Initialize the app by fetching staff name and tasks
fetchStaffName();
