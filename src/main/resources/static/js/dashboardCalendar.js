!function() {

    var today = moment();

    function Calendar(selector, events) {
      this.el = document.querySelector(selector);
      this.events = events;
      this.current = moment().date(1);
      this.draw();
      var current = document.querySelector('.today');
      if(current) {
        var self = this;
        window.setTimeout(function() {
          self.openDay(current);
        }, 500);
      }
    }

    Calendar.prototype.draw = function() {
      //Create Header
      this.drawHeader();

      //Draw Month
      this.drawMonth();

      //this.drawLegend();
    }

    Calendar.prototype.drawHeader = function() {
      var self = this;
      if(!this.header) {
        //Create the header elements
        this.header = createElement('div', 'header');
        this.header.className = 'header';

        this.title = createElement('h1');

        var right = createElement('div', 'right');
        right.addEventListener('click', function() { self.nextMonth(); });

        var left = createElement('div', 'left');
        left.addEventListener('click', function() { self.prevMonth(); });

        //Append the Elements
        this.header.appendChild(this.title);
        this.header.appendChild(right);
        this.header.appendChild(left);
        this.el.appendChild(this.header);
      }

      this.title.innerHTML = this.current.format('MMMM YYYY');
    }

    Calendar.prototype.drawMonth = function() {
      var self = this;

      this.events.forEach(function(ev) {
        // Convert date to a Moment.js object if not already
        ev.date = moment(ev.date);
      });


      if(this.month) {
        this.oldMonth = this.month;
        this.oldMonth.className = 'month out ' + (self.next ? 'next' : 'prev');
        this.oldMonth.addEventListener('webkitAnimationEnd', function() {
          self.oldMonth.parentNode.removeChild(self.oldMonth);
          self.month = createElement('div', 'month');
          self.backFill();
          self.currentMonth();
          self.fowardFill();
          self.el.appendChild(self.month);
          window.setTimeout(function() {
            self.month.className = 'month in ' + (self.next ? 'next' : 'prev');
          }, 16);
        });
      } else {
          this.month = createElement('div', 'month');
          this.el.appendChild(this.month);
          this.backFill();
          this.currentMonth();
          this.fowardFill();
          this.month.className = 'month new';
      }
    }

    Calendar.prototype.backFill = function() {
      var clone = this.current.clone();
      var dayOfWeek = clone.day();

      if(!dayOfWeek) { return; }

      clone.subtract('days', dayOfWeek+1);

      for(var i = dayOfWeek; i > 0 ; i--) {
        this.drawDay(clone.add('days', 1));
      }
    }

    Calendar.prototype.fowardFill = function() {
      var clone = this.current.clone().add('months', 1).subtract('days', 1);
      var dayOfWeek = clone.day();

      if(dayOfWeek === 6) { return; }

      for(var i = dayOfWeek; i < 6 ; i++) {
        this.drawDay(clone.add('days', 1));
      }
    }

    Calendar.prototype.currentMonth = function() {
      var clone = this.current.clone();

      while(clone.month() === this.current.month()) {
        this.drawDay(clone);
        clone.add('days', 1);
      }
    }

    Calendar.prototype.getWeek = function(day) {
      if(!this.week || day.day() === 0) {
        this.week = createElement('div', 'week');
        this.month.appendChild(this.week);
      }
    }

    Calendar.prototype.drawDay = function(day) {
      var self = this;
      this.getWeek(day);

      //Outer Day
      var outer = createElement('div', this.getDayClass(day));
      outer.addEventListener('click', function() {
        self.openDay(this);
      });

      //Day Name
      var name = createElement('div', 'day-name', day.format('ddd'));

      //Day Number
      var number = createElement('div', 'day-number', day.format('DD'));


      //Events
      var events = createElement('div', 'day-events');
      this.drawEvents(day, events);

      outer.appendChild(name);
      outer.appendChild(number);
      outer.appendChild(events);
      this.week.appendChild(outer);
    }

    Calendar.prototype.drawEvents = function(day, element) {
      if(day.month() === this.current.month()) {
        var todaysEvents = this.events.reduce(function(memo, ev) {
          if(ev.date.isSame(day, 'day')) {
            memo.push(ev);
          }
          return memo;
        }, []);

        todaysEvents.forEach(function(ev) {
          var evSpan = createElement('span', ev.color);
          element.appendChild(evSpan);
        });
      }
    }

    Calendar.prototype.getDayClass = function(day) {
      classes = ['day'];
      if(day.month() !== this.current.month()) {
        classes.push('other');
      } else if (today.isSame(day, 'day')) {
        classes.push('today');
      }
      return classes.join(' ');
    }

    Calendar.prototype.openDay = function(el) {
      var details, arrow;
      var dayNumber = +el.querySelectorAll('.day-number')[0].innerText || +el.querySelectorAll('.day-number')[0].textContent;
      var day = this.current.clone().date(dayNumber);

      var currentOpened = document.querySelector('.details');

      //Check to see if there is an open detais box on the current row
      if(currentOpened && currentOpened.parentNode === el.parentNode) {
        details = currentOpened;
        arrow = document.querySelector('.arrow');
      } else {
        //Close the open events on differnt week row
        //currentOpened && currentOpened.parentNode.removeChild(currentOpened);
        if(currentOpened) {
          currentOpened.addEventListener('webkitAnimationEnd', function() {
            currentOpened.parentNode.removeChild(currentOpened);
          });
          currentOpened.addEventListener('oanimationend', function() {
            currentOpened.parentNode.removeChild(currentOpened);
          });
          currentOpened.addEventListener('msAnimationEnd', function() {
            currentOpened.parentNode.removeChild(currentOpened);
          });
          currentOpened.addEventListener('animationend', function() {
            currentOpened.parentNode.removeChild(currentOpened);
          });
          currentOpened.className = 'details out';
        }

        //Create the Details Container
        details = createElement('div', 'details in');

        //Create the arrow
        var arrow = createElement('div', 'arrow');

        //Create the event wrapper

        details.appendChild(arrow);
        el.parentNode.appendChild(details);
      }

      var todaysEvents = this.events.reduce(function(memo, ev) {
        if(ev.date.isSame(day, 'day')) {
          memo.push(ev);
        }
        return memo;
      }, []);

      this.renderEvents(todaysEvents, details);

      arrow.style.left = el.offsetLeft - el.parentNode.offsetLeft + 27 + 'px';
    }

    Calendar.prototype.renderEvents = function(events, ele) {
      //Remove any events in the current details element
      var currentWrapper = ele.querySelector('.events');
      var wrapper = createElement('div', 'events in' + (currentWrapper ? ' new' : ''));

      events.forEach(function(ev) {
        var div = createElement('div', 'event');
        var square = createElement('div', 'event-category ' + ev.color);
        var span = createElement('span', '', ev.eventName);

        div.appendChild(square);
        div.appendChild(span);
        wrapper.appendChild(div);
      });

      if(!events.length) {
        var div = createElement('div', 'event empty');
        var span = createElement('span', '', 'No Events');

        div.appendChild(span);
        wrapper.appendChild(div);
      }

      if(currentWrapper) {
        currentWrapper.className = 'events out';
        currentWrapper.addEventListener('webkitAnimationEnd', function() {
          currentWrapper.parentNode.removeChild(currentWrapper);
          ele.appendChild(wrapper);
        });
        currentWrapper.addEventListener('oanimationend', function() {
          currentWrapper.parentNode.removeChild(currentWrapper);
          ele.appendChild(wrapper);
        });
        currentWrapper.addEventListener('msAnimationEnd', function() {
          currentWrapper.parentNode.removeChild(currentWrapper);
          ele.appendChild(wrapper);
        });
        currentWrapper.addEventListener('animationend', function() {
          currentWrapper.parentNode.removeChild(currentWrapper);
          ele.appendChild(wrapper);
        });
      } else {
        ele.appendChild(wrapper);
      }
    }

    // Calendar.prototype.drawLegend = function() {
    //   var legend = createElement('div', 'legend');
    //   var calendars = this.events.map(function(e) {
    //     return e.calendar + '|' + e.color;
    //   }).reduce(function(memo, e) {
    //     if(memo.indexOf(e) === -1) {
    //       memo.push(e);
    //     }
    //     return memo;
    //   }, []).forEach(function(e) {
    //     var parts = e.split('|');
    //     var entry = createElement('span', 'entry ' +  parts[1], parts[0]);
    //     legend.appendChild(entry);
    //   });
    //   this.el.appendChild(legend);
    // }

    Calendar.prototype.nextMonth = function() {
      this.current.add('months', 1);
      this.next = true;
      this.draw();
    }

    Calendar.prototype.prevMonth = function() {
      this.current.subtract('months', 1);
      this.next = false;
      this.draw();
    }

    window.Calendar = Calendar;

    function createElement(tagName, className, innerText) {
      var ele = document.createElement(tagName);
      if(className) {
        ele.className = className;
      }
      if(innerText) {
        ele.innderText = ele.textContent = innerText;
      }
      return ele;
    }
  }();

   !function() {
       var data = [];

       // Fetch the username first
       fetch('http://localhost:8080/api/getUsername')
           .then(response => response.text()) // Use .text() to handle plain text responses
           .then(username => {
               // Fetch additional events using the retrieved username
               return fetch(`http://localhost:8080/api/events/employee/${username}`);
           })
           .then(response => response.json())
           .then(events => {
               events.forEach(event => {
                   // Extract and process event details
                   const eventName = event.eventName;
                   const eventDate = new Date(event.eventDate).toISOString().split('T')[0]; // Convert to YYYY-MM-DD
                   const calendar = 'Work'; // Default calendar
                   const color = 'orange'; // Default color

                   // Add the event to the data array
                   data.push({ eventName, calendar, color, date: eventDate });
               });

               console.log('Final data:', data);

               // Initialize the calendar after fetching and adding new events
               var calendar = new Calendar('#calendar', data);
           })
           .catch(error => console.error('Error:', error));
   }();

 let username = '';  // Declare the variable to store the username

 // Function to fetch the username first
 async function fetchUsername() {
   try {
     // Fetch the username from the API
     const response = await fetch('http://localhost:8080/api/getUsername');
     username = await response.text();  // Store the fetched username in the 'username' variable
     console.log('Username fetched:', username);  // Log the username

     // Now that the username is fetched, you can call other functions that depend on it
     fetchClients(username);  // Pass the username to another function
   } catch (error) {
     console.error('Error fetching username:', error);
   }
 }

 // Function to fetch clients (example usage)
 async function fetchClients(username) {
   try {
     const response = await fetch(`http://localhost:8080/api/recentClients?staff=${username}`);
     const clients = await response.json();
     console.log(clients);  // Process the clients data

     // Your existing code to populate the table goes here...
     const tableBody = document.getElementById('client-table-body');
     clients.forEach(client => {
       const fullName = `${client.firstName} ${client.lastName}`;
       const row = document.createElement('tr');
       row.innerHTML = `
         <td>${fullName}</td>
         <td>${client.email}</td>
         <td>${client.service}</td>
         <td>${client.address}</td>
         <td>
           <button class="btn btn-primary btn-sm view-all-btn" data-client-id="${client.id}">View All</button>
         </td>
       `;
       tableBody.appendChild(row);
     });

     // Add event listeners for "View All" buttons
     const viewAllButtons = document.querySelectorAll('.view-all-btn');
     viewAllButtons.forEach(button => {
       button.addEventListener('click', () => {
         const clientId = button.getAttribute('data-client-id');
         const client = clients.find(client => client.id === clientId);
         showPopup(client);
       });
     });
   } catch (error) {
     console.error('Error fetching clients:', error);
   }
 }

 // Show the popup with the full client details
 function showPopup(client) {
   const popupOverlay = document.querySelector('.popup-overlay');
   const popupBody = document.getElementById('popup-body');
   popupBody.innerHTML = `
     <p><strong>Full Name:</strong> ${client.firstName} ${client.lastName}</p>
     <p><strong>Email:</strong> ${client.email}</p>
     <p><strong>Phone:</strong> ${client.phone}</p>
     <p><strong>Address:</strong> ${client.address}</p>
     <p><strong>Service:</strong> ${client.service}</p>
     <p><strong>Reference Number:</strong> ${client.referenceNumber}</p>
     <p><strong>Referral Date:</strong> ${new Date(client.referralDate).toLocaleDateString()}</p>
     <p><strong>Date of Birth:</strong> ${new Date(client.dob).toLocaleDateString()}</p>
     <p><strong>Start Date:</strong> ${new Date(client.startDate).toLocaleDateString()}</p>
     <p><strong>Region:</strong> ${client.region}</p>
   `;
   popupOverlay.style.display = 'block';
 }

 // Close the popup when the close button is clicked
 document.querySelector('.popup-close').addEventListener('click', () => {
   document.querySelector('.popup-overlay').style.display = 'none';
 });

 // Close the popup when clicking outside of the popup content
 document.querySelector('.popup-overlay').addEventListener('click', (event) => {
   if (event.target === document.querySelector('.popup-overlay')) {
     document.querySelector('.popup-overlay').style.display = 'none';
   }
 });

 // Call the function to fetch the username first
 fetchUsername();

