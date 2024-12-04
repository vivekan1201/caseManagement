            // Function to load services and display them in the table
            async function loadServices() {
                try {
                    const response = await fetch('http://localhost:8080/api/custom-services');
                    const services = await response.json();
                    const servicesList = document.getElementById('services-list');

                    // Clear previous services list
                    servicesList.innerHTML = '';

                    services.forEach(service => {
                        const row = document.createElement('tr');
                        row.innerHTML = `

                            <td>${service.serviceName}</td>
                            <td>
                                <button class="btn btn-sm btn-warning edit-btn" onclick="editService(${service.id})">Edit</button>
                                <button class="btn btn-sm btn-danger" onclick="deleteService(${service.id})">Delete</button>
                            </td>
                        `;
                        servicesList.appendChild(row);
                    });
                } catch (error) {
                    console.error('Error loading services:', error);
                }
            }

            // Function to open the Add Service form
            document.getElementById('add-service').addEventListener('click', () => {
                document.getElementById('new-services-container').style.display = 'block';
            });

            // Function to save a new service
            document.getElementById('save-service').addEventListener('click', async () => {
                const serviceName = document.getElementById('service-name').value;
                const contactClient = document.getElementById('contact-client').checked;
                const contactClientDays = document.getElementById('contact-client-days').value;
                const initialInterview = document.getElementById('initial-interview').checked;
                const interviewDays = document.getElementById('interview-days').value;
                const employmentAction = document.getElementById('employment-action').checked;
                const actionPlanDays = document.getElementById('action-plan-days').value;
                const contactMonthly = document.getElementById('contact-monthly').checked;

                const newService = {
                    serviceName,
                    taskDetails: [
                        { taskType: 'Contact Client', businessDays: contactClient ? parseInt(contactClientDays) : 0 },
                        { taskType: 'Initial Intake Interview', businessDays: initialInterview ? parseInt(interviewDays) : 0 },
                        { taskType: 'Employment Action Plan', businessDays: employmentAction ? parseInt(actionPlanDays) : 0 },
                        { taskType: 'Contact Client Monthly', businessDays: contactMonthly ? 1 : 0 }
                    ]
                };

                try {
                    const response = await fetch('http://localhost:8080/api/createService', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(newService)
                    });

                    if (response.ok) {
                        alert('Service saved successfully!');
                        loadServices();  // Reload the services list
                        document.getElementById('new-services-container').style.display = 'none'; // Hide form
                    } else {
                        alert('Failed to save service');
                    }
                } catch (error) {
                    console.error('Error saving service:', error);
                }
            });

            // Function to edit a service
            async function editService(serviceId) {
                // Similar logic for editing the service can go here
                // Fetch service details and populate the form for editing
            }

            // Function to delete a service
            async function deleteService(serviceId) {
                if (confirm('Are you sure you want to delete this service?')) {
                    try {
                        const response = await fetch(`http://localhost:8080/api/deleteService/${serviceId}`, {
                            method: 'DELETE'
                        });

                        if (response.ok) {
                            alert('Service deleted successfully!');
                            loadServices();  // Reload the services list
                        } else {
                            alert('Failed to delete service');
                        }
                    } catch (error) {
                        console.error('Error deleting service:', error);
                    }
                }
            }

            // Initial call to load services
            loadServices();
