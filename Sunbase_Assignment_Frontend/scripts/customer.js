const base_url = 'http://localhost:8080/api/sunbasedata';

let currentPage = 0;
const token = localStorage.getItem('token');
let customerId = 0;

// Ensure the user is authenticated
document.addEventListener('DOMContentLoaded', () => {
    if (!token) {
        window.location.href = './index.html';
    } else {
        fetchCustomer();
    }
});

// Fetch customers based on filters and pagination
async function fetchCustomer(searchBy = 'firstName', searchTerm = '', sortBy = 'firstName', sortOrder = 'asc', page = 0) {
    const url = new URL(`${base_url}/search-customers`);
    url.searchParams.append('searchBy', searchBy);
    url.searchParams.append('searchTerm', searchTerm);
    url.searchParams.append('sort', sortBy);
    url.searchParams.append('dir', sortOrder);
    url.searchParams.append('page', page);
    url.searchParams.append('size', 5);

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status === 401) {
            alert('Session expired. Please log in again.');
            window.location.href = './index.html'; // Redirect to login page
        } else if (response.status === 403) {
            alert('Forbidden access. Contact the administrator.');
        } else if (response.ok) {
            const customersPage = await response.json();
            const customers = customersPage._embedded.customerList;
            const customerTableBody = document.getElementById('customers');
            customerTableBody.innerHTML = '';

            if (customers.length === 0) {
                alert('No customers match the given criteria.');
            } else {
                customers.forEach(customer => {
                    const user = customer;
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>${customer.firstName}</td>
                        <td>${customer.lastName}</td>
                        <td>${customer.street}</td>
                        <td>${customer.address}</td>
                        <td>${customer.city}</td>
                        <td>${customer.state}</td>
                        <td>${customer.email}</td>
                        <td>${customer.phone}</td>
                        <td><button style="background-color: green;" class="btn btn-secondary" onclick="customerUpdateHnadller(${customer.custId})">Edit</button></td>
                        <td><button style="background-color: red;" class="btn btn-secondary" onclick="deleteCustomer(${customer.custId})">Delete</button></td>
                    `;
                    customerTableBody.appendChild(tr);
                });

                // Handle pagination controls
                document.getElementById('prevPageButton').style.display = customersPage.page.number > 0 ? 'block' : 'none';
                document.getElementById('nextPageButton').style.display = customersPage.page.number < customersPage.page.totalPages - 1 ? 'block' : 'none';
            }
        } else {
            alert('Failed to fetch customers!');
        }
    } catch (error) {
        console.error('Error fetching customers:', error);
    }
}

// Sync customers with server
async function syncCustomers() {
    try {
        const response = await fetch(`${base_url}/sync-customers`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            alert('Customers synced successfully!');
            fetchCustomer();
        } else if (response.status === 401) {
            alert('Session expired. Please log in again.');
            window.location.href = './index.html';
        } else if (response.status === 403) {
            alert('You do not have permission to perform this action.');
        } else {
            alert('Failed to sync customers!');
        }
    } catch (error) {
        console.error('Error syncing customers:', error);
    }
}

// Apply filters and sort
function applyFilters() {
    const searchBy = document.getElementById('searchBy').value;
    const searchTerm = document.getElementById('searchTerm').value;
    const sortBy = document.getElementById('sortBy').value;
    const sortOrder = document.getElementById('sortOrder').value;
    fetchCustomer(searchBy, searchTerm, sortBy, sortOrder, currentPage);
}

// Pagination controls
function nextPage() {
    currentPage++;
    applyFilters();
}

function prevPage() {
    if (currentPage > 0) {
        currentPage--;
        applyFilters();
    }
}

// Open the Add Customer popup
function openRegisterCustomerPopup() {
    document.getElementById('addCustomerPopup').style.display = 'flex';
}

// Close the register Customer popup
function closeRegisterCustomerPopup() {
    document.getElementById('addCustomerPopup').style.display = 'none';
}

// Resiter a new customer
async function registerCustomer() {
    const customer = {
        first_name: document.getElementById('newFirstName').value,
        last_name: document.getElementById('newLastName').value,
        street: document.getElementById('newStreet').value,
        address: document.getElementById('newAddress').value,
        city: document.getElementById('newCity').value,
        state: document.getElementById('newState').value,
        email: document.getElementById('newEmail').value,
        phone: document.getElementById('newPhone').value,
        password: document.getElementById('newPassword').value
    };

    try {
        const response = await fetch(`${base_url}/register-customer`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(customer)
        });

        if (response.status === 401) {
            alert('Session expired. Please log in again.');
            window.location.href = './index.html'; // Redirect to login page
        } else if (response.status === 403) {
            alert('Forbidden access. Contact the administrator.');
        } else if (response.ok) {
            alert('Customer added successfully!');
            closeRegisterCustomerPopup();
            fetchCustomer();
        } else {
            alert('Failed to add customer!');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
// customer update pop handler

async function customerUpdateHnadller(id) {
    customerId = id;    //updatecustomer id for uapdate to current id

    try {
        const response = await fetch(`${base_url}/get-customer/${id}`, {
            method: 'Get',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        if (response.ok) {
            const customer = await response.json();
            if (customer.custId == id) {
                openUpdateCustomerPopup(customer);
            } else {
                alert("customer not found...")
            }
        } else if (response.status == 401) {
            alert('Session expired. Please log in again.');
            window.location.href = './index.html'; // Redirect to login page
        } else if (response.status == 403) {
            alert('Forbidden access. Contact the administrator.');
        } else {
            alert('something went wrong.....');
        }
    } catch (error) {
        console.error('Error deleting customer:', error);
    }
}
// Open the Edit Customer popup with pre-filled data
function openUpdateCustomerPopup(customer) {

    console.log("Custmoer update pop page clalling------");

    document.getElementById('updateFirstName').value = customer.firstName;
    document.getElementById('updateLastName').value = customer.lastName;
    document.getElementById('updateStreet').value = customer.street;
    document.getElementById('updateAddress').value = customer.address;
    document.getElementById('updateCity').value = customer.city;
    document.getElementById('updateState').value = customer.state;
    document.getElementById('updateEmail').value = customer.email;
    document.getElementById('updatePhone').value = customer.phone;
    document.getElementById('updateCustomerPopup').style.display = 'flex';
}

// Close the Edit Customer popup
function closeUpdateCustomerPopup() {
    document.getElementById('updateCustomerPopup').style.display = 'none';
}

// Update customer details
async function updateCustomer() {
    const updatedCustomer = {
        first_name: document.getElementById('updateFirstName').value,
        last_name: document.getElementById('updateLastName').value,
        street: document.getElementById('updateStreet').value,
        address: document.getElementById('updateAddress').value,
        city: document.getElementById('updateCity').value,
        state: document.getElementById('updateState').value,
        email: document.getElementById('updateEmail').value,
        phone: document.getElementById('updatePhone').value
    };
    if (customerId == 0) {
        alert("customer id doesn't match")
        closeUpdateCustomerPopup();
        return;
    }
    if (customerId == 1) {
        alert("You can't Update Super Admin Details ");
        closeUpdateCustomerPopup();
        return;
    }


    try {
        const response = await fetch(`${base_url}/update-customer/${customerId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(updatedCustomer)
        });
        if (response.ok) {
            alert('Customer updated successfully!');
            closeUpdateCustomerPopup();
            fetchCustomer();
        }
        else if (response.status === 401) {
            alert('Session expired. Please log in again.');
            window.location.href = './index.html'; // Redirect to login page
        } else if (response.status === 403) {
            alert('Forbidden access. Contact the administrator.');
        } else {
            alert('Failed to update customer!');
        }
    } catch (error) {
        console.error('Error updating customer:', error);
    }
}


// Delete a customer
async function deleteCustomer(id) {

    if (id == 1) {
        alert("You can't Delete to Super Admin")
        return;
    }
    console.log("================================")
    if (confirm('Are you sure you want to delete this customer?')) {
        try {
            const response = await fetch(`${base_url}/delete-customer/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.status === 401) {
                alert('Session expired. Please log in again.');
                window.location.href = './index.html'; // Redirect to login page
            } else if (response.status === 403) {
                alert('Forbidden access. Contact the administrator.');
            } else if (response.ok) {
                alert('Customer deleted successfully!');
                fetchCustomer();
            } else {
                alert('Failed to delete customer!');
            }
        } catch (error) {
            console.error('Error deleting customer:', error);
        }
    }
}
